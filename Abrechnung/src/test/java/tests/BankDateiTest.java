package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.validation.XmlValidatorFactory;
import org.xml.sax.SAXParseException;

import betrag.Geld;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.abrechnung.entities.Überweisung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;
import boundingContext.zahlungen.vorlagen.BankExportModell;
import boundingContext.zahlungen.vorlagen.BankExportVorlage;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class BankDateiTest {
    protected static final Logger log = LoggerFactory.getLogger(BankDateiTest.class);
    
    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;


    @Autowired
    private ZahlungsAuftragRepository zahlungsAuftragRepository;

    @Autowired
    private ÜberweisungRepository überweisungRepository;

    @Autowired
    private BuchungRepository buchungRepository;

    @Value("classpath:pain.001.003.03.xsd")
    Resource überweisungsXmlSchema;

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        buchungRepository.deleteAll();
        überweisungRepository.deleteAll();
        zahlungsAuftragRepository.deleteAll();
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
        d.setBank(new BankVerbindung(new IBAN("DE02120300000000202051"),
                new BIC("BYLADEM1001")));
        d.setProzentSatz(prozentSatz);
        d.setTag(1);
        d.setMandant(mandant);
        mandant.addZahlungsDefinitionen(d);
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

    @Test
    public void aufträgeErzeugen() {
        List<Überweisung> überweisungen = new ArrayList<>();
        createÜberweisung(überweisungen, 1.2, 1);
        createÜberweisung(überweisungen, 2.2, 2);
        createÜberweisung(überweisungen, 3.2, 3);
        BankExportModell model = new BankExportModell(123, "Test Name", überweisungen);

        BankExportVorlage<BankExportModell> vorlage = new BankExportVorlage<>("pain.001.003.03", ".",
                Charset.defaultCharset(), model);
        try {
            XmlValidator validator = XmlValidatorFactory.createValidator(
                    überweisungsXmlSchema, XmlValidatorFactory.SCHEMA_W3C_XML);
            String dateiName = vorlage.erzeugeAusgabe();
            Source source = new StreamSource(new File(dateiName));
            SAXParseException[] r = validator.validate(source);
            assertTrue(r == null || r.length == 0);
        } catch (Exception e) {
            log.error("Unerwartete Ausnahme",e);
            fail(e.getMessage());
        }

    }

    private void createÜberweisung(List<Überweisung> überweisungen,
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
