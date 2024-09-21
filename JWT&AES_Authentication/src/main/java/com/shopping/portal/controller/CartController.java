package com.shopping.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.portal.dto.EndUserDTO;
import com.shopping.portal.model.Cart;
import com.shopping.portal.model.CartItem;
import com.shopping.portal.model.ResponseApi;
import com.shopping.portal.service.CartItemService;

@RestController
@RequestMapping("/api/v1")
public class CartController {

	@Autowired
	CartItemService cartItemService;

	@PostMapping("/cart/{userid}/{productid}")
	public ResponseEntity<?> addProductTocart(@RequestBody CartItem cartItem, @PathVariable("userid") Long userid,
			@PathVariable("productid") Long productid) throws Exception {
		List<Object> cartList = new ArrayList<>();
		Cart savedCart = cartItemService.addProductTocart(userid,productid,cartItem);
		cartList.add(savedCart);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseApi.createResponse(1, "Product is added to your cart", cartList));
	}
	
	@DeleteMapping("/cart/{userid}/{productid}")
	public ResponseEntity<?> removeProductFromcart(@PathVariable("userid") Long userid,
			@PathVariable("productid") Long productid) throws Exception {
		List<Object> cartList = new ArrayList<>();
		Cart savedCart = cartItemService.removeItemFromCart(userid,productid);
		cartList.add(savedCart);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseApi.createResponse(1, "Product is removed from your cart", cartList));
	}
	
	@DeleteMapping("/cart/flushproduct/{userid}")
	public ResponseEntity<?> removeAllProductFromcart(@PathVariable("userid") Long userid) throws Exception {
		List<Object> cartList = new ArrayList<>();
		Cart savedCart = cartItemService.removeAllProducts(userid);
		cartList.add(savedCart);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseApi.createResponse(1, "All Product is removed from your cart", cartList));
	}
	
	@GetMapping("/cart/get_products/{userid}")
	public ResponseEntity<?> getUserCartDetails(@PathVariable("userid") Long userid) throws Exception {
		List<Object> cartList = new ArrayList<>();
		 cartList = cartItemService.getAllCartItems(userid);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseApi.createResponse(1, "Fetched All Products from your cart", cartList));
	}
}
