package com.shopping.portal.service.implementations;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.portal.exceptions.ResourceNotFoundException;
import com.shopping.portal.model.Bucket;
import com.shopping.portal.model.EndUser;
import com.shopping.portal.model.Order;
import com.shopping.portal.model.Product;
import com.shopping.portal.repository.EnduserRepository;
import com.shopping.portal.repository.OrderRepository;
import com.shopping.portal.repository.ProductRepository;
import com.shopping.portal.service.OrderService;

@Service
public class OrderServiceImplementation implements OrderService {

	@Autowired
	private EnduserRepository enduserRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	ProductRepository productRepository;

	@Override
	public Order placeOrder(Long userid, Order order) {
		EndUser user = enduserRepository.findById(userid)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with UserId " + userid));
		if (user.getIsdelete() == 1) {
			throw new ResourceNotFoundException("User with UserId " + userid + " is deleted.");
		}
		Double totalOrderPrice = order.getBucket().stream().map(this::calculateBucketItemPrice).reduce(0.0,Double::sum);

		order.setUser(user);
		order.setTotalamount(totalOrderPrice);
		order.setIsdelevered("NO");
		order.setDate(UserServiceImplementation.sf.format(new Date()));
	    order.getBucket().forEach(bucketItem -> bucketItem.setOrder(order));		
		return orderRepository.save(order);
	}

	private Double calculateBucketItemPrice(Bucket bucketItem) {
	    Long productId = bucketItem.getProductid();

	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ProductId " + productId));

	    if (product.getInstock() != 0) {
	        if (product.getInstock() < bucketItem.getQuantity()) {
	            throw new ResourceNotFoundException(
	                    "For " + product.getName() + " you can order only " + product.getInstock() + " items");
	        }
	    } else {
	        throw new ResourceNotFoundException("Product " + product.getName() + " is OUT OF STOCK");
	    }
	    product.setInstock(product.getInstock() - bucketItem.getQuantity());
	    if(product.getInstock() == 0) {
	    	product.setProductstatus("OUTOFF-STOCK");
	    }
	    
	    product.setQuantity(product.getInstock());
	    productRepository.save(product);
	    double itemPrice = product.getPrice() * bucketItem.getQuantity();
	    bucketItem.setPrice(itemPrice); 
	    bucketItem.setProduct(product);
	    return itemPrice;
	}
}
