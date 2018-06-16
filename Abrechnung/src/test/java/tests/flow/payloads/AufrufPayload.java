package tests.flow.payloads;

import boundingContext.abrechnung.entities.AbrechnungsTyp;

public class AufrufPayload {
    private AbrechnungsArt art;
    private AbrechnungsTyp typ;
    private long mandantId;
    private long abrechnungId;
    private int monat;
    private int jahr;

    public AufrufPayload(AbrechnungsArt art, long mandantId, long abrechnungId,
            int monat, int jahr, AbrechnungsTyp typ) {
        super();
        this.art = art;
        this.mandantId = mandantId;
        this.monat = monat;
        this.jahr = jahr;
        this.typ = typ;
        this.abrechnungId = abrechnungId;
    }

    public AbrechnungsArt getArt() {
        return art;
    }

    public long getMandantId() {
        return mandantId;
    }

    public int getMonat() {
        return monat;
    }

    public int getJahr() {
        return jahr;
    }

    public AbrechnungsTyp getTyp() {
        return typ;
    }

    public long getAbrechnungId() {
        return abrechnungId;
    }

    @Override
    public String toString() {
        return "AufrufPayload [art=" + art + ", typ=" + typ + ", mandantId="
                + mandantId + ", abrechnungId=" + abrechnungId + ", monat="
                + monat + ", jahr=" + jahr + "]";
    }

}
