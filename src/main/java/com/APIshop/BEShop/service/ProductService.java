package com.APIshop.BEShop.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.APIshop.BEShop.payloads.dto.product.ProductDTO;
import com.APIshop.BEShop.payloads.request.ProductRequest;
import com.APIshop.BEShop.payloads.response.ProductResponse;

public interface ProductService {

    ProductDTO getById(String productId);

    ProductDTO getBySlug(String slug);

    ProductResponse getAll(Long categoryId, Boolean isSale, Boolean status, Integer pageNumber,
            Integer pageSize, String sortBy, String sortOrder);

    ProductDTO create(ProductRequest request, MultipartFile image) throws IOException;

    ProductDTO update(ProductRequest request, MultipartFile image) throws IOException;

    void delete(String productId);
}