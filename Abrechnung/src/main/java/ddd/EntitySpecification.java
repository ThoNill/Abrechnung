package ddd;

import predicates.StaticPredicate;

public class EntitySpecification implements StaticPredicate<Entity> {

    public boolean staticTest(Entity e) {
        return !(e instanceof Value || e instanceof Fabric || e instanceof Repository);
    }
}
