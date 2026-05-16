package com.APIshop.BEShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APIshop.BEShop.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

}
