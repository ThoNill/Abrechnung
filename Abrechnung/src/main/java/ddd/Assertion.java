package ddd;

import predicates.Predicate;

public interface Assertion<T> extends Predicate<T> {

    default void assertion(T t, String message) {
        try {
            if (!test(t)) {
                throw new AssertionException(message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
