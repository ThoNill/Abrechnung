package boundingContext.abrechnung.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import boundingContext.abrechnung.aufzählungen.AbrechnungsStatus;
import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufzählungen.RunStatus;
import boundingContext.abrechnung.aufzählungen.Zeitraum;

public class AbrechnungBasis {

    public AbrechnungBasis() {
        super();
    }

    private java.lang.Long AbrechnungId;
    private int monat;
    private int Jahr;
    private Mandant mandant;
    private AbrechnungsTyp typ = AbrechnungsTyp.INITIAL;
    private String bezeichnung;
    private int nummer;
    private RunStatus runStatus = RunStatus.CREATED;
    private Date angelegt = new Date();
    private Zeitraum zeitraum;
    private List<Buchung> buchung = new ArrayList<>();

    public java.lang.Long getAbrechnungId() {
        return this.AbrechnungId;
    };

    public int getMonat() {
        return this.monat;
    };

    public int getJahr() {
        return this.Jahr;
    };

    
    public Mandant getMandant() {
        return this.mandant;
    };

    public List<Buchung> getBuchung() {
        return this.buchung;
    };

    public void setAbrechnungId(java.lang.Long AbrechnungId) {
        this.AbrechnungId = AbrechnungId;
    };

    public void setMonat(int monat) {
        this.monat = monat;
    };

    public void setJahr(int Jahr) {
        this.Jahr = Jahr;
    };

    public void setMandant(Mandant mandant) {
        this.mandant = mandant;
    };

    public void setBuchung(List<Buchung> buchung) {
        this.buchung = buchung;
    };

    public void addBuchung(Buchung buchung) {
        this.buchung.add(buchung);
    };

    public void removeBuchung(Buchung buchung) {
        this.buchung.remove(buchung);
    };

    
    public String toString() {
        return "AbrechnungBasis " + " AbrechnungId =" + this.getAbrechnungId()
                + " Monat =" + this.getMonat() + " Jahr =" + this.getJahr()
                + " Mandant =" + this.getMandant()

        ;
    }

    
    public AbrechnungsTyp getTyp() {
        return typ;
    }

    
    public void setTyp(AbrechnungsTyp typ) {
        this.typ = typ;
    }

    
    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    
    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    
    public RunStatus getRunStatus() {
        return runStatus;
    }

    
    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }

    
    public Date getAngelegt() {
        return angelegt;
    }

    public void setAngelegt(Date angelegt) {
        this.angelegt = angelegt;
    }

    
    public Zeitraum getZeitraum() {
        return zeitraum;
    }

    public void setZeitraum(Zeitraum zeitraum) {
        this.zeitraum = zeitraum;
    }

    
    public Long getId() {
        // TODO Auto-generated method stub
        return null;
    }

    
    public AbrechnungsStatus getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void setStatus(AbrechnungsStatus status) {
        // TODO Auto-generated method stub

    }

}
