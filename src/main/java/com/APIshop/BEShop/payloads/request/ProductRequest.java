package com.APIshop.BEShop.payloads.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String productId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

    @Positive(message = "Giá sản phẩm phải lớn hơn 0")
    private double price;

    @Min(value = 0, message = "Số lượng không được nhỏ hơn 0")
    private int quantity;

    @Min(value = 0, message = "Giảm giá tỷ lệ không được nhỏ hơn 0")
    private int discount;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean status;

    @Size(min = 6, message = "Mô tả sản phẩm phải có ít nhất 6 ký tự")
    private String description;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;
}
