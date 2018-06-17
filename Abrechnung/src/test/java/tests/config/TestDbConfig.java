package tests.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

// Eine der Datenbankkonfigurationen muss eine Primary Annotation haben
@Configuration
@EnableJpaRepositories(basePackageClasses = boundingContext.abrechnung.repositories.BuchungRepository.class // eine
                                                                                     // Klasse
                                                                                     // aus
                                                                                     // dem
                                                                                     // Package
                                                                                     // mit
                                                                                     // den
                                                                                     // Repositories
// , entityManagerFactoryRef = "entityManagerFactory" braucht man hier nicht
, transactionManagerRef = "dbATransactionManager" // Name des
                                                  // TransactionManagers,
                                                  // Transactional Annotationen
                                                  // verweisen dann darauf
)
public class TestDbConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.aurl}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddl;

    @Value("${hibernate.dialect}")
    private String dialect;

    // @Bean("dbADatasource") muss nicht sein, aber kann eventuell für
    // JdbcTemplate verwendet werde
    // @Primary
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean("entityManagerFactory")
    // muss dieser Name sein, da Default
    @Primary
    public LocalContainerEntityManagerFactoryBean dbAEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { boundingContext.abrechnung.entities.Abrechnung.class
                .getPackage().getName() }); // Hier steht der Packagename zu den
                                            // Entities
        /*
         * em.setPersistenceUnitName("dbA");
         * 
         * Muss nicht sein, braucht man aber, wenn man einen EntityManager
         * injecten will
         * 
         * @PersistenceContext(unitName = "dbA") private EntityManager
         * entityManager;
         */

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", hbm2ddl);
        properties.put("hibernate.dialect", dialect);
        em.setJpaPropertyMap(properties);

        return em;
    }

    // Name des TransactionManagers, Transactional Annotationen verweisen dann
    // darau
    // @Bean("dbATransactionManager") braucht man nicht , der Name oben reicht
    @Bean
    @Primary
    public PlatformTransactionManager dbATransactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dbAEntityManager()
                .getObject());
        return transactionManager;
    }
}
