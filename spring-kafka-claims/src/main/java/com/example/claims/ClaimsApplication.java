package com.example.claims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Claims Spring Boot application.
 */
@SpringBootApplication
public class ClaimsApplication {

    /**
     * Public no-argument constructor required by Spring Boot and Checkstyle.
     */
    public ClaimsApplication() {
        // Default constructor
    }

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(ClaimsApplication.class, args);
    }
}
