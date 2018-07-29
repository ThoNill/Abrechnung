package tests;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import tests.konten.TestSachKontoProvider;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.�berweisungRepository;

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
    protected ZahlungsDefinitionRepository zahlungsDefinitionRepository;
    @Autowired
    protected �berweisungRepository �berweisungRepository;

    public AbrechnungBasisTest() {
        super();
    }

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
   //     kontoBewegungRepository.deleteAll();
        buchungRepository.deleteAll();
        �berweisungRepository.deleteAll();
        zahlungsAuftragRepository.deleteAll();
        zahlungsDefinitionRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    
    protected SachKontoProvider sachKontoProvider() {
        return new TestSachKontoProvider(mandantRepository,
                abrechnungRepository, buchungRepository,
                zahlungsAuftragRepository,
                zahlungsDefinitionRepository, �berweisungRepository);
    }

}