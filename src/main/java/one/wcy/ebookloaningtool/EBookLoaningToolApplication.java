/**
 * Main application class for the E-Book Loaning Tool.
 * This class serves as the entry point for the Spring Boot application.
 * It enables scheduling capabilities for automated tasks.
 */
package one.wcy.ebookloaningtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EBookLoaningToolApplication {

    /**
     * Main method that starts the Spring Boot application.
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(EBookLoaningToolApplication.class, args);
    }

}
