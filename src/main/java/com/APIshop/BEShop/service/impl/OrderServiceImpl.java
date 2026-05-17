package com.APIshop.BEShop.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.APIshop.BEShop.entity.Order;
import com.APIshop.BEShop.entity.OrderItem;
import com.APIshop.BEShop.entity.Payment;
import com.APIshop.BEShop.entity.Product;
import com.APIshop.BEShop.entity.User;
import com.APIshop.BEShop.enums.OrderStatus;
import com.APIshop.BEShop.exceptions.APIException;
import com.APIshop.BEShop.exceptions.ResourceNotFoundException;
import com.APIshop.BEShop.payloads.dto.order.OrderDTO;
import com.APIshop.BEShop.payloads.request.order.OrderItemRequest;
import com.APIshop.BEShop.payloads.request.order.OrderRequest;
import com.APIshop.BEShop.payloads.request.order.OrderStatusRequest;
import com.APIshop.BEShop.payloads.response.OrderResponse;
import com.APIshop.BEShop.repository.OrderItemRepo;
import com.APIshop.BEShop.repository.OrderRepo;
import com.APIshop.BEShop.repository.ProductRepo;
import com.APIshop.BEShop.repository.UserRepo;
import com.APIshop.BEShop.security.JWTUtil;
import com.APIshop.BEShop.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepo userRepo;

    private final OrderRepo orderRepo;

    private final OrderItemRepo orderItemRepo;

    private final ProductRepo productRepo;

    private final ModelMapper modelMapper;

    private final JWTUtil jwtUtil;

    @Override
    public OrderDTO getById(String orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaim("userId");
        String roles = jwt.getClaim("scope");

        // Get order by orderId from database
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        // Verify access permission
        if (!userId.equals(order.getUserId()) && !roles.contains("ADMIN")) {
            throw new AccessDeniedException("Bạn không có quyền truy cập đơn hàng này.");
        }

        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderResponse getAllByUserId(OrderStatus status, String userId, Integer pageNumber, Integer pageSize,
            String sortBy,
            String sortOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String Id = jwt.getClaim("userId");
        String roles = jwt.getClaim("scope");

        // Verify access permission
        if (!Id.equals(userId) && !roles.contains("ADMIN")) {
            throw new AccessDeniedException("Bạn không có quyền truy cập đơn hàng này.");
        }

        // Get orders by userId from database
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Order> pageOrders = status != null ? orderRepo.findAllByUserIdAndOrderStatus(userId, status, pageDetails)
                : orderRepo.findAllByUserId(userId, pageDetails);
        List<OrderDTO> orderDTOs = pageOrders.getContent().stream().map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());

        return orderResponse;
    }

    @Override
    public OrderResponse getAll(OrderStatus status, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        // Get orders by userId from database
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Order> pageOrders = status != null ? orderRepo.findAllByOrderStatus(status, pageDetails)
                : orderRepo.findAll(pageDetails);
        List<OrderDTO> orderDTOs = pageOrders.getContent().stream().map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());

        return orderResponse;
    }

    @Override
    public OrderDTO create(OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaim("userId");

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new APIException("Người dùng không tồn tại"));

        // Create order items and calculate subtotal
        List<OrderItem> orderItems = new ArrayList<>();
        double subTotal = 0;

        for (OrderItemRequest itemRequest : request.getOrderItems()) {
            Product product = productRepo.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new APIException("Sản phẩm không tồn tại: " + itemRequest.getProductId()));

            if (product.getDeleted()) {
                throw new APIException("Sản phẩm %s không còn tồn tại".formatted(product.getProductName()));
            }

            if (!product.getStatus()) {
                throw new APIException("Sản phẩm %s hiện không khả dụng".formatted(product.getProductName()));
            }

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new APIException("Sản phẩm %s không đủ số lượng".formatted(product.getProductName()));
            }
            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setDiscount(product.getDiscount());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
            subTotal += (product.getPrice() * (1 - product.getDiscount() / 100.0)) * itemRequest.getQuantity();
        }

        // Create Payment
        Payment payment = new Payment();
        payment.setPaymentMethod(request.getPaymentMethod());

        // Create Order
        double priceShip = 30000;

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryName(request.getDeliveryName());
        order.setDeliveryPhone(request.getDeliveryPhone());
        order.setAddress(request.getAddress());
        order.setNote(request.getNote());

        order.setOrderDateTime(LocalDateTime.now());
        order.setSubTotal(subTotal);
        order.setPriceShip(priceShip);
        order.setTotalAmount(subTotal + priceShip);
        order.setPayment(payment);
        order.setOrderStatus(OrderStatus.PENDING);

        // Assign order reference to each item
        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);

        orderRepo.save(order);

        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO changeStatus(OrderStatusRequest orderStatusRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaim("userId");
        String roles = jwt.getClaim("scope");

        // Get order by orderId from database
        Order order = orderRepo.findById(orderStatusRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderStatusRequest.getOrderId()));

        // Verify access permission
        if (!userId.equals(order.getUserId()) && !roles.contains("ADMIN")) {
            throw new AccessDeniedException("Bạn không có quyền truy cập đơn hàng này.");
        }
        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus status = orderStatusRequest.getOrderStatus();
        Boolean isAdmin = roles.contains("ADMIN");

        // Validate transition
        if (!currentStatus.canTransitionTo(status, isAdmin)) {
            throw new APIException("Bạn không thể chuyển từ %s sang %s".formatted(currentStatus.getDisplayName(),
                    status.getDisplayName()));
        }

        order.setOrderStatus(status);
        orderRepo.save(order);

        return modelMapper.map(order, OrderDTO.class);
    }

}
