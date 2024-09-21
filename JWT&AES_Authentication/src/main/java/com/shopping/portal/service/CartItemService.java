package com.shopping.portal.service;

import java.util.List;

import com.shopping.portal.model.Cart;
import com.shopping.portal.model.CartItem;

public interface CartItemService {
		
	public Cart addProductTocart(Long userid , Long productid , CartItem cartItem);
	
	public Cart removeItemFromCart(Long userid, Long productid);

	public Cart removeAllProducts(Long userid);

	public List<Object> getAllCartItems(Long userid);
}
