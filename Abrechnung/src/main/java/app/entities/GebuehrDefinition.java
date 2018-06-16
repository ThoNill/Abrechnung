package app.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "GEBUEHRDEFINITION")
@SequenceGenerator(name = "GEBUEHRDEFINITION_SEQ", sequenceName = "GEBUEHRDEFINITION_SEQ")
public class GebuehrDefinition {
    private long gebuehrDefinitionId;
    private double parameter;
    private int art;
    private int kontoNr;
    private List<Mandant> mandanten = new ArrayList<Mandant>();

    private int datenArt;
    private int gebührArt;
    private int buchungsArt;
    private String name;
    private String beschreibung;
    private double mwstSatz;
    private int mwstKonto;
    private String buchungstext;

    public GebuehrDefinition() {
        super();
    }

    @Basic
    @Column(name = "GEBUEHRDEFINITIONID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEBUEHRDEFINITION_SEQ")
    public java.lang.Long getGebuehrDefinitionId() {
        return gebuehrDefinitionId;
    };

    public void setGebuehrDefinitionId(long gebuehrDefinitionId) {
        this.gebuehrDefinitionId = gebuehrDefinitionId;
    }

    @Basic
    @Column(name = "ART")
    public int getArt() {
        return art;
    };

    public void setArt(int art) {
        this.art = art;
    }

    @Basic
    @Column(name = "PARAMETER")
    public double getParameter() {
        return parameter;
    }

    public void setParameter(double parameter) {
        this.parameter = parameter;
    }

    @Basic
    @Column(name = "KONTONR")
    public int getKontoNr() {
        return kontoNr;
    }

    public void setKontoNr(int kontoNr) {
        this.kontoNr = kontoNr;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
            CascadeType.MERGE }, mappedBy = "gebuehrDefinitionen")
    public List<Mandant> getMandanten() {
        return mandanten;
    }

    public void setMandanten(List<Mandant> mandanten) {
        this.mandanten = mandanten;
    };

    public void addMandant(Mandant mandant) {
        this.mandanten.add(mandant);
    }

    @Basic
    @Column(name = "BUCHUNGSART")
    public int getBuchungsArt() {
        return buchungsArt;
    }

    public void setBuchungsArt(int buchungsArt) {
        this.buchungsArt = buchungsArt;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "BESCHREIBUNG")
    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Basic
    @Column(name = "MWSTSATZ")
    public double getMwstSatz() {
        return mwstSatz;
    }

    public void setMwstSatz(double mwstSatz) {
        this.mwstSatz = mwstSatz;
    }

    @Basic
    @Column(name = "MWSTKONTO")
    public int getMwstKonto() {
        return mwstKonto;
    }

    public void setMwstKonto(int mwstKonto) {
        this.mwstKonto = mwstKonto;
    }

    @Basic
    @Column(name = "BUCHUNGSTEXT")
    public String getBuchungstext() {
        return buchungstext;
    }

    public void setBuchungstext(String buchungstext) {
        this.buchungstext = buchungstext;
    }

    @Basic
    @Column(name = "DATENART")
    public int getDatenArt() {
        return datenArt;
    }

    public void setDatenArt(int datenArt) {
        this.datenArt = datenArt;
    }

    @Basic
    @Column(name = "GEBUEHRART")
    public int getGebührArt() {
        return gebührArt;
    }

    public void setGebührArt(int gebührArt) {
        this.gebührArt = gebührArt;
    }

}
