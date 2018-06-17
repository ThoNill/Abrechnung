package boundaryContext.abrechnung.helper;

import boundingContext.abrechnung.aufzählungen.Position;

public class TestLeistungsRepository extends LeitungsGebührRepository {

    public TestLeistungsRepository() {
        super(Position.BETRAG, 1);
    }

}
