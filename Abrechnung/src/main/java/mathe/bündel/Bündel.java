package mathe.bündel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.log4j.Logger;

import ddd.Repository;

public interface Bündel<KEY, ELEMENT> extends Repository {

    abstract Set<KEY> getKeys();

    ELEMENT getValue(KEY position);

    ELEMENT put(KEY position, ELEMENT value);

    default Set<KEY> alleKeys(Bündel<KEY, ELEMENT> a, Bündel<KEY, ELEMENT> b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        Set<KEY> alleKeys = new HashSet(a.getKeys());
        alleKeys.addAll(b.getKeys());
        return alleKeys;
    }

    default Bündel<KEY, ELEMENT> filterKeys(Predicate<KEY> predicate) {
        Objects.requireNonNull(predicate);

        BündelMap<KEY, ELEMENT> bündel = new BündelMap<>();
        for (KEY k : getKeys()) {
            if (predicate.test(k)) {
                bündel.put(k, getValue(k));
            }
        }
        return bündel;
    }

    default Bündel<KEY, ELEMENT> forgetKeys(Predicate<KEY> predicate) {
        Objects.requireNonNull(predicate);

        return filterKeys(predicate.negate());
    }

    default Bündel<KEY, ELEMENT> filterValues(Predicate<ELEMENT> predicate) {
        Objects.requireNonNull(predicate);

        BündelMap<KEY, ELEMENT> bündel = new BündelMap<>();
        for (KEY k : getKeys()) {
            ELEMENT e = getValue(k);
            if (predicate.test(e)) {
                bündel.put(k, e);
            }
            ;
        }
        return bündel;
    }

    default <TO_KEY> Bündel<TO_KEY, ELEMENT> transformKeys(
            Function<KEY, TO_KEY> transformation) {
        Objects.requireNonNull(transformation);

        BündelMap<TO_KEY, ELEMENT> bündel = new BündelMap<>();
        for (KEY k : getKeys()) {
            bündel.put(transformation.apply(k), getValue(k));
        }
        return bündel;
    }

    default <TO_ELEMENT> BündelMap<KEY, TO_ELEMENT> transformElements(
            Function<ELEMENT, TO_ELEMENT> transformation) {
        Objects.requireNonNull(transformation);

        BündelMap<KEY, TO_ELEMENT> bündel = new BündelMap<>();
        for (KEY k : getKeys()) {
            bündel.put(k, transformation.apply(getValue(k)));
        }
        return bündel;
    }

    default <B extends BündelMap<KEY, ELEMENT>> B erweitern(B neuesBündel,
            Bündel<KEY, ELEMENT> bündel) {
        Objects.requireNonNull(bündel);

        for (KEY k : getKeys()) {
            neuesBündel.put(k, getValue(k));
        }
        for (KEY k : bündel.getKeys()) {
            neuesBündel.put(k, bündel.getValue(k));
        }

        return neuesBündel;
    }

    default BündelMap<KEY, ELEMENT> erweitern(Bündel<KEY, ELEMENT> bündel) {
        return erweitern(new BündelMap<>(), bündel);
    }

    default void log(Logger log) {
        Objects.requireNonNull(log);
        for (KEY k : getKeys()) {
            log.debug("Key " + k + "Value: " + getValue(k));
        }
    }

}