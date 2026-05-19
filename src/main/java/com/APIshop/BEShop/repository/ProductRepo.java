package com.APIshop.BEShop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.APIshop.BEShop.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {

    Optional<Product> findBySlug(String slug);

    Page<Product> findAll(Specification<Product> productSpecification, Pageable pageDetails);

    boolean existsByCategory_CategoryId(Long categoryId);

    @Modifying
    @Query("""
                UPDATE Product p
                SET p.quantity = p.quantity - :qty
                WHERE p.productId = :productId
                  AND p.quantity >= :qty
                  AND p.deleted = false
                  AND p.status = true
            """)
    int decreaseQuantity(String productId, int qty);

    @Modifying
    @Query("UPDATE Product p SET p.deleted = true WHERE p.productId = :productId")
    void softDeleteById(String productId);

}
