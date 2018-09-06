package org.nill.abrechnung.tests.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = { org.nill.abrechnung.entities.Leistung.class })
@EnableJpaRepositories(basePackageClasses = { org.nill.abrechnung.repositories.LeistungRepository.class }, transactionManagerRef = "dbATransactionManager" // Name
                                                                                                                                                           // des
)
public class LeistungConfig {
}
