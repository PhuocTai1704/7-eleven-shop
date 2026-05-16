package com.APIshop.BEShop.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.APIshop.BEShop.payloads.dto.category.CategoryDTO;
import com.APIshop.BEShop.payloads.request.CategoryRequset;
import com.APIshop.BEShop.payloads.response.CategoryResponse;

public interface CategoryService {
    CategoryDTO getById(Long categoryId);

    CategoryDTO getBySlug(String slug);

    CategoryResponse getAll(Boolean status, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO create(CategoryRequset request, MultipartFile image) throws IOException;

    CategoryDTO update(CategoryRequset request, MultipartFile image) throws IOException;

    void delete(Long categoryId);
}