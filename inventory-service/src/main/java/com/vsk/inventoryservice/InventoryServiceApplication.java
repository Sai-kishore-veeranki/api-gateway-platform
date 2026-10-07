package com.vsk.inventoryservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@EnableCaching
public class InventoryServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(InventoryServiceApplication.class);

	public static void main(String[] args) {
		log.info("Starting inventory-service");
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

}
