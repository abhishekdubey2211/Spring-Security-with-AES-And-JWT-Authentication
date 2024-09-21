package com.shopping.portal.service.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.portal.exceptions.CustomException;
import com.shopping.portal.model.Cart;
import com.shopping.portal.repository.CartRepository;
import com.shopping.portal.service.CartService;

@Service
public class CartServiceImplementation implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Override
	public Cart getCartById(Long id) {
		Optional<Cart> optionalCart = cartRepository.findById(id);
		if (optionalCart.isEmpty()) {
			throw new CustomException(305, "No Cart Found with CartId " + id, "No Cart Found with CartId");
		}
		return optionalCart.get();
	}
}
