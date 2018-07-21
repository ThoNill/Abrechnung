package tests;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Optional;

import lombok.extern.java.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import boundingContext.abrechnung.actions.AbrechnungHelper;
import boundingContext.abrechnung.aufzählungen.AbrechnungsStatus;
import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufzählungen.RunStatus;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.flow.handler.HoleAbrechnung;
import boundingContext.abrechnung.flow.payloads.AbrechnungPayload;
import boundingContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundingContext.abrechnung.flow.payloads.AufrufPayload;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;

@Log
@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class HoleAbrechnungsTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        return new Mandant();
    }

    public Abrechnung erzeugeAbrechnung(Mandant mandant,int nummer) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setMandant(mandant);
        abrechnung.setNummer(nummer);
        abrechnung.setJahr(2018);
        abrechnung.setMonat(4);
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        mandant.addAbrechnung(abrechnung);
        return abrechnungRepository.save(abrechnung);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void holeEineBestimmteAbrechnung() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant,1);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NACHBERCHNEN,mandant.getMandantId(),abrechnung.getAbrechnungId(),4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        
        aufruf(parameter);
        
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
    }

    
    @Test
    @Transactional("dbATransactionManager")
    public void ohneAbrechnungNachberechnenOderErgänzen() {
        Mandant mandant = mandantRepository.save(erzeugeMandant());
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NACHBERCHNEN,mandant.getMandantId(),0,4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        aufrufMitException(parameter);
    
        parameter = new AufrufPayload(AbrechnungsArt.ERGÄNZEN,mandant.getMandantId(),0,4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        aufrufMitException(parameter);
    
        
        assertEquals(1, mandantRepository.count());
        assertEquals(0, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void ohneAbrechnungNachberechnenOderErgänzen2() {
        Mandant mandant = mandantRepository.save(erzeugeMandant());
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NACHBERCHNEN,mandant.getMandantId(),3,4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        aufrufMitException(parameter);
    
        parameter = new AufrufPayload(AbrechnungsArt.ERGÄNZEN,mandant.getMandantId(),3,4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        aufrufMitException(parameter);
    
        
        assertEquals(1, mandantRepository.count());
        assertEquals(0, abrechnungRepository.count());
    }


    @Test
    @Transactional("dbATransactionManager")
    public void ohneAbrechnungNachberechnenOderErgänzen3() {
        Mandant mandant = mandantRepository.save(erzeugeMandant());
        Abrechnung abrechnung = erzeugeAbrechnung(mandant,1);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NACHBERCHNEN,mandant.getMandantId(),abrechnung.getAbrechnungId(),5,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        aufrufMitException(parameter);
    
        parameter = new AufrufPayload(AbrechnungsArt.ERGÄNZEN,mandant.getMandantId(),abrechnung.getAbrechnungId(),5,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        aufrufMitException(parameter);
    
        
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
    }


    
    @Test
    @Transactional("dbATransactionManager")
    public void ohneAbrechnung() {
        Mandant mandant = mandantRepository.save(erzeugeMandant());
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NEU,mandant.getMandantId(),0,4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        AbrechnungPayload testErgebnis = aufruf(parameter);
        
        assertEquals(1, testErgebnis.getAbrechnung().getNummer());
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void rechneLetzteAbrechnungAb() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant,1);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NEU,mandant.getMandantId(),0,4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        
        AbrechnungPayload testErgebnis = aufruf(parameter);
        
        assertEquals(1, testErgebnis.getAbrechnung().getNummer());
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void rechneNeueAbrechnungAb() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant,1);
        abrechnung.setStatus(AbrechnungsStatus.ABGERECHNET);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NEU,mandant.getMandantId(),0,4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        
        AbrechnungPayload testErgebnis = aufruf(parameter);
        
        assertEquals(2, testErgebnis.getAbrechnung().getNummer());
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void rechneNeueAbrechnungAbAusnahme() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant,1);
        abrechnung.setStatus(AbrechnungsStatus.ABGERECHNET);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NEU,mandant.getMandantId(),abrechnung.getAbrechnungId(),4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        
        aufrufMitException(parameter);
        
    }

    
    @Test
    @Transactional("dbATransactionManager")
    public void falscherMandant() {
        Mandant mandant1 = mandantRepository.save(erzeugeMandant());
        Mandant mandant2 = mandantRepository.save(erzeugeMandant());
        Abrechnung abrechnung = erzeugeAbrechnung(mandant2,1);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NACHBERCHNEN,mandant1.getMandantId(),abrechnung.getAbrechnungId(),4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        
        aufrufMitException(parameter);
    }


    @Test
    @Transactional("dbATransactionManager")
    public void keinMandant() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant,1);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NACHBERCHNEN,0,abrechnung.getAbrechnungId(),4,2018,AbrechnungsTyp.MONATSABRECHNUNG);        
        
        
        aufrufMitException(parameter);
    }

    
    private AbrechnungPayload aufruf(AufrufPayload parameter) {
        HoleAbrechnung handler = new HoleAbrechnung(mandantRepository, abrechnungRepository);
        AbrechnungPayload testErgebnis;
        try {
            return handler.testTransformPayload(parameter);
            
        } catch (Exception e) {
          fail("Unerwertete Ausnahme");
          log.severe(e.getMessage());
        }
        return null;
    }
    
    private void aufrufMitException(AufrufPayload parameter) {
        HoleAbrechnung handler = new HoleAbrechnung(mandantRepository, abrechnungRepository);
        
        try {
            handler.testTransformPayload(parameter);
            
            fail("Ausnahme erwartet");
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }

   
}
