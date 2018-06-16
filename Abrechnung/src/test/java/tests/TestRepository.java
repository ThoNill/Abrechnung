package tests;

import java.util.Hashtable;
import java.util.Objects;

import ddd.Entity;
import ddd.Repository;

public class TestRepository<E extends Entity<Long>> implements Repository {
    protected Hashtable<Long, E> entities = new Hashtable<>();

    public TestRepository() {
        super();
    }

    public E getEntity(Long l) {
        return entities.get(Objects.requireNonNull(l));
    }

    public void insert(E entity) {
        Objects.requireNonNull(entity, "Die Entity darf nicht null sein");
        if (entities.containsKey(entity.getId())) {
            throw new IllegalArgumentException("Die ID " + entity.getId()
                    + " wurde schon geinserted");
        }
        entities.put(entity.getId(), entity);
    }

    public void update(E entity) {
        Objects.requireNonNull(entity, "Die Entity darf nicht null sein");
        if (!entities.containsKey(entity.getId())) {
            throw new IllegalArgumentException("Die ID " + entity.getId()
                    + " wurde noch nicht geinserted");
        }
    }

}
