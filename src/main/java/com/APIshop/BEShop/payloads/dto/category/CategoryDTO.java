package com.APIshop.BEShop.payloads.dto.category;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;

    private String categoryName;

    private String slug;

    private String image;

    private Boolean status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
