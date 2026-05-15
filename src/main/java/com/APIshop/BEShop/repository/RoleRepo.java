package com.APIshop.BEShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APIshop.BEShop.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

}
