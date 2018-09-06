package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.nill.abrechnung.geb�hren.AnzahlGeb�hr;
import org.nill.abrechnung.geb�hren.FestGeb�hr;
import org.nill.abrechnung.geb�hren.MindestGeb�hr;
import org.nill.abrechnung.geb�hren.ProzentualeGeb�hr;
import org.nill.abrechnung.interfaces.Geb�hr;
import org.nill.basiskomponenten.betrag.Geld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Geb�hrenTest {
    protected static final Logger log = LoggerFactory
            .getLogger(Geb�hrenTest.class);

    @Test
    public void testeProzentualeGeb�hr() {
        ProzentualeGeb�hr g = new ProzentualeGeb�hr(0.05);

        assertEquals(Geld.createAmount(5), g.getGeb�hr(Geld.createAmount(100)));
        assertEquals(Geld.createAmount(-5),
                g.getGeb�hr(Geld.createAmount(-100)));
    }

    @Test
    public void testeProzentualeGeb�hrError() {
        try {
            new ProzentualeGeb�hr(-0.05);
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Erwartete Ausnahme", ex);
        }
    }

    @Test
    public void testeMindestGeb�hr() {
        Geb�hr g = new MindestGeb�hr(Geld.createAmount(25));

        assertEquals(Geld.createAmount(0), g.getGeb�hr(Geld.createAmount(100)));
        assertEquals(Geld.createAmount(25), g.getGeb�hr(Geld.createAmount(20)));
        assertEquals(Geld.createAmount(0), g.getGeb�hr(Geld.createAmount(-100)));
        assertEquals(Geld.createAmount(-25),
                g.getGeb�hr(Geld.createAmount(-20)));

    }

    @Test
    public void testeMindestGeb�hrError() {
        try {
            new MindestGeb�hr(Geld.createAmount(-25));
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Erwartete Ausnahme", ex);
        }
        try {
            new MindestGeb�hr(null);
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Erwartete Ausnahme", ex);
        }
    }

    @Test
    public void testeFestGeb�hr() {
        Geb�hr g = new FestGeb�hr(Geld.createAmount(25));

        assertEquals(Geld.createAmount(25), g.getGeb�hr(Geld.createAmount(100)));
        assertEquals(Geld.createAmount(25), g.getGeb�hr(Geld.createAmount(20)));
    }

    @Test
    public void testeFestGeb�hrError() {
        try {
            new FestGeb�hr(Geld.createAmount(-25));
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Erwartete Ausnahme", ex);
        }
        try {
            new FestGeb�hr(null);
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Erwartete Ausnahme", ex);
        }
    }

    @Test
    public void testeAnzahlGeb�hr() {
        Geb�hr g = new AnzahlGeb�hr(Geld.createAmount(25));

        assertEquals(Geld.createAmount(50), g.getGeb�hr(Geld.createAmount(2)));
        assertEquals(Geld.createAmount(75), g.getGeb�hr(Geld.createAmount(3)));
    }

    @Test
    public void testeAnzahlGeb�hrError() {
        try {
            new AnzahlGeb�hr(Geld.createAmount(-25));
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Erwartete Ausnahme", ex);
        }
        try {
            new AnzahlGeb�hr(null);
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Erwartete Ausnahme", ex);
        }
    }

}
