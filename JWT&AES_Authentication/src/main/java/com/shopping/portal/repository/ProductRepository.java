package com.shopping.portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;
import com.shopping.portal.model.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product__images WHERE product_id = ?", nativeQuery = true)
    void deleteProductImagesByProductId(@Param("product_id") Long productid);
    
    Optional<List<Product>> findByCategory(String categoryname);
    
}
