package mathe.gruppen;

import java.util.Objects;

public class IntegerAdditiv implements Gruppe<Integer> {
    public static final IntegerAdditiv GRUPPE = new IntegerAdditiv();

    @Override
    public Integer add(Integer a, Integer b) {
        return Objects.requireNonNull(a) + Objects.requireNonNull(b);
    }

    @Override
    public Integer negate(Integer x) {
        return -Objects.requireNonNull(x);
    }

    @Override
    public Integer unit() {
        return 0;
    }

    @Override
    public boolean isElement(Integer x) {
        Objects.requireNonNull(x);
        return true;
    }

    @Override
    public boolean isUnit(Integer x) {
        Objects.requireNonNull(x);
        return x == 0;
    }

}
