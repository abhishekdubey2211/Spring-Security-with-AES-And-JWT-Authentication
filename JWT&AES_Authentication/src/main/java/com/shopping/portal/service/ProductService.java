package com.shopping.portal.service;

import com.shopping.portal.model.Product;
import java.util.*;

public interface ProductService {

	public Product addProduct(Product product);

	public Product updateProduct(Product product);

	public Product getProductById(Long id);

	public List<Object> getProductByType(String type);

	public List<Object> getAllInStocksProduct();

	public List<Object> getAllOutOfStockProducts();

	public List<Object> getAllProducts();
		
	public List<Object> disableProduct(Long id);
}
