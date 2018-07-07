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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.validation.XmlValidatorFactory;
import org.xml.sax.SAXParseException;

import tests.konten.TestSachKontoProvider;
import betrag.Geld;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.abrechnung.entities.�berweisung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.�berweisungRepository;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;
import boundingContext.zahlungen.vorlagen.STModel;
import boundingContext.zahlungen.vorlagen.STVorlage;


@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class BankDateiTest {

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

    @Value("classpath:pain.001.003.03.xsd")
    Resource �berweisungsXmlSchema;
    
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
        Mandant mandant =  mandantRepository.save(new Mandant());
        return addZahlungsDefinition(addZahlungsDefinition(mandant, 0.3),0.7);
    }

    private Mandant addZahlungsDefinition(Mandant mandant, double prozentSatz) {
        ZahlungsDefinition d = new ZahlungsDefinition();
        d.setBuchungsart(1);
        d.setBank(new BankVerbindung(new IBAN("DE02120300000000202051"),new BIC("BYLADEM1001")));
        d.setProzentSatz(prozentSatz);
        d.setTag(1);
        d  = zahlungsDefinitionRepository.save(d);
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
        List<�berweisung> �berweisungen = new ArrayList<>();
        create�berweisung(�berweisungen,1.2,1);
        create�berweisung(�berweisungen,2.2,2);
        create�berweisung(�berweisungen,3.2,3);
        STModel model = new STModel(123,"Test Name", �berweisungen);
        
        STVorlage<STModel> vorlage = new STVorlage<>("pain.001.003.03",".",Charset.defaultCharset(),model);
        try {
            XmlValidator validator = XmlValidatorFactory.createValidator(�berweisungsXmlSchema,XmlValidatorFactory.SCHEMA_W3C_XML);
            String dateiName = vorlage.erzeugeAusgabe();
            Source source = new StreamSource(new File(dateiName));
            SAXParseException[]  r = validator.validate(source);
            assertTrue(r == null || r.length == 0);
        } catch (Exception e) {
           fail(e.getMessage());
        }
        
    }

    private void create�berweisung(List<�berweisung> �berweisungen,double betrag,int nummer) {
        �berweisung � = new �berweisung();
        �.setVon(new BankVerbindung(new IBAN("DE02500105170137075030"), new BIC("INGDDEFF")));
        �.setAn(new BankVerbindung(new IBAN("DE02300209000106531065"), new BIC("CMCIDEDD")));
        �.setBetrag(Geld.createAmount(betrag));
        �.setVerwendungszweck("V "+ nummer);
        �.setAusbezahlt(new Date());
        �berweisungen.add(�berweisungRepository.save(�));
    }
        
}
