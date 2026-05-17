package com.APIshop.BEShop.payloads.request.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    @NotNull(message = "Mã sản phẩm không được để trống")
    private String productId;

    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn 0")
    @NotNull(message = "Số lượng sản phẩm không được để trống")
    private Integer quantity;
}
