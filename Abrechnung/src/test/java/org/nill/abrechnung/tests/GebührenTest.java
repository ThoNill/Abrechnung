package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.nill.abrechnung.gebühren.AnzahlGebühr;
import org.nill.abrechnung.gebühren.FestGebühr;
import org.nill.abrechnung.gebühren.Gebühr;
import org.nill.abrechnung.gebühren.MindestGebühr;
import org.nill.abrechnung.gebühren.ProzentualeGebühr;
import org.nill.basiskomponenten.betrag.Geld;

public class GebührenTest {

    @Test
    public void testeProzentualeGebühr() {
        ProzentualeGebühr g = new ProzentualeGebühr(0.05);

        assertEquals(Geld.createAmount(5), g.getGebühr(Geld.createAmount(100)));
        assertEquals(Geld.createAmount(-5),
                g.getGebühr(Geld.createAmount(-100)));
    }

    @Test
    public void testeProzentualeGebührError() {
        try {
            ProzentualeGebühr g = new ProzentualeGebühr(-0.05);
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
        }
    }

    @Test
    public void testeMindestGebühr() {
        Gebühr g = new MindestGebühr(Geld.createAmount(25));

        assertEquals(Geld.createAmount(0), g.getGebühr(Geld.createAmount(100)));
        assertEquals(Geld.createAmount(25), g.getGebühr(Geld.createAmount(20)));
        assertEquals(Geld.createAmount(0), g.getGebühr(Geld.createAmount(-100)));
        assertEquals(Geld.createAmount(-25),
                g.getGebühr(Geld.createAmount(-20)));

    }

    @Test
    public void testeMindestGebührError() {
        try {
            Gebühr g = new MindestGebühr(Geld.createAmount(-25));
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
        }
        try {
            Gebühr g = new MindestGebühr(null);
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
        }
    }

    @Test
    public void testeFestGebühr() {
        Gebühr g = new FestGebühr(Geld.createAmount(25));

        assertEquals(Geld.createAmount(25), g.getGebühr(Geld.createAmount(100)));
        assertEquals(Geld.createAmount(25), g.getGebühr(Geld.createAmount(20)));
    }

    @Test
    public void testeFestGebührError() {
        try {
            Gebühr g = new FestGebühr(Geld.createAmount(-25));
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
        }
        try {
            Gebühr g = new FestGebühr(null);
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
        }
    }

    @Test
    public void testeAnzahlGebühr() {
        Gebühr g = new AnzahlGebühr(Geld.createAmount(25));

        assertEquals(Geld.createAmount(50), g.getGebühr(Geld.createAmount(2)));
        assertEquals(Geld.createAmount(75), g.getGebühr(Geld.createAmount(3)));
    }

    @Test
    public void testeAnzahlGebührError() {
        try {
            Gebühr g = new AnzahlGebühr(Geld.createAmount(-25));
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
        }
        try {
            Gebühr g = new AnzahlGebühr(null);
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
        }
    }

}
