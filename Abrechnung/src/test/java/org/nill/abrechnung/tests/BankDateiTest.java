package org.nill.abrechnung.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.entities.Überweisung;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.IÜberweisung;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.allgemein.values.MonatJahr;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.zahlungen.values.BIC;
import org.nill.zahlungen.values.BankVerbindung;
import org.nill.zahlungen.values.IBAN;
import org.nill.zahlungen.vorlagen.BankExportModell;
import org.nill.zahlungen.vorlagen.BankExportVorlage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.validation.XmlValidatorFactory;
import org.xml.sax.SAXParseException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class BankDateiTest extends AbrechnungBasisTest {
    protected static final Logger log = LoggerFactory
            .getLogger(BankDateiTest.class);

    @Value("classpath:pain.001.003.03.xsd")
    Resource überweisungsXmlSchema;

    public IMandant erzeugeMandant() {
        Mandant mandant = mandantRepository.save(new Mandant());
        return addZahlungsDefinition(addZahlungsDefinition(mandant, 0.3), 0.7);
    }

    private Mandant addZahlungsDefinition(Mandant mandant, double prozentSatz) {
        ZahlungsDefinition d = new ZahlungsDefinition();
        d.setBuchungsart(1);
        d.setBank(new BankVerbindung(new IBAN("DE02120300000000202051"),
                new BIC("BYLADEM1001")));
        d.setProzentSatz(prozentSatz);
        d.setTag(1);
        mandant.addZahlungsDefinitionen(d);
        return mandantRepository.save(mandant);
    }

    public IAbrechnung erzeugeAbrechnung(Mandant mandant) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setNummer(3);
        abrechnung.setMj(new MonatJahr(4, 2018));
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        abrechnung = abrechnungRepository.save(abrechnung);
        abrechnung.setIMandant(mandant);
        abrechnung = abrechnungRepository.save(abrechnung);
        mandant.addAbrechnung(abrechnung);
        mandantRepository.save(mandant);
        return abrechnungRepository.save(abrechnung);
    }

    @Test
    public void aufträgeErzeugen() {
        List<IÜberweisung> überweisungen = new ArrayList<>();
        createÜberweisung(überweisungen, 1.2, 1);
        createÜberweisung(überweisungen, 2.2, 2);
        createÜberweisung(überweisungen, 3.2, 3);
        BankExportModell model = new BankExportModell(123, "Test Name",
                überweisungen);

        BankExportVorlage<BankExportModell> vorlage = new BankExportVorlage<>(
                "pain.001.003.03", ".", Charset.defaultCharset(), model);
        try {
            XmlValidator validator = XmlValidatorFactory.createValidator(
                    überweisungsXmlSchema, XmlValidatorFactory.SCHEMA_W3C_XML);
            String dateiName = vorlage.erzeugeAusgabe();
            Source source = new StreamSource(new File(dateiName));
            SAXParseException[] r = validator.validate(source);
            assertTrue(r == null || r.length == 0);
        } catch (Exception e) {
            log.error("Unerwartete Ausnahme", e);
            fail(e.getMessage());
        }

    }

    private void createÜberweisung(List<IÜberweisung> überweisungen,
            double betrag, int nummer) {
        Überweisung ü = new Überweisung();
        ü.setVon(new BankVerbindung(new IBAN("DE02500105170137075030"),
                new BIC("INGDDEFF")));
        ü.setAn(new BankVerbindung(new IBAN("DE02300209000106531065"), new BIC(
                "CMCIDEDD")));
        ü.setBetrag(Geld.createAmount(betrag));
        ü.setVerwendungszweck("V " + nummer);
        ü.setAusbezahlt(new Date());
        überweisungen.add(überweisungRepository.save(ü));
    }

}
