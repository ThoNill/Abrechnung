package app.entities;

import java.util.List;

import boundingContext.buchhaltung.Buchung;
import ddd.Entity;

/**
 * Overwrite
 */
public class BuchungUser extends BuchungBasis {

    public BuchungUser() {
        super();
    }

    @Override
    public Long getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void stornieren() {
        // TODO Auto-generated method stub

    }

    @Override
    public Buchung getStornoBuchung() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Entity<?>> getReferenzen() {
        // TODO Auto-generated method stub
        return null;
    }

}
