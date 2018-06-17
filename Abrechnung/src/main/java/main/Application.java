package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
// simple SpringBoot application
@EnableIntegration
@IntegrationComponentScan(basePackages = { "org.batch" })
@ComponentScan(basePackages = { "tests.db" })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}