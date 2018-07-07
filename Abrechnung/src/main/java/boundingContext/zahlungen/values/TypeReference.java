package boundingContext.zahlungen.values;

import javax.persistence.Embeddable;

import ddd.Value;

public @Embeddable class TypeReference implements Value {
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
