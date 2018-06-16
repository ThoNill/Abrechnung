package mathe.gruppen;

import java.util.Objects;

public class DoubleMultiplikativ implements Gruppe<Double> {

    private static final double SEHR_KLEIN = 0.00000001d;

    @Override
    public Double add(Double a, Double b) {
        return Objects.requireNonNull(a) * Objects.requireNonNull(b);
    }

    @Override
    public Double negate(Double x) {
        return -1.0d / Objects.requireNonNull(x);
    }

    @Override
    public Double unit() {
        return 1.0d;
    }

    @Override
    public boolean isElement(Double x) {
        Objects.requireNonNull(x);
        return x >= SEHR_KLEIN || x <= -SEHR_KLEIN;
    }

    @Override
    public boolean isUnit(Double x) {
        Objects.requireNonNull(x);
        return x == 1.0d;
    }

}
