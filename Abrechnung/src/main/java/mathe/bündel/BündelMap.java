package mathe.b�ndel;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class B�ndelMap<KEY, VALUE> extends HashMap<KEY, VALUE> implements
        B�ndel<KEY, VALUE> {

    public B�ndelMap() {
        super();
    }

    public B�ndelMap(int initialCapacity) {
        super(initialCapacity);
    }

    public B�ndelMap(B�ndel<KEY, VALUE> b�ndel) {
        Objects.requireNonNull(b�ndel);

        for (KEY k : b�ndel.getKeys()) {
            put(k, b�ndel.getValue(k));
        }
    }

    public B�ndelMap(int initialCapacity, float loadFactor) {
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