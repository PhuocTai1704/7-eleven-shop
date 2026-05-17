package com.APIshop.BEShop.payloads.request.order;

import org.hibernate.annotations.NotFound;

import com.APIshop.BEShop.enums.OrderStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusRequest {

    @NotBlank(message = "Mã đơn hàng không được để trống")
    private String orderId;

    @NotNull(message = "Trạng thái không được để trống")
    private OrderStatus orderStatus;

}
