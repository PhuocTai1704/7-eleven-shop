package com.APIshop.BEShop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APIshop.BEShop.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    Page<Category> findAllByStatus(Boolean status, Pageable pageDetails);
}
