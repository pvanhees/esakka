package io.esakka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by pieter on 12/8/16.
 */
@SpringBootApplication
public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting app");
        SpringApplication.run(Application.class, args);
        logger.info("be.ordina.esakka.Application started");
    }
}
