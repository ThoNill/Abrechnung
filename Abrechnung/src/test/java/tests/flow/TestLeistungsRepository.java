package tests.flow;

import tests.TestSachKonto;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.helper.LeitungsGebührRepository;

public class TestLeistungsRepository extends LeitungsGebührRepository {

    public TestLeistungsRepository() {
        super(TestSachKonto.BETRAG, 1);
    }

}
