package com.shopping.portal.service.implementations;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.shopping.portal.exceptions.ResourceNotFoundException;
import com.shopping.portal.model.Cart;
import com.shopping.portal.model.CartItem;
import com.shopping.portal.model.Product;
import com.shopping.portal.redis.RedisUtil;
import com.shopping.portal.repository.CartItemRepository;
import com.shopping.portal.repository.CartRepository;
import com.shopping.portal.repository.ProductRepository;
import com.shopping.portal.service.CartItemService;
import java.util.*;
import jakarta.transaction.Transactional;

@Service
public class CartItemServiceImplementation implements CartItemService {

	private final CartItemRepository cartItemRepository;

	@Autowired
	public CartItemServiceImplementation(CartItemRepository cartItemRepository) {
		this.cartItemRepository = cartItemRepository;
	}

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	RedisUtil redis = new RedisUtil();

	@Override
	@Transactional
	public Cart addProductTocart(Long userid, Long productid, CartItem cartItem) {

		Long cartid = cartItemRepository.findCartidByUserid(userid)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found for user with id " + userid));

		Cart fetchedCart = cartRepository.findById(cartid)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found with id " + cartid));

		Product fetchedProduct = productRepository.findById(productid)
				.orElseThrow(() -> new ResourceNotFoundException("No product found with id " + productid));

		Optional<CartItem> existingCartItem = fetchedCart.getCartitem().stream()
				.filter(item -> item.getProduct().getId().equals(productid)).findFirst();

		CartItem existingItem;
		if (existingCartItem.isPresent()) {
			existingItem = existingCartItem.get();
			int newQuantity = existingItem.getQuantity() + cartItem.getQuantity();
			existingItem.setQuantity(newQuantity);
			existingItem.setPrice(existingItem.getPrice() + (fetchedProduct.getPrice() * cartItem.getQuantity()));
		} else {
			existingItem = new CartItem();
			existingItem.setProduct(fetchedProduct);
			existingItem.setQuantity(cartItem.getQuantity());
			existingItem.setPrice(fetchedProduct.getPrice() * cartItem.getQuantity());
			existingItem.setCart(fetchedCart);
			fetchedCart.getCartitem().add(existingItem); 
		}
		double newTotalAmount = fetchedCart.getCartitem().stream().mapToDouble(CartItem::getPrice).sum();
		fetchedCart.setTotalamount(newTotalAmount);

		cartItemRepository.save(existingItem);
		cartRepository.save(fetchedCart); 
		return fetchedCart;
	}

	@Override
	@Transactional
	public Cart removeItemFromCart(Long userid, Long productid) {
		Long cartid = cartItemRepository.findCartidByUserid(userid)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found for user with id " + userid));

		Cart fetchedCart = cartRepository.findById(cartid)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found with id " + cartid));

		Product fetchedProduct = productRepository.findById(productid)
				.orElseThrow(() -> new ResourceNotFoundException("No product found with id " + productid));

		// Check if the product is already in the cart
		Optional<CartItem> existingCartItem = fetchedCart.getCartitem().stream()
				.filter(item -> item.getProduct().getId().equals(productid)).findFirst();

		if (existingCartItem.isEmpty()) {
			throw new ResourceNotFoundException("No such product exists in the cart");
		}

		// Get the CartItem to remove
		CartItem cartItemToRemove = existingCartItem.get();

		// Remove the CartItem from the cart
		fetchedCart.getCartitem().remove(cartItemToRemove);

		// Recalculate the total amount after removal
		double newTotalAmount = fetchedCart.getCartitem().stream().mapToDouble(CartItem::getPrice).sum();
		fetchedCart.setTotalamount(newTotalAmount);

		// Save the updated cart
		cartRepository.save(fetchedCart);

		return fetchedCart;
	}

	@Override
	@Transactional
	public Cart removeAllProducts(Long userid) {
		Long cartid = cartItemRepository.findCartidByUserid(userid)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found for user with id " + userid));
		Cart fetchedCart = cartRepository.findById(cartid)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found with id " + cartid));
		List<CartItem> cartItems = fetchedCart.getCartitem();
		if (cartItems.isEmpty()) {
			throw new ResourceNotFoundException("No products found in the cart to remove");
		}
		fetchedCart.getCartitem().clear();
		fetchedCart.setTotalamount(0.0);
		cartRepository.save(fetchedCart);
		return fetchedCart;
	}

	@Override
	public List<Object> getAllCartItems(Long userid) {
		Long cartid = cartItemRepository.findCartidByUserid(userid)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found for user with id " + userid));
		Cart fetchedCart = cartRepository.findById(cartid)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found with id " + cartid));
		List<Object> data = new ArrayList<>();
		data.add(fetchedCart.getCartitem());
		return data;
	}

}
