package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.aufzählungen.ParameterKey;
import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.entities.Parameter;
import org.nill.abrechnung.repositories.ParameterRepository;
import org.nill.allgemein.values.MonatJahr;
import org.nill.allgemein.values.TypeReference;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class ParameterTest extends AbrechnungBasisTest {

    @Test
    @Transactional("dbATransactionManager")
    public void lesen() {
        SachKontoProvider provider = sachKontoProvider();
        ParameterRepository repo = provider.getParameterRepository();

        createParameter(repo, 1, 2018, 0.6);
        createParameter(repo, 1, 2017, 0.3);
        createParameter(repo, 1, 2014, 0.2);

        assertEquals(0.3, repo.getDoubleZeitWert(
                ParameterKey.ZINS_ÜBERZAHLUNGEN, TypeReference.ALLE,
                new MonatJahr(6, 2017)), 0.001);
        assertEquals(0.3, repo.getDoubleZeitWert(
                ParameterKey.ZINS_ÜBERZAHLUNGEN, TypeReference.ALLE,
                new MonatJahr(1, 2017)), 0.001);
        assertEquals(0.2, repo.getDoubleZeitWert(
                ParameterKey.ZINS_ÜBERZAHLUNGEN, TypeReference.ALLE,
                new MonatJahr(12, 2016)), 0.001);
        assertNull(repo.getDoubleZeitWert(ParameterKey.ZINS_ÜBERZAHLUNGEN,
                TypeReference.ALLE, new MonatJahr(12, 2012)));

    }

    private void createParameter(ParameterRepository repo, int monat, int jahr,
            double wert) {
        Parameter p = new Parameter();
        p.setKey(ParameterKey.ZINS_ÜBERZAHLUNGEN);
        p.setMj(new MonatJahr(monat, jahr));
        p.setRef(TypeReference.ALLE);
        p.setWert("" + wert);
        repo.save(p);
    }

}
