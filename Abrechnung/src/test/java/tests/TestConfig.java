package tests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tests.flow.TestAbrechnungsKonfigurator;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.flow.AbrechnungsKonfigurator;
import boundingContext.abrechnung.repositories.LeistungRepository;

@Configuration
public class TestConfig {

    
    @Bean
    public AbrechnungsKonfigurator configurator(SachKontoProvider sachKontoProvider,LeistungRepository leistungRepository) {
        return new TestAbrechnungsKonfigurator(sachKontoProvider,
                leistungRepository);
    }

    @Bean
    public SachKontoProvider sachKontoProvider() {
        return new TestSachKontoProvider();
    }
}
