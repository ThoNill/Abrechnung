package tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import tests.konten.TestSachKonto;
import tests.konten.TestSachKontoProvider;
import betrag.Geld;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsAuftrag;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.�berweisungRepository;
import boundingContext.zahlungen.helper.ZahlungsAuftragsManager;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class ZahlungenTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Autowired
    private ZahlungsDefinitionRepository zahlungsDefinitionRepository;

    @Autowired
    private ZahlungsAuftragRepository zahlungsAuftragRepository;

    @Autowired
    private �berweisungRepository �berweisungRepository;

    @Autowired
    private BuchungRepository buchungRepository;

    @Autowired
    private KontoBewegungRepository kontoBewegungRepository;

    @Before
    @Transactional("dbATransactionManager")
    public void clear() {
        kontoBewegungRepository.deleteAll();
        buchungRepository.deleteAll();
        �berweisungRepository.deleteAll();
        zahlungsAuftragRepository.deleteAll();
        zahlungsDefinitionRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        Mandant mandant = mandantRepository.save(new Mandant());
        return addZahlungsDefinition(addZahlungsDefinition(mandant, 0.3), 0.7);
    }

    private Mandant addZahlungsDefinition(Mandant mandant, double prozentSatz) {
        ZahlungsDefinition d = new ZahlungsDefinition();
        d.setBuchungsart(1);
        d.setBank(new BankVerbindung(new IBAN("123"), new BIC("999")));
        d.setProzentSatz(prozentSatz);
        d.setTag(1);
        d = zahlungsDefinitionRepository.save(d);
        d.setMandant(mandant);
        mandant.addZahlungsDefinitionen(d);
        zahlungsDefinitionRepository.save(d);
        return mandantRepository.save(mandant);
    }

    public Abrechnung erzeugeAbrechnung(Mandant mandant) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setNummer(3);
        abrechnung.setJahr(2018);
        abrechnung.setMonat(4);
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        abrechnung = abrechnungRepository.save(abrechnung);
        abrechnung.setMandant(mandant);
        abrechnung = abrechnungRepository.save(abrechnung);
        mandant.addAbrechnung(abrechnung);
        mandantRepository.save(mandant);
        return abrechnungRepository.save(abrechnung);
    }

    private SachKontoProvider sachKontoProvider() {
        return new TestSachKontoProvider();
    }

    @Test
    public void auftr�geErzeugen() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        SachKontoProvider sachKontoProvider = sachKontoProvider();

        ZahlungsAuftragsManager manager = new ZahlungsAuftragsManager(
                sachKontoProvider, buchungRepository, kontoBewegungRepository,
                zahlungsAuftragRepository, �berweisungRepository,
                TestSachKonto.GUTHABEN, TestSachKonto.AUSBEZAHLT);
        List<ZahlungsAuftrag> auftr�ge = manager.erzeugeAuftr�ge(abrechnung,
                Geld.createAmount(100), "verwendungszweck");
        assertEquals(2, auftr�ge.size());
        assertEquals(Geld.createAmount(30), auftr�ge.get(0).getBetrag());
        assertEquals(Geld.createAmount(70), auftr�ge.get(1).getBetrag());

        assertEquals(2, zahlungsAuftragRepository.count());
    }

    @Test
    public void �berweisungenErzeugen() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        SachKontoProvider sachKontoProvider = sachKontoProvider();

        ZahlungsAuftragsManager manager = new ZahlungsAuftragsManager(
                sachKontoProvider, buchungRepository, kontoBewegungRepository,
                zahlungsAuftragRepository, �berweisungRepository,
                TestSachKonto.GUTHABEN, TestSachKonto.AUSBEZAHLT);
        List<ZahlungsAuftrag> auftr�ge = manager.erzeugeAuftr�ge(abrechnung,
                Geld.createAmount(100), "verwendungszweck");

        manager.erzeuge�berweisungen(mandant, auftr�ge, new BankVerbindung(
                new IBAN("1"), new BIC("9")));
        assertEquals(2, �berweisungRepository.count());
    }

}
