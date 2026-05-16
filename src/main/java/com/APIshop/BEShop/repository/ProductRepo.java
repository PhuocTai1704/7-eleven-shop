package com.APIshop.BEShop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APIshop.BEShop.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {

    Optional<Product> findBySlug(String slug);

    Page<Product> findAll(Specification<Product> productSpecification, Pageable pageDetails);

    boolean existsByCategory_CategoryId(Long categoryId);

}
