package org.nill.abrechnung.tests.flow;

import org.nill.abrechnung.gebühren.ProzentualeGebühr;
import org.nill.abrechnung.interfaces.Gebühr;
import org.nill.abrechnung.interfaces.GebührFabrik;

public class TestGebührFabrik implements GebührFabrik {

    @Override
    public Gebühr createGebühr(double parameter) {
        return new ProzentualeGebühr(parameter);
    }

}
