package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.allgemein.values.MonatJahr;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class AbrechnungsTest extends AbrechnungBasisTest {

    @Override
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

    public Abrechnung erzeugeAbrechnung(Mandant mandant) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setIMandant(mandant);
        abrechnung.setNummer(3);
        abrechnung.setMj(new MonatJahr(4, 2018));
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        mandant.addAbrechnung(abrechnung);
        return abrechnungRepository.save(abrechnung);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void nächsteAbrechnungTest() {
        IAbrechnung abrechnung = naechsteAbrechnung();
        assertEquals(4, abrechnung.getNummer());
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
    }

    public IAbrechnung naechsteAbrechnung() {
        IAbrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        return abrechnung.createOrGetNächsteAbrechnung(umgebung());
    }

    public IAbrechnung naechsteAbrechnungMerfacherAufruf() {
        Umgebung provider = umgebung();
        IAbrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        abrechnung.createOrGetNächsteAbrechnung(provider);
        abrechnung.createOrGetNächsteAbrechnung(provider);
        return abrechnung.createOrGetNächsteAbrechnung(provider);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void nächsteAbrechnungMehrfacherAufrufTest() {
        IAbrechnung abrechnung = naechsteAbrechnungMerfacherAufruf();
        assertEquals(1, mandantRepository.count());
        assertEquals(4, abrechnung.getNummer());
        assertEquals(2, abrechnungRepository.count());
    }

    public Optional<IAbrechnung> vorherigeAbrechnung() {
        IAbrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        return abrechnung.getVorherigeAbrechnung(umgebung());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void vorherigeAbrechnungTest() {
        Optional<IAbrechnung> oAbrechnung = vorherigeAbrechnung();
        assertFalse(oAbrechnung.isPresent());
        assertEquals(1, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void zusammenspielNachherVorher() {
        Umgebung provider = umgebung();

        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        IAbrechnung nächsteAbrechnung = abrechnung
                .createOrGetNächsteAbrechnung(provider);
        Optional<IAbrechnung> vorkerigeDerNächstenAbrechnung = nächsteAbrechnung
                .getVorherigeAbrechnung(provider);
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertTrue(vorkerigeDerNächstenAbrechnung.isPresent());
        assertEquals(abrechnung.getAbrechnungId(),
                vorkerigeDerNächstenAbrechnung.get().getAbrechnungId());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abgerechneteAbrechnung() {
        Mandant mandant = erzeugeMandant();
        IAbrechnung abrechnung = erzeugeAbrechnung(mandant);
        abrechnung.setMj(new MonatJahr(3, 2018));
        abrechnung.setStatus(AbrechnungsStatus.ABGERECHNET);
        abrechnung.setTyp(AbrechnungsTyp.TEILABRECHNUNG);
        abrechnung = abrechnungRepository.saveIAbrechnung(abrechnung);
        Optional<IAbrechnung> abgerechneteAbrechnung = mandant
                .getLetzteAbgerechneteAbrechnung(umgebung(),
                        new MonatJahr(3, 2018), AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertTrue(abgerechneteAbrechnung.isPresent());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void neueAbrechnung() {
        Umgebung provider = umgebung();

        IMandant mandant = mandantRepository.save(erzeugeMandant());
        IAbrechnung neueAbrechnung = mandant.createNeueAbrechnung(provider,
                new MonatJahr(3, 2018), AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertTrue(neueAbrechnung != null);
        assertEquals(1, neueAbrechnung.getNummer());

        neueAbrechnung = mandant.createNeueAbrechnung(provider, new MonatJahr(
                3, 2018), AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertTrue(neueAbrechnung != null);
        assertEquals(2, neueAbrechnung.getNummer());

    }

}
