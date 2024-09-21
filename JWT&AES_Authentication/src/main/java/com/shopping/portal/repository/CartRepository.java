package com.shopping.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.portal.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}
