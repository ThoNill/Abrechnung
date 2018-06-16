package ddd;

import predicates.StaticPredicate;

public class ValueSpecification implements StaticPredicate<Value> {

    public boolean staticTest(Value e) {
        return !(e instanceof Entity || e instanceof Fabric || e instanceof Repository);
    }
}
