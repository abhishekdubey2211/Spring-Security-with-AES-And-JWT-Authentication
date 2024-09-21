package com.shopping.portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopping.portal.model.CartItem;
import com.shopping.portal.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	@Query(value = "select cart_id FROM user WHERE id = ? and isdelete = 0", nativeQuery = true)
	Optional<Long> findCartidByUserid(Long id);
	
	void deleteByProduct(Product product);
}
