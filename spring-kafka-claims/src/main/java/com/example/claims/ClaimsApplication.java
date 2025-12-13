
package com.example.claims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Claims Spring Boot application.
 * <p>
 * This is a utility class and cannot be instantiated.
 */
@SpringBootApplication
public final class ClaimsApplication {
    /**
     * Private constructor to prevent instantiation.
     */
    private ClaimsApplication() {
        throw new UnsupportedOperationException("Utility class");
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
