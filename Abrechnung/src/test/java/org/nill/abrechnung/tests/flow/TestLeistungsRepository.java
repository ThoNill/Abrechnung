package org.nill.abrechnung.tests.flow;

import org.nill.abrechnung.tests.konten.TestSachKonto;

public class TestLeistungsRepository extends LeitungsGebührRepository {

    public TestLeistungsRepository() {
        super(TestSachKonto.BETRAG, 1);
    }

}
