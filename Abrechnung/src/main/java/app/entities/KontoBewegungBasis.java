package app.entities;

public class KontoBewegungBasis implements
        boundingContext.buchhaltung.KontoBewegung {

    public KontoBewegungBasis() {
        super();
    }

    private java.lang.Long KontoBewegungId;
    private int art;
    private int kontonr;
    private javax.money.MonetaryAmount betrag;
    private Buchung buchung;

    public java.lang.Long getKontoBewegungId() {
        return this.KontoBewegungId;
    };

    public int getArt() {
        return this.art;
    };

    public int getKontoNr() {
        return this.kontonr;
    };

    @Override
    public javax.money.MonetaryAmount getBetrag() {
        return this.betrag;
    };

    @Override
    public Buchung getBuchung() {
        return this.buchung;
    };

    public void setKontoBewegungId(java.lang.Long KontoBewegungId) {
        this.KontoBewegungId = KontoBewegungId;
    };

    public void setArt(int art) {
        this.art = art;
    };

    public void setKontoNr(int konto) {
        this.kontonr = konto;
    };

    public void setBetrag(javax.money.MonetaryAmount betrag) {
        this.betrag = betrag;
    };

    public void setBuchung(Buchung buchung) {
        this.buchung = buchung;
    };

    @Override
    public String toString() {
        return "KontoBewegungBasis " + " KontoBewegungId ="
                + this.getKontoBewegungId() + " Art =" + this.getArt()
                // + " Konto =" + this.getKonto() + " Betrag =" +
                // this.getBetrag()
                + " Buchung =" + this.getBuchung();
    }

    @Override
    public Long getId() {
        return getKontoBewegungId();
    }

    /*
     * @Override public Konto getKonto() { // TODO Auto-generated method stub
     * return null; }
     */
}
