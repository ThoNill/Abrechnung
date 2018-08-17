package org.nill.abrechnung.tests.flow;

import org.nill.abrechnung.geb�hren.ProzentualeGeb�hr;
import org.nill.abrechnung.interfaces.Geb�hr;
import org.nill.abrechnung.interfaces.Geb�hrFabrik;

public class TestGeb�hrFabrik implements Geb�hrFabrik {

    @Override
    public Geb�hr createGeb�hr(double parameter) {
        return new ProzentualeGeb�hr(parameter);
    }

}
