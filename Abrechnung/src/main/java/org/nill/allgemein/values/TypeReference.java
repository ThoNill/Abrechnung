package org.nill.allgemein.values;

import javax.persistence.Embeddable;

import org.nill.basiskomponenten.ddd.Value;

@Embeddable
public class TypeReference implements Value {
    public static final TypeReference ALLE = new TypeReference(0, 0);

    private int art;
    private long id;

    public TypeReference(int art, long id) {
        super();
        this.art = art;
        this.id = id;
    }

    public TypeReference() {
        super();
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
