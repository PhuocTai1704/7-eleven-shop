package com.APIshop.BEShop.payloads.dto.order;

import java.time.LocalDateTime;
import java.util.List;

import com.APIshop.BEShop.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String orderId;

    private String userId;

    private String deliveryName;

    private String deliveryPhone;

    private List<OrderItemDTO> orderItems;

    private String address;

    private LocalDateTime orderDateTime;

    private PaymentDTO payment;

    private double subTotal;

    private double priceShip;

    private double totalAmount;

    private OrderStatus orderStatus;

}
