package mathe.gruppen;

import java.util.Objects;

public class DoubleAdditiv implements Gruppe<Double> {
    public static final DoubleAdditiv GRUPPE = new DoubleAdditiv();

    @Override
    public Double add(Double a, Double b) {
        return Objects.requireNonNull(a) + Objects.requireNonNull(b);
    }

    @Override
    public Double negate(Double x) {
        return -Objects.requireNonNull(x);
    }

    @Override
    public Double unit() {
        return 0.0d;
    }

    @Override
    public boolean isElement(Double x) {
        Objects.requireNonNull(x);
        return true;
    }

    @Override
    public boolean isUnit(Double x) {
        Objects.requireNonNull(x);
        return x == 0.0;
    }

}
