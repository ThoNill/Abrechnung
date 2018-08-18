package org.nill.abrechnung.tests.config;

import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IAusgangsDateiRepository;
import org.nill.abrechnung.interfaces.IBuchungsRepository;
import org.nill.abrechnung.interfaces.IMandantRepository;
import org.nill.abrechnung.interfaces.IZahlungsAuftragRepository;
import org.nill.abrechnung.interfaces.IÜberweisungRepository;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.ILeistungRepository;
import org.nill.abrechnung.repositories.ParameterRepository;
import org.nill.abrechnung.tests.flow.TestAbrechnungsKonfigurator;
import org.nill.abrechnung.tests.konten.TestUmgebung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Autowired
    protected IMandantRepository mandantRepository;
    @Autowired
    protected AbrechnungRepository abrechnungRepository;
    @Autowired
    protected IBuchungsRepository buchungRepository;
    @Autowired
    protected IZahlungsAuftragRepository zahlungsAuftragRepository;
    @Autowired
    protected IÜberweisungRepository überweisungRepository;
    @Autowired
    protected ParameterRepository parameterRepository;
    @Autowired
    protected IAusgangsDateiRepository ausgangsDateiRepository;

    @Bean
    public AbrechnungsKonfigurator configurator(
            ILeistungRepository leistungRepository) {
        return new TestAbrechnungsKonfigurator(leistungRepository);
    }

    @Bean
    protected Umgebung umgebung() {
        return new TestUmgebung(mandantRepository,
                abrechnungRepository, buchungRepository,
                zahlungsAuftragRepository, überweisungRepository,
                parameterRepository, ausgangsDateiRepository);
    }

}
