package com.APIshop.BEShop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.APIshop.BEShop.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Size(min = 2, message = "Tên người nhận phải có ít nhất 2 ký tự")
    private String deliveryName;

    @Size(min = 10, max = 10, message = "Số điện thoại người nhận phải có đúng 10 ký tự")
    private String deliveryPhone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private String note;

    @OneToMany(mappedBy = "order", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private double subTotal;

    @Column(nullable = false)
    private double priceShip;

    @Column(nullable = false)
    private double totalAmount;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @NotNull(message = "Trạng thái đơn hàng là bắt buộc")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
