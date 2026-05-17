package com.APIshop.BEShop.enums;

import java.util.Map;
import java.util.Set;

public enum OrderStatus {
    PENDING("Chờ xử lý"),
    CONFIRMED("Đã xác nhận"),
    SHIPPED("Đang giao hàng"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy"),
    FAILED("Thất bại");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<OrderStatus, Set<OrderStatus>> CUSTOMER_TRANSITIONS = Map.of(
            PENDING, Set.of(CANCELLED));

    private static final Map<OrderStatus, Set<OrderStatus>> ADMIN_TRANSITIONS = Map.of(
            PENDING, Set.of(CONFIRMED, CANCELLED, FAILED),
            CONFIRMED, Set.of(SHIPPED, CANCELLED, FAILED),
            SHIPPED, Set.of(COMPLETED, FAILED));

    public boolean canTransitionTo(OrderStatus next, boolean isAdmin) {
        Map<OrderStatus, Set<OrderStatus>> transitions = isAdmin ? ADMIN_TRANSITIONS : CUSTOMER_TRANSITIONS;
        return transitions.getOrDefault(this, Set.of()).contains(next);
    }

}
