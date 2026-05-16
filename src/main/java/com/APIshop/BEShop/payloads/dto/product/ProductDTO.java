package com.APIshop.BEShop.payloads.dto.product;

import java.time.LocalDateTime;

import com.APIshop.BEShop.payloads.dto.category.CategoryDTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String productId;

    private String productName;

    private String slug;

    private String image;

    private double price;

    private int quantity;

    @Min(0)
    @Max(100)
    private int discount = 0;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean status;

    @NotBlank
    @Size(min = 6, message = "Mô tả sản phẩm phải có ít nhất 6 ký tự")
    private String description;

    private CategoryDTO category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
