package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import boundingContext.zahlungen.values.MonatJahr;

public class MonatJahrTest {
    @Test
    public void test() {
        for (int m = 1; m <= 12; m++) {
            for (int j = 2001; j <= 2020; j++) {
                monatJahrVergleich(m, j);
            }
        }
    }

    private void monatJahrVergleich(int m, int j) {
        MonatJahr mj = new MonatJahr(m, j);
        assertEquals(m, mj.getMonat());
        assertEquals(j, mj.getJahr());
    }
}
