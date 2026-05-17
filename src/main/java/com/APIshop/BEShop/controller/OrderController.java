package com.APIshop.BEShop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.APIshop.BEShop.config.AppConstants;
import com.APIshop.BEShop.enums.OrderStatus;
import com.APIshop.BEShop.payloads.dto.order.OrderDTO;
import com.APIshop.BEShop.payloads.request.order.OrderRequest;
import com.APIshop.BEShop.payloads.request.order.OrderStatusRequest;
import com.APIshop.BEShop.payloads.response.OrderResponse;
import com.APIshop.BEShop.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<OrderResponse> getAllByUserId(
            @PathVariable String userId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        return ResponseEntity.ok(orderService.getAllByUserId(status, userId,
                pageNumber == 0 ? pageNumber : pageNumber - 1,
                pageSize, "id".equals(sortBy) ? "orderId" : sortBy,
                sortOrder));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<OrderResponse> getAll(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        return ResponseEntity.ok(orderService.getAll(status,
                pageNumber == 0 ? pageNumber : pageNumber - 1,
                pageSize, "id".equals(sortBy) ? "orderId" : sortBy,
                sortOrder));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @PatchMapping("/status")
    public ResponseEntity<OrderDTO> changeStatus(@RequestBody @Valid OrderStatusRequest request) {
        return ResponseEntity.ok(orderService.changeStatus(request));
    }
}