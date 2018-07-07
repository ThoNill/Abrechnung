package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import betrag.Geld;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.Überweisung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.AusgangsDateiRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;
import boundingContext.zahlungen.helper.ÜberweisungenManager;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;
import boundingContext.zahlungen.values.TypeReference;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class ÜbereisungenErzeugenUndVersendenTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Autowired
    private ZahlungsDefinitionRepository zahlungsDefinitionRepository;

    @Autowired
    private ZahlungsAuftragRepository zahlungsAuftragRepository;

    @Autowired
    private ÜberweisungRepository überweisungRepository;

    @Autowired
    private AusgangsDateiRepository ausgangsDateiRepository;

    @Autowired
    private BuchungRepository buchungRepository;

    @Autowired
    private KontoBewegungRepository kontoBewegungRepository;

    @Value("classpath:pain.001.003.03.xsd")
    Resource überweisungsXmlSchema;

    @Before
    @Transactional("dbATransactionManager")
    public void clear() {
        kontoBewegungRepository.deleteAll();
        buchungRepository.deleteAll();
        überweisungRepository.deleteAll();
        ausgangsDateiRepository.deleteAll();
        zahlungsAuftragRepository.deleteAll();
        zahlungsDefinitionRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        return mandantRepository.save(new Mandant());
    }

    @Test
    public void aufträgeErzeugen() {
        Mandant mandant = erzeugeMandant();

        createÜberweisung("DE02500105170137075030", 5, mandant);
        createÜberweisung("DE02200505501015871393", 20, mandant);
   
        assertEquals(25,überweisungRepository.count());
        
        try {
            ÜberweisungenManager manager = new ÜberweisungenManager(
                    ausgangsDateiRepository, überweisungRepository, ".",
                    "Test", 1, new TypeReference(1, 1L), 2);
            manager.markiereÜberweisungsDateien(10);
            manager.dateienMarkierenUndErstellen();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(3,ausgangsDateiRepository.count());

    }

    private void createÜberweisung(String von, double betrag, int nummer,
            Mandant mandant) {
        Überweisung ü = new Überweisung();

        ü.setVon(new BankVerbindung(new IBAN(von), new BIC("INGDDEFF")));
        ü.setAn(new BankVerbindung(new IBAN("DE02300209000106531065"), new BIC(
                "CMCIDEDD")));
        ü.setBetrag(Geld.createAmount(betrag));
        ü.setVerwendungszweck("V " + nummer);
    //    ü.setAusbezahlt(new Date());
        ü = überweisungRepository.save(ü);
        ü.setMandant(mandant);
    }


    private void createÜberweisung(String von, int anz,
            Mandant mandant) {
        for(int i = 0; i < anz;i++) {
                createÜberweisung(von, 1.2 * i, i, mandant);
        }

    }

}
