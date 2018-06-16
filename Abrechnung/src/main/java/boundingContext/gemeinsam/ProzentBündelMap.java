package boundingContext.gemeinsam;

import java.util.Objects;

import mathe.bündel.BündelMap;

public class ProzentBündelMap<KEY> extends BündelMap<KEY, Double> implements
        ProzentBündel<KEY> {

    @Override
    public Double getValue(KEY position) {
        Double d = get(Objects.requireNonNull(position));
        if (d != null) {
            return d.doubleValue();
        }
        return 0.0;
    }

    public ProzentBündelMap<KEY> setValue(KEY position, double d) {
        put(Objects.requireNonNull(position), d);
        return this;
    }

    @Override
    public Double put(KEY position, Double d) {
        Objects.requireNonNull(position);

        if (d.doubleValue() < 0) {
            throw new IllegalArgumentException(
                    "Der Prozentwert sollte > 0 sein");
        }
        return super.put(position, d);
    }

}
