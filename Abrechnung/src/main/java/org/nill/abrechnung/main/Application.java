package org.nill.abrechnung.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@IntegrationComponentScan(basePackages = { "org.nill" })
@ComponentScan(basePackages = { "org.nill" })
@EntityScan(basePackageClasses = {
        org.nill.abrechnung.entities.Abrechnung.class,
        org.nill.abrechnung.entities.ZahlungsAuftrag.class,
        org.nill.abrechnung.entities.Überweisung.class,
        org.nill.abrechnung.entities.AusgangsDatei.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
