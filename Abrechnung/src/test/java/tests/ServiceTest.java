package tests;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import boundingContext.abrechnung.entities.AbrechnungsTyp;
import boundingContext.abrechnung.entities.MandantenRepository;
import boundingContext.abrechnung.entities.Zeitraum;
import boundingContext.abrechnung.services.AbrechnungsAktion;
import boundingContext.abrechnung.services.AbrechnungsService;
import boundingContext.buchhaltung.eingang.BuchhaltungService;

@RunWith(SpringRunner.class)
public class ServiceTest {

    @Test
    public void keinKunde() {
        AbrechnungsService<Long, Position, TestGebührenDefinition, TestDatenRepository> abrechnungsService = new AbrechnungsService<>(
                new TestAbrechnungsRepository(), new TestMandantenRepository(),
                new TestAbrechnungsFabrik(),
                new TestAbrechnungsDatenRepositoryFabrik(),
                new BuchhaltungService<>(new TestBuchhaltungRepository()));
        Zeitraum zeitraum = new Zeitraum(3, 2018, 10, new Date(), new Date());
        try {
            abrechnungsService.einenMandantenAbrechnen(1L, zeitraum,
                    AbrechnungsAktion.ERSTABRECHNUNG,
                    AbrechnungsTyp.MONATSABRECHNUNG);
            fail("Ausnahme wegen Kunden erwartet");
        } catch (Exception ex) {

        }
    }

    @Ignore
    @Test
    public void keineAbrechnung() {
        MandantenRepository<Long, TestGebührenDefinition> mandantenRepository = new TestMandantenRepository();
        TestMandant mandant = new TestMandant();
        mandantenRepository.insert(mandant);
        AbrechnungsService<Long, Position, TestGebührenDefinition, TestDatenRepository> abrechnungsService = new AbrechnungsService<>(
                new TestAbrechnungsRepository(), mandantenRepository,
                new TestAbrechnungsFabrik(),
                new TestAbrechnungsDatenRepositoryFabrik(),
                new BuchhaltungService<>(new TestBuchhaltungRepository()));
        Zeitraum zeitraum = new Zeitraum(3, 2018, 10, new Date(), new Date());
        try {
            abrechnungsService.einenMandantenAbrechnen(mandant.getId(),
                    zeitraum, AbrechnungsAktion.ERSTABRECHNUNG,
                    AbrechnungsTyp.MONATSABRECHNUNG);
        } catch (Exception ex) {
            fail("unerwartete Ausnahme");
        }
    }

}
