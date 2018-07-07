package tests.flow;

import tests.konten.TestSachKonto;
import boundingContext.abrechnung.helper.LeitungsGebührRepository;

public class TestLeistungsRepository extends LeitungsGebührRepository {

    public TestLeistungsRepository() {
        super(TestSachKonto.BETRAG, 1);
    }

}
