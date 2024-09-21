package com.shopping.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ShoppingPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingPortalApplication.class, args);
		System.out.println(
				"**********************************************  Bosa Shopping Portal is Running Successfully ...........  **********************************************");
	}
}
