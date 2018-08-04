package tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import betrag.Geld;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsAuftrag;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.zahlungen.actions.ZahlungsAufträgeErzeugen;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;
import boundingContext.zahlungen.values.MonatJahr;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class ZahlungenTest extends AbrechnungBasisTest{

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
        d.setMandant(mandant);
        mandant.addZahlungsDefinitionen(d);
        return mandantRepository.save(mandant);
    }

    
    public Abrechnung erzeugeAbrechnung(Mandant mandant) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setNummer(3);
        abrechnung.setMj(new MonatJahr(4,2018));
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        abrechnung.setMandant(mandant);
        mandant.addAbrechnung(abrechnung);
        return abrechnungRepository.save(abrechnung);
    }

 

    @Test
    @Transactional("dbATransactionManager")
    public void aufträgeErzeugen() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        SachKontoProvider sachKontoProvider = sachKontoProvider();

        ZahlungsAufträgeErzeugen manager = new ZahlungsAufträgeErzeugen(
                sachKontoProvider, buchungRepository, 
                zahlungsAuftragRepository, überweisungRepository,abrechnungRepository);
        List<ZahlungsAuftrag> aufträge = manager.erzeugeAufträge(abrechnung,
                Geld.createAmount(100), "verwendungszweck");
        assertEquals(2, aufträge.size());
        assertEquals(Geld.createAmount(30), aufträge.get(0).getBetrag());
        assertEquals(Geld.createAmount(70), aufträge.get(1).getBetrag());

        assertEquals(2, zahlungsAuftragRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void überweisungenErzeugen() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        SachKontoProvider sachKontoProvider = sachKontoProvider();

        ZahlungsAufträgeErzeugen manager = new ZahlungsAufträgeErzeugen(
                sachKontoProvider, buchungRepository, 
                zahlungsAuftragRepository, überweisungRepository,abrechnungRepository);
        List<ZahlungsAuftrag> aufträge = manager.erzeugeAufträge(abrechnung,
                Geld.createAmount(100), "verwendungszweck");

        manager.erzeugeÜberweisungen(aufträge, new BankVerbindung(
                new IBAN("1"), new BIC("9")));
        assertEquals(2, überweisungRepository.count());
    }

}
