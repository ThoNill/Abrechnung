package tests.flow;

import tests.TestSachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.helper.LeitungsGeb�hrRepository;

public class TestLeistungsRepository extends LeitungsGeb�hrRepository {

    public TestLeistungsRepository() {
        super(TestSachKonto.BETRAG, 1);
    }

}
