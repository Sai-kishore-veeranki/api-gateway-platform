package org.vsk.ecomapigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcomApiGatewayApplication {

    private static final Logger log = LoggerFactory.getLogger(EcomApiGatewayApplication.class);

    public static void main(String[] args) {
        log.info("Starting ecom-api-gateway");
        SpringApplication.run(EcomApiGatewayApplication.class, args);
    }

}
