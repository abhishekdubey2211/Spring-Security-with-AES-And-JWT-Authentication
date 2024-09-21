package com.shopping.portal.service;

import com.shopping.portal.model.Order;

public interface OrderService {
	public Order placeOrder(Long userid, Order order);
}
