package predicates;

public class OrPredicate<T> implements Predicate<T> {
    java.util.function.Predicate<T> predicates[];

    public OrPredicate(java.util.function.Predicate<T>... predicates) {
        super();
        this.predicates = predicates.clone();
    }

    @Override
    public boolean test(T t) {
        for (java.util.function.Predicate<T> p : predicates) {
            if (p.test(t))
                return true;
        }
        return false;
    }

}
