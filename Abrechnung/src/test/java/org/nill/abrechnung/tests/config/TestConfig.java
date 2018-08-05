package org.nill.abrechnung.tests.config;

import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.flow.handler.AbrechnungsKonfigurator;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.abrechnung.repositories.LeistungRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ParameterRepository;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
import org.nill.abrechnung.repositories.ÜberweisungRepository;
import org.nill.abrechnung.tests.flow.TestAbrechnungsKonfigurator;
import org.nill.abrechnung.tests.konten.TestSachKontoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    protected ÜberweisungRepository überweisungRepository;
    @Autowired
    protected ParameterRepository parameterRepository;
    @Autowired
    protected AusgangsDateiRepository ausgangsDateiRepository;

    @Bean
    public AbrechnungsKonfigurator configurator(
            LeistungRepository leistungRepository) {
        return new TestAbrechnungsKonfigurator(leistungRepository);
    }

    @Bean
    protected SachKontoProvider sachKontoProvider() {
        return new TestSachKontoProvider(mandantRepository,
                abrechnungRepository, buchungRepository,
                zahlungsAuftragRepository, überweisungRepository,
                parameterRepository, ausgangsDateiRepository);
    }

}
