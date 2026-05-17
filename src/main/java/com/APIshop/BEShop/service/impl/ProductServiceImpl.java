package com.APIshop.BEShop.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.APIshop.BEShop.Specification.ProductSpecification;
import com.APIshop.BEShop.entity.Category;
import com.APIshop.BEShop.entity.Product;
import com.APIshop.BEShop.exceptions.ResourceNotFoundException;
import com.APIshop.BEShop.payloads.dto.product.ProductDTO;
import com.APIshop.BEShop.payloads.request.ProductRequest;
import com.APIshop.BEShop.payloads.response.ProductResponse;
import com.APIshop.BEShop.repository.CategoryRepo;
import com.APIshop.BEShop.repository.OrderItemRepo;
import com.APIshop.BEShop.repository.ProductRepo;
import com.APIshop.BEShop.service.FileService;
import com.APIshop.BEShop.service.ProductService;
import com.APIshop.BEShop.utils.Slug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    private final CategoryRepo categoryRepo;

    private final OrderItemRepo orderItemRepo;

    private final ModelMapper modelMapper;

    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO getById(String productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "Id", productId));

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO getBySlug(String slug) {
        Product product = productRepo.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "slug", slug));

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductResponse getAll(Long categoryId, Boolean isSale, Boolean status, Integer pageNumber,
            Integer pageSize, String sortBy,
            String sortOrder) {
        // Select products from database
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Specification<Product> productSpecification = ProductSpecification.filter(categoryId, isSale, status, false);

        if (categoryId != null && !categoryRepo.existsById(categoryId)) {
            throw new ResourceNotFoundException("Danh mục", "Id", categoryId);
        }
        Page<Product> pageProducts = productRepo.findAll(productSpecification, pageDetails);

        List<ProductDTO> productDTOs = pageProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO create(ProductRequest request, MultipartFile image) throws IOException {
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục", "Id", request.getCategoryId()));

        Product product = new Product();
        product.setCategory(category);
        product.setProductName(request.getProductName());
        product.setSlug(Slug.toSlug(request.getProductName()));
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDiscount(request.getDiscount());
        product.setStatus(request.getStatus());
        product.setDescription(request.getDescription());
        // update file image
        if (image != null) {
            String fileName = fileService.uploadImage(path, image);
            product.setImage(fileName);
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // Save product to the database
        productRepo.save(product);

        return modelMapper.map(product, ProductDTO.class);

    }

    @Override
    public ProductDTO update(ProductRequest request, MultipartFile image) throws IOException {
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh mục", "Id", request.getCategoryId()));

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "Id", request.getProductId()));
        product.setCategory(category);
        product.setProductName(request.getProductName());
        product.setSlug(Slug.toSlug(request.getProductName()));
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDiscount(request.getDiscount());
        product.setStatus(request.getStatus());
        product.setDescription(request.getDescription());
        // update file image
        if (image != null) {
            String fileName = fileService.uploadImage(path, image);
            product.setImage(fileName);
        }

        product.setUpdatedAt(LocalDateTime.now());
        // Save product to the database
        productRepo.save(product);

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public void delete(String productId) {
        if (!productRepo.existsById(productId)) {
            throw new ResourceNotFoundException("Sản phẩm", "Id", productId);
        }
        if (orderItemRepo.existsByProduct_ProductId(productId)) {
            productRepo.softDeleteById(productId);
        } else {
            productRepo.deleteById(productId);
        }
    }

}
