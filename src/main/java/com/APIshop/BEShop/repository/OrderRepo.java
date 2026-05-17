package com.APIshop.BEShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APIshop.BEShop.entity.Order;
import com.APIshop.BEShop.enums.OrderStatus;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {

    Page<Order> findAllByUserId(String userId, Pageable pageDetails);

    Page<Order> findAllByUserIdAndOrderStatus(String userId, OrderStatus status, Pageable pageDetails);

    Page<Order> findAllByOrderStatus(OrderStatus status, Pageable pageDetails);

}
