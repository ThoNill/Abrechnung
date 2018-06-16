package predicates;

public interface Predicate<T> extends java.util.function.Predicate<T> {

    default boolean isTrue(T t) {
        return test(t);
    }

    default boolean isFalse(T t) {
        return !test(t);
    }

    default java.util.function.Predicate<T> and(
            java.util.function.Predicate<T>... predicates) {
        return new AndPredicate<T>(predicates);
    }

    default java.util.function.Predicate<T> or(
            java.util.function.Predicate<T>... predicates) {
        return new OrPredicate<T>(predicates);
    }
}
