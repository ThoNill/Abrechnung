package boundaryContext.abrechnung.helper;

import boundingContext.abrechnung.gebühren.Gebühr;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.abrechnung.gebühren.ProzentualeGebühr;

public class TestGebührFabrik implements GebührFabrik {

    @Override
    public Gebühr createGebühr(double parameter) {
        return new ProzentualeGebühr(parameter);
    }

}
