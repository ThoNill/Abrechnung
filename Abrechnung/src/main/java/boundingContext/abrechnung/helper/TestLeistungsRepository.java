package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufzählungen.SachKonto;

public class TestLeistungsRepository extends LeitungsGebührRepository {

    public TestLeistungsRepository() {
        super(SachKonto.BETRAG, 1);
    }

}
