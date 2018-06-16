package predicates;

import java.lang.reflect.Method;

public interface StaticPredicate<T> extends Predicate<T> {

    @Override
    default boolean test(T t) {
        try {
            Method m = this.getClass().getDeclaredMethod("staticTest",
                    Object.class);
            return (Boolean) m.invoke(null, t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
