package com.APIshop.BEShop.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.APIshop.BEShop.config.AppConstants;
import com.APIshop.BEShop.payloads.dto.product.ProductDTO;
import com.APIshop.BEShop.payloads.request.ProductRequest;
import com.APIshop.BEShop.payloads.response.ProductResponse;
import com.APIshop.BEShop.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductDTO> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getBySlug(slug));
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getAll(
            @RequestParam(required = false) String sreach,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean isSale,
            @RequestParam(required = false) Boolean status,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        return ResponseEntity.ok(productService.getAll(sreach, categoryId, isSale, status,
                pageNumber == 0 ? pageNumber : pageNumber - 1,
                pageSize, "id".equals(sortBy) ? "productId" : sortBy,
                sortOrder));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> create(
            @RequestParam(name = "image", required = false) MultipartFile image,
            @ModelAttribute @Valid ProductRequest request) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request, image));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<ProductDTO> update(
            @RequestParam(name = "image", required = false) MultipartFile image,
            @ModelAttribute @Valid ProductRequest request) throws IOException {

        return ResponseEntity.ok(productService.update(request, image));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        productService.delete(id);

        return ResponseEntity.noContent().build();
    }
}