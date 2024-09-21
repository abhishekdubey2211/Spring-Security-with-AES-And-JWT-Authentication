package com.shopping.portal.service.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shopping.portal.dto.EndUserDTO;
import com.shopping.portal.exceptions.ResourceNotFoundException;
import com.shopping.portal.model.EndUser;
import com.shopping.portal.model.Product;
import com.shopping.portal.model.ProductImages;
import com.shopping.portal.redis.RedisUtil;
import com.shopping.portal.service.ProductService;

import jakarta.transaction.Transactional;

import com.shopping.portal.repository.ImageRepository;
import com.shopping.portal.repository.ProductRepository;

@Service
public class ProductServiceInplementation implements ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceInplementation.class);

	@Autowired
	ProductRepository productRepository;

	@Autowired
	RedisUtil redis = new RedisUtil();

	@Autowired
	ImageRepository imageRepository;
	@Override
	public Product addProduct(Product pushProduct) {
	    try {
	        // Initialize new Product object and set its fields from pushProduct
	        Product product = new Product();
	        product.setBrand(pushProduct.getBrand());
	        product.setCategory(pushProduct.getCategory());
	        product.setDescription(pushProduct.getDescription());
	        product.setPrice(pushProduct.getPrice());
	        product.setName(pushProduct.getName());
	        product.setActive(1);
	        product.setIsdelete(0);
	        product.setSpecification(pushProduct.getSpecification());
	        product.setInstock(pushProduct.getQuantity());

	        // Set product status based on stock availability
	        if (product.getInstock() > 0) {
	            product.setProductstatus("IN-STOCK");
	        } else {
	            product.setProductstatus("OUT-OF-STOCK"); // Corrected spelling
	        }

	        product.setLast_recieved_date(UserServiceImplementation.sf.format(new Date()));

	        Product savedProduct = productRepository.save(product);

	        List<ProductImages> imagelist = pushProduct.getImages().stream()
	            .peek(image -> image.setProduct(savedProduct))  // Set product for each image
	            .collect(Collectors.toList());

	        imageRepository.saveAll(imagelist); 
	        savedProduct.setImages(imagelist);  // Ensure images are set in the product
	        return savedProduct;
	    } catch (Exception e) {
	        logger.error("Unexpected error occurred while adding product", e);
	        throw new RuntimeException("Failed to add product", e);  // Improved error feedback
	    }
	}

	@Override
	@Transactional
	public Product updateProduct(Product product) {
	    try {
	        // Fetch the existing product from the database by its ID
	        Product existingProduct = getSingleProductById(product.getId());

	        // Calculate the updated stock quantity
	        int updatedStockQuantity = existingProduct.getInstock() + product.getQuantity();

	        // Update basic product details
	        existingProduct.setName(product.getName());
	        existingProduct.setBrand(product.getBrand());
	        existingProduct.setCategory(product.getCategory());
	        existingProduct.setDescription(product.getDescription());
	        existingProduct.setSpecification(product.getSpecification());

	        existingProduct.getImages().removeAll(existingProduct.getImages());
	        if (product.getImages() != null && !product.getImages().isEmpty()) {
	            List<ProductImages> updatedImages = product.getImages().stream()
	                .peek(image -> image.setProduct(existingProduct))  // Set the product for each image
	                .collect(Collectors.toList());
	            imageRepository.saveAll(updatedImages); 
	            existingProduct.setImages(updatedImages);
	        }

	        // Set the updated stock quantity
	        existingProduct.setQuantity(product.getQuantity());
	        existingProduct.setInstock(updatedStockQuantity);

	        // Update product status based on stock availability
	        if (existingProduct.getInstock() > 0) {
	            existingProduct.setProductstatus("IN-STOCK");
	        } else {
	            existingProduct.setProductstatus("OUT-OF-STOCK");  // Fixed typo
	        }

	        // Set the last received date
	        existingProduct.setLast_recieved_date(UserServiceImplementation.sf.format(new Date()));

	        // Save the updated product
	        Product savedProduct = productRepository.save(existingProduct);

	        return savedProduct;
	    } catch (Exception e) {
	        logger.error("Unexpected error occurred while updating the product", e);
	        throw new RuntimeException("Failed to update product", e);  // Specific exception handling
	    }
	}

	@Override
	public Product getProductById(Long id) {
		try {
			return getSingleProductById(id);
		} catch (Exception e) {
			logger.error("Unexpected error occured while getProductById ", e);
			throw e;
		}
	}

	@Override
	public List<Object> getProductByType(String type) {
		try {
			List<Product> products = productRepository.findAll();
			List<Object> activeProducts = products.stream()
					.filter(product -> product.getCategory().equalsIgnoreCase(type) && product.getQuantity() > 0
							&& product.getIsdelete() != 1)
					.collect(Collectors.toList());
			return activeProducts;
		} catch (Exception e) {
			logger.error("Unexpected error occurred while getting products by type", e);
			throw e;
		}
	}

	@Override
	public List<Object> getAllProducts() {
		try {
			List<Product> products = new ArrayList<>();
			products = productRepository.findAll();
			List<Object> productList = products.stream().filter(p -> p.getIsdelete() == 0 && p.getActive() == 1)
					.collect(Collectors.toList());
			return productList;
		} catch (Exception e) {
			logger.error("Unexpected error occured while getAllProducts ", e);
			throw e;
		}
	}

	@Override
	public List<Object> getAllInStocksProduct() {
		try {
			List<Product> products = new ArrayList<>();

			products = productRepository.findAll().stream()
					.filter(p -> p.getIsdelete() == 0 || p.getProductstatus().equalsIgnoreCase("IN-STOCK"))
					.collect(Collectors.toList());
			List<Object> productList = products.stream()
					.filter(p -> p.getIsdelete() == 0 && p.getActive() == 1 && p.getQuantity() > 0)
					.collect(Collectors.toList());
			return productList;
		} catch (Exception e) {
			logger.error("Unexpected error occured while getAllProducts ", e);
			throw e;
		}
	}

	@Override
	public List<Object> getAllOutOfStockProducts() {
		try {
			List<Object> productList = productRepository.findAll().stream().filter(p -> p.getQuantity() <= 0
					&& p.getIsdelete() == 0 && p.getProductstatus().equalsIgnoreCase("OUTOFF-STOCK"))
					.collect(Collectors.toList());
			return productList;
		} catch (Exception e) {
			logger.error("Unexpected error occured while getAllProducts ", e);
			throw e;
		}
	}

	@Override
	public List<Object> disableProduct(Long id) {
		try {
			Product retrivedProduct = getSingleProductById(id);
			retrivedProduct.setActive(0);
			retrivedProduct.setIsdelete(1);
			retrivedProduct.setLast_recieved_date(UserServiceImplementation.sf.format(new Date()));
			productRepository.save(retrivedProduct);
			return List.of("Poduct with ProductId " + id + " is disable done Sucessfully......");
		} catch (Exception e) {
			logger.error("Unexpected error occured while getAllProducts ", e);
			throw e;
		}
	}
//*******************************************************Helper Methods *******************************************************************************

	public Product getSingleProductById(Long id) {
		try {
			Product product = new Product();
			product = productRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Product with ProductId " + id + " not found"));
			if (product.getIsdelete() == 1) {
				throw new ResourceNotFoundException("Product with ProductId " + id + " not found");
			}
			return product;
		} catch (Exception e) {
			logger.error("Unexpected error occured while getSingleProductById ", e);
			throw e;
		}
	}

	public List<Product> getProductsByCategoryname(String categoryName) {
		try {
			Optional<List<Product>> productList;
			productList = productRepository.findByCategory(categoryName);
			if (productList.isEmpty()) {
				throw new ResourceNotFoundException("Product with Category " + categoryName + " not found");
			}
			return productList.get();
		} catch (Exception e) {
			logger.error("Unexpected error occured while getProductsByCategoryname ", e);
			throw e;
		}
	}

}
