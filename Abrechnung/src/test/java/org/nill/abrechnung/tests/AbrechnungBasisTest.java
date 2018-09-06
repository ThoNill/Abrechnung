package org.nill.abrechnung.tests;

import org.junit.After;
import org.junit.Before;
import org.nill.abrechnung.aufzählungen.ParameterKey;
import org.nill.abrechnung.entities.Parameter;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ParameterRepository;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
import org.nill.abrechnung.repositories.ÜberweisungRepository;
import org.nill.abrechnung.tests.konten.TestUmgebung;
import org.nill.allgemein.values.MonatJahr;
import org.nill.allgemein.values.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

@EntityScan(basePackageClasses = { org.nill.abrechnung.entities.Leistung.class,
        org.nill.abrechnung.entities.Abrechnung.class })
@EnableJpaRepositories(basePackageClasses = {
        org.nill.abrechnung.repositories.BuchungRepository.class,
        org.nill.abrechnung.repositories.LeistungRepository.class }, transactionManagerRef = "dbATransactionManager" // Name
                                                                                                                     // des
)
public class AbrechnungBasisTest {

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

    public AbrechnungBasisTest() {
        super();
    }

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        buchungRepository.deleteAll();
        überweisungRepository.deleteAll();
        ausgangsDateiRepository.deleteAll();
        zahlungsAuftragRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
        parameterRepository.deleteAll();
    }

    protected Umgebung umgebung() {
        return new TestUmgebung(mandantRepository, abrechnungRepository,
                buchungRepository, zahlungsAuftragRepository,
                überweisungRepository, parameterRepository,
                ausgangsDateiRepository);
    }

    protected void fülleParameter(String tage) {
        Umgebung provider = umgebung();

        neuerParameter(provider, ParameterKey.ZINS_ÜBERZAHLUNGEN, "0.06");
        neuerParameter(provider, ParameterKey.MWST_GANZ, "0.19");
        neuerParameter(provider, ParameterKey.MWST_HALB, "0.07");
        neuerParameter(provider, ParameterKey.ÜBERZAHLUNGSTAGE, tage);
    }

    private void neuerParameter(Umgebung provider, ParameterKey key, String wert) {
        Parameter p = new Parameter();
        p.setKey(key);
        p.setMj(new MonatJahr(1, 2000));
        p.setRef(TypeReference.ALLE);
        p.setWert(wert);
        provider.getParameterRepository().save(p);
    }

}