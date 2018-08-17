package org.nill.abrechnung.tests;

import org.junit.After;
import org.junit.Before;
import org.nill.abrechnung.aufzählungen.ParameterKey;
import org.nill.abrechnung.entities.Parameter;
import org.nill.abrechnung.interfaces.SachKontoProvider;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ParameterRepository;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
import org.nill.abrechnung.repositories.ÜberweisungRepository;
import org.nill.abrechnung.tests.konten.TestSachKontoProvider;
import org.nill.allgemein.values.MonatJahr;
import org.nill.allgemein.values.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    protected SachKontoProvider sachKontoProvider() {
        return new TestSachKontoProvider(mandantRepository,
                abrechnungRepository, buchungRepository,
                zahlungsAuftragRepository, überweisungRepository,
                parameterRepository, ausgangsDateiRepository);
    }
    
    protected void fülleParameter(String tage) {
        SachKontoProvider provider = sachKontoProvider();
        
        neuerParameter(provider, ParameterKey.ZINS_ÜBERZAHLUNGEN,"0.06");
        neuerParameter(provider, ParameterKey.MWST_GANZ,"0.19");
        neuerParameter(provider, ParameterKey.MWST_HALB,"0.07");
        neuerParameter(provider, ParameterKey.ÜBERZAHLUNGSTAGE,tage);
    }

    private void neuerParameter(SachKontoProvider provider, ParameterKey key,
            String wert) {
        Parameter p = new Parameter();
        p.setKey(key);
        p.setMj(new MonatJahr(1,2000));
        p.setRef(TypeReference.ALLE);
        p.setWert(wert);
        provider.getParameterRepository().save(p);
    }


}