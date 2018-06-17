package boundaryContext.abrechnung.helper;

import boundingContext.abrechnung.geb�hren.Geb�hr;
import boundingContext.abrechnung.geb�hren.Geb�hrFabrik;
import boundingContext.abrechnung.geb�hren.ProzentualeGeb�hr;

public class TestGeb�hrFabrik implements Geb�hrFabrik {

    @Override
    public Geb�hr createGeb�hr(double parameter) {
        return new ProzentualeGeb�hr(parameter);
    }

}
