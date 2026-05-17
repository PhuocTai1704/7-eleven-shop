package com.APIshop.BEShop.service;

import com.APIshop.BEShop.enums.OrderStatus;
import com.APIshop.BEShop.payloads.dto.order.OrderDTO;
import com.APIshop.BEShop.payloads.request.order.OrderRequest;
import com.APIshop.BEShop.payloads.request.order.OrderStatusRequest;
import com.APIshop.BEShop.payloads.response.OrderResponse;

public interface OrderService {
    OrderDTO getById(String orderId);

    OrderResponse getAllByUserId(OrderStatus status, String userId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    OrderResponse getAll(OrderStatus status, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderDTO create(OrderRequest request);

    OrderDTO changeStatus(OrderStatusRequest orderStatusRequest);

}
