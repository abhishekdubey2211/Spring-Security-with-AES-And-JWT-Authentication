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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.portal.dto.EndUserDTO;
import com.shopping.portal.model.Product;
import com.shopping.portal.model.ResponseApi;
import com.shopping.portal.service.ProductService;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

	@Autowired
	ProductService productService;

	@PostMapping("/product")
	public ResponseEntity<?> pushProduct(@RequestBody Product product) {
		List<Object> productList = new ArrayList<>();
		Product savedProduct = productService.addProduct(product);
		productList.add(savedProduct);
		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseApi.createResponse(1,
				"Product Added Sucessfully with ProductId " + savedProduct.getId(), productList));
	}

	@PutMapping("/product")
	public ResponseEntity<?> putProduct(@RequestBody Product product) {
		List<Object> productList = new ArrayList<>();
		Product savedProduct = productService.updateProduct(product);
		productList.add(savedProduct);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseApi.createResponse(1,
				"Product Updated Sucessfully with ProductId " + savedProduct.getId(), productList));
	}

	@GetMapping("/product/get_products")
	public ResponseEntity<?> getAllProduct() {
		List<Object> productList = new ArrayList<>();
		productList = productService.getAllProducts();
		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseApi.createResponse(1, "All Products Retrived Successfully....", productList));
	}

	@GetMapping("/product/get_products/in_stock")
	public ResponseEntity<?> getAllInStocksProduct() {
		List<Object> productList = new ArrayList<>();
		productList = productService.getAllInStocksProduct();
		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseApi.createResponse(1, "All In Stock Products Retrived Successfully....", productList));
	}

	@GetMapping("/product/get_products/outoff_stock")
	public ResponseEntity<?> getAllOutOffStocksProduct() {
		List<Object> productList = new ArrayList<>();
		productList = productService.getAllOutOfStockProducts();
		return ResponseEntity.status(HttpStatus.OK).body(
				ResponseApi.createResponse(1, "All Out off Socks Products Retrived Successfully....", productList));
	}

	@GetMapping("/product/get_products/{categoryname}")
	public ResponseEntity<?> getProductsByCategoryname(@PathVariable("categoryname") String categoryname) {
		List<Object> productList = new ArrayList<>();
		productList = productService.getProductByType(categoryname);
		return ResponseEntity.status(HttpStatus.OK).body(ResponseApi.createResponse(1,
				"All " + categoryname + " Products Retrived Successfully....", productList));
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<?> disaleProduct(@PathVariable("productId") Long productId) {
		List<Object> productList = new ArrayList<>();
		productList = productService.disableProduct(productId);
		return ResponseEntity.status(HttpStatus.OK).body(
				ResponseApi.createResponse(1, "Product " + productId + " disable done Successfully....", productList));
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
		List<Object> productList = new ArrayList<>();
		Product product = productService.getProductById(productId);
		productList.add(product);
		return ResponseEntity.status(HttpStatus.OK).body(
				ResponseApi.createResponse(1, "Product " + productId + "disable done Successfully....", productList));
	}

}
