package com.shopping.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.portal.model.Order;

public interface OrderRepository  extends JpaRepository<Order,Long>{

}
