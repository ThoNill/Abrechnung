package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.IZahlungsAuftrag;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.allgemein.values.MonatJahr;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.zahlungen.actions.ZahlungsAufträgeErzeugen;
import org.nill.zahlungen.values.BIC;
import org.nill.zahlungen.values.BankVerbindung;
import org.nill.zahlungen.values.IBAN;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class ZahlungenTest extends AbrechnungBasisTest {

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
        mandant.addZahlungsDefinitionen(d);
        return mandantRepository.save(mandant);
    }

    public Abrechnung erzeugeAbrechnung(Mandant mandant) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setNummer(3);
        abrechnung.setMj(new MonatJahr(4, 2018));
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        abrechnung.setIMandant(mandant);
        mandant.addAbrechnung(abrechnung);
        return abrechnungRepository.save(abrechnung);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void aufträgeErzeugen() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        Umgebung umgebung = umgebung();

        ZahlungsAufträgeErzeugen manager = new ZahlungsAufträgeErzeugen(
                umgebung);
        List<IZahlungsAuftrag> aufträge = manager.erzeugeAufträge(abrechnung,
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
        Umgebung umgebung = umgebung();

        ZahlungsAufträgeErzeugen manager = new ZahlungsAufträgeErzeugen(
                umgebung);
        List<IZahlungsAuftrag> aufträge = manager.erzeugeAufträge(abrechnung,
                Geld.createAmount(100), "verwendungszweck");

        manager.erzeugeÜberweisungen(aufträge, new BankVerbindung(
                new IBAN("1"), new BIC("9")));
        assertEquals(2, überweisungRepository.count());
    }

}
