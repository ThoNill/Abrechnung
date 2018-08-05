package org.nill.abrechnung.tests.flow;

import org.nill.abrechnung.gebühren.Gebühr;
import org.nill.abrechnung.gebühren.GebührFabrik;
import org.nill.abrechnung.gebühren.ProzentualeGebühr;

public class TestGebührFabrik implements GebührFabrik {

    @Override
    public Gebühr createGebühr(double parameter) {
        return new ProzentualeGebühr(parameter);
    }

}
