package app.entities;

import java.util.ArrayList;
import java.util.List;

import boundingContext.buchhaltung.Buchung;
import ddd.Entity;

public class BuchungBasis implements boundingContext.buchhaltung.Buchung {

    public BuchungBasis() {
        super();
    }

    private java.lang.Long BuchungId;
    private String text;
    private int art;
    private List<KontoBewegung> bewegungen = new ArrayList<>();
    private Abrechnung abrechnung;

    public java.lang.Long getBuchungId() {
        return this.BuchungId;
    };

    @Override
    public String getText() {
        return this.text;
    };

    @Override
    public int getArt() {
        return this.art;
    };

    @Override
    public List<KontoBewegung> getBewegungen() {
        return this.bewegungen;
    };

    public Abrechnung getAbrechnung() {
        return this.abrechnung;
    };

    public void setBuchungId(java.lang.Long BuchungId) {
        this.BuchungId = BuchungId;
    };

    public void setText(String text) {
        this.text = text;
    };

    public void setArt(int art) {
        this.art = art;
    };

    public void setBewegungen(List<KontoBewegung> bewegungen) {
        this.bewegungen = bewegungen;
    };

    public void addBewegungen(KontoBewegung bewegungen) {
        this.bewegungen.add(bewegungen);
    };

    public void removeBewegungen(KontoBewegung bewegungen) {
        this.bewegungen.remove(bewegungen);
    };

    public void setAbrechnung(Abrechnung abrechnung) {
        this.abrechnung = abrechnung;
    };

    @Override
    public String toString() {
        return "BuchungBasis " + " BuchungId =" + this.getBuchungId()
                + " Text =" + this.getText() + " Art =" + this.getArt()

                + " Abrechnung =" + this.getAbrechnung();
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
