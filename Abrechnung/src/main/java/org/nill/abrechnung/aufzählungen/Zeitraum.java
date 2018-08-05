package org.nill.abrechnung.aufzählungen;

import java.util.Date;

import org.nill.basiskomponenten.ddd.Value;

public class Zeitraum implements Value {
    private int monat;
    private int jahr;
    private int kalenderWoche;
    private Date vonDatum;
    private Date bisDatum;

    public Zeitraum(int monat, int jahr, int kalenderWoche, Date vonDatum,
            Date bisDatum) {
        super();
        this.monat = monat;
        this.jahr = jahr;
        this.kalenderWoche = kalenderWoche;
        this.vonDatum = vonDatum;
        this.bisDatum = bisDatum;
    }

    public int getMonat() {
        return monat;
    }

    public int getJahr() {
        return jahr;
    }

    public int getKalenderWoche() {
        return kalenderWoche;
    }

    public Date getVonDatum() {
        return vonDatum;
    }

    public Date getBisDatum() {
        return bisDatum;
    }

}
