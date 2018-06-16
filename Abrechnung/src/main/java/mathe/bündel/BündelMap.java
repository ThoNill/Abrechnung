package mathe.bündel;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class BündelMap<KEY, VALUE> extends HashMap<KEY, VALUE> implements
        Bündel<KEY, VALUE> {

    public BündelMap() {
        super();
    }

    public BündelMap(int initialCapacity) {
        super(initialCapacity);
    }

    public BündelMap(Bündel<KEY, VALUE> bündel) {
        Objects.requireNonNull(bündel);

        for (KEY k : bündel.getKeys()) {
            put(k, bündel.getValue(k));
        }
    }

    public BündelMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    @Override
    public Set<KEY> getKeys() {
        return keySet();
    }

    @Override
    public VALUE getValue(KEY position) {
        return get(Objects.requireNonNull(position));
    }

}