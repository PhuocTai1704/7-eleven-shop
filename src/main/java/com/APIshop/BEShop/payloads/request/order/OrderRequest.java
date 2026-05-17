package com.APIshop.BEShop.payloads.request.order;

import java.util.List;

import com.APIshop.BEShop.enums.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank(message = "Tên người nhận không được để trống")
    private String deliveryName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String deliveryPhone;

    private String note;

    @Valid
    @NotNull(message = "Danh sách sản phẩm không được null")
    @Size(min = 1, message = "Đơn hàng phải có ít nhất 1 sản phẩm")
    private List<OrderItemRequest> orderItems;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Phương thức không được để trống")
    private PaymentMethod paymentMethod;

}
