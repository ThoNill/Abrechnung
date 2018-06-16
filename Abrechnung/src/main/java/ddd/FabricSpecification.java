package ddd;

import predicates.StaticPredicate;

public class FabricSpecification implements StaticPredicate<Entity> {

    public boolean staticTest(Entity e) {
        return !(e instanceof Value || e instanceof Entity || e instanceof Repository);
    }
}
