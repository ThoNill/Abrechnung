package boundingContext.abrechnung.helper;

import boundingContext.abrechnung.aufz�hlungen.SachKonto;

public class TestLeistungsRepository extends LeitungsGeb�hrRepository {

    public TestLeistungsRepository() {
        super(SachKonto.BETRAG, 1);
    }

}
