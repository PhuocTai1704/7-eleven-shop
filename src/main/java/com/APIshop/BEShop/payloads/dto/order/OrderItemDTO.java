package com.APIshop.BEShop.payloads.dto.order;

import com.APIshop.BEShop.payloads.dto.product.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private ProductDTO product;

    private int quantity;

    private int discount;

    private double price;
}
