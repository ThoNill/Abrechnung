package tests;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import tests.konten.TestSachKontoProvider;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ParameterRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;

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

    
    public AbrechnungBasisTest() {
        super();
    }

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        buchungRepository.deleteAll();
        überweisungRepository.deleteAll();
        zahlungsAuftragRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
        parameterRepository.deleteAll();
    }

    
    protected SachKontoProvider sachKontoProvider() {
        return new TestSachKontoProvider(mandantRepository,
                abrechnungRepository, buchungRepository,
                zahlungsAuftragRepository,
                überweisungRepository,parameterRepository);
    }

}