package boundaryContext.abrechnung.helper;

import boundingContext.abrechnung.aufz�hlungen.Position;

public class TestLeistungsRepository extends LeitungsGeb�hrRepository {

    public TestLeistungsRepository() {
        super(Position.BETRAG, 1);
    }

}
