package tests.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tests.flow.TestAbrechnungsKonfigurator;
import tests.konten.TestSachKontoProvider;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.flow.handler.AbrechnungsKonfigurator;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.LeistungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.�berweisungRepository;

@Configuration
public class TestConfig {

    @Autowired
    protected MandantRepository mandantRepository;
    @Autowired
    protected AbrechnungRepository abrechnungRepository;
    @Autowired
    protected BuchungRepository buchungRepository;
    @Autowired
    protected ZahlungsAuftragRepository zahlungsAuftragRepository;
    @Autowired
    protected �berweisungRepository �berweisungRepository;

    @Bean
    public AbrechnungsKonfigurator configurator(
            LeistungRepository leistungRepository) {
        return new TestAbrechnungsKonfigurator(leistungRepository);
    }


    @Bean
    protected SachKontoProvider sachKontoProvider() {
        return new TestSachKontoProvider(mandantRepository,
                abrechnungRepository, buchungRepository,
                zahlungsAuftragRepository,
                �berweisungRepository);
    }


}
