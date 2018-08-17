package org.nill.abrechnung.tests.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EntityScan(basePackageClasses = {
        org.nill.abrechnung.entities.Leistung.class})
@EnableJpaRepositories(basePackageClasses = { 
        org.nill.abrechnung.repositories.LeistungRepository.class}
, transactionManagerRef = "dbATransactionManager" // Name des
)
public class LeistungConfig {
}
