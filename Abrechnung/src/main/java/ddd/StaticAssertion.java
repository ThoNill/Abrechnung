package ddd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import predicates.StaticPredicate;

public class StaticAssertion<T> implements Assertion<T> {
    private Method method;
 

    public StaticAssertion(Class<? extends StaticPredicate<T>> predicate,
            String message) {
        try {
            method = predicate.getDeclaredMethod("staticTest", Object.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrongClassOrMethod(predicate,e);
        }
    }

    static boolean assertion(Class<? extends StaticPredicate<?>> predicate,
            Object t, String message) {
        try {
            Method m = predicate.getDeclaredMethod("staticTest", Object.class);
            if (!(boolean) m.invoke(null, t)) {
                throw new AssertionException(message);
            }
        } catch (Exception e) {
            wrongClassOrMethod(predicate,e);
        }
        return false;
    }

    @Override
    public boolean test(T t) {
        try {
            return (boolean) method.invoke(null, t);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            wrongClassOrMethod(method.getDeclaringClass(),e);
        }
        return false;
    }

    private static void wrongClassOrMethod(Class<?> predicate,Exception ex) {
        throw new IllegalArgumentException("The class " + predicate.getName()
                + " need a static method staticTest(Object o) ",ex);
    }
}
