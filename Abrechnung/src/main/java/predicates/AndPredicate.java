package predicates;

public class AndPredicate<T> implements Predicate<T> {
    java.util.function.Predicate<T> predicates[];

    public AndPredicate(java.util.function.Predicate<T>... predicates) {
        super();
        this.predicates = predicates.clone();
    }

    @Override
    public boolean test(T t) {
        boolean isTrue = true;
        for (java.util.function.Predicate<T> p : predicates) {
            isTrue = isTrue && p.test(t);
        }
        return isTrue;
    }

}
