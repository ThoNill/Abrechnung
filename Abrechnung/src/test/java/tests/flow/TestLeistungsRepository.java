package tests.flow;

import tests.konten.TestSachKonto;
import boundingContext.abrechnung.helper.LeitungsGeb�hrRepository;

public class TestLeistungsRepository extends LeitungsGeb�hrRepository {

    public TestLeistungsRepository() {
        super(TestSachKonto.BETRAG, 1);
    }

}
