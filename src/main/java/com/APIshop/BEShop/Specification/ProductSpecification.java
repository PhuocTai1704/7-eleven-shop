package com.APIshop.BEShop.Specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.APIshop.BEShop.entity.Product;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {
    public static Specification<Product> filter(
            String search,
            Long categoryId,
            Boolean isSale,
            Boolean status,
            Boolean deleted) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (search != null) {
                predicates.add(cb.like(cb.lower(root.get("productName")), "%" + search.toLowerCase() + "%"));
            }

            // Filter by category
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("categoryId"), categoryId));
            }

            // Filter products on sale
            if (Boolean.TRUE.equals(isSale)) {
                predicates.add(cb.greaterThan(root.get("discount"), 0));
            }

            // Filter by status
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // Filter deleted products
            if (deleted != null) {
                predicates.add(cb.equal(root.get("deleted"), deleted));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
