package com.APIshop.BEShop.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.APIshop.BEShop.entity.Category;
import com.APIshop.BEShop.exceptions.APIException;
import com.APIshop.BEShop.exceptions.ResourceNotFoundException;
import com.APIshop.BEShop.payloads.dto.category.CategoryDTO;
import com.APIshop.BEShop.payloads.request.CategoryRequset;
import com.APIshop.BEShop.payloads.response.CategoryResponse;
import com.APIshop.BEShop.repository.CategoryRepo;
import com.APIshop.BEShop.repository.ProductRepo;
import com.APIshop.BEShop.service.CategoryService;
import com.APIshop.BEShop.service.FileService;
import com.APIshop.BEShop.utils.Slug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final ProductRepo productRepo;

    private final CategoryRepo categoryRepo;

    private final ModelMapper modelMapper;

    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public CategoryDTO getById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục", "Id", categoryId));

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO getBySlug(String slug) {
        Category category = categoryRepo.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục", "slug", slug));

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryResponse getAll(Boolean status, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        // Select categories from database
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> pageCategories = status != null ? categoryRepo.findAllByStatus(status, pageDetails)
                : categoryRepo.findAll(pageDetails);

        List<CategoryDTO> categoryDTOs = pageCategories.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);
        categoryResponse.setPageNumber(pageCategories.getNumber());
        categoryResponse.setPageSize(pageCategories.getSize());
        categoryResponse.setTotalElements(pageCategories.getTotalElements());
        categoryResponse.setTotalPages(pageCategories.getTotalPages());
        categoryResponse.setLastPage(pageCategories.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO create(CategoryRequset request, MultipartFile image) throws IOException {
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setSlug(Slug.toSlug(request.getCategoryName()));
        if (image != null) {
            String fileName = fileService.uploadImage(path, image);
            category.setImage(fileName);
        }
        category.setStatus(request.getStatus());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        categoryRepo.save(category);

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO update(CategoryRequset request, MultipartFile image) throws IOException {
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục", "Id", request.getCategoryId()));
        category.setCategoryName(request.getCategoryName());
        category.setSlug(Slug.toSlug(request.getCategoryName()));
        if (image != null) {
            String fileName = fileService.uploadImage(path, image);
            category.setImage(fileName);
        }
        category.setStatus(request.getStatus());
        category.setUpdatedAt(LocalDateTime.now());

        categoryRepo.save(category);

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public void delete(Long categoryId) {
        if (!categoryRepo.existsById(categoryId)) {
            throw new ResourceNotFoundException("Danh mục", "Id", categoryId);
        }
        if (productRepo.existsByCategory_CategoryId(categoryId)) {
            throw new APIException("Danh mục đang liên kết nhiều sản phẩm không thể xóa!");
        }
        categoryRepo.deleteById(categoryId);
    }

}
