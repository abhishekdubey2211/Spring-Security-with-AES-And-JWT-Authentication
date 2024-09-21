package com.shopping.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.portal.model.Order;
import com.shopping.portal.model.Product;
import com.shopping.portal.model.ResponseApi;
import com.shopping.portal.service.OrderService;

@RestController
@RequestMapping("/api/v1")
public class OrderComtroller {

	@Autowired
	OrderService orderService;

	@PostMapping("/order/{userid}")
	public ResponseEntity<?> pushProduct(@PathVariable("userid") Long userid, @RequestBody Order order) {
		List<Object> orderList = new ArrayList<>();
		Order savedOrder = orderService.placeOrder(userid, order);
		orderList.add(savedOrder);
		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseApi.createResponse(1,
				"Order placed Successfully with orderid " + savedOrder.getId(), orderList));
	}

}
