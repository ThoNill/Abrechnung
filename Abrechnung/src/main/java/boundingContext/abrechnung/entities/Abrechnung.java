package boundingContext.abrechnung.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import boundingContext.abrechnung.actions.SaldoAusgleichen;
import boundingContext.abrechnung.actions.SchuldenInDieAbrechnung;
import boundingContext.abrechnung.aufz�hlungen.AbrechnungsStatus;
import boundingContext.abrechnung.aufz�hlungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufz�hlungen.RunStatus;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.zahlungen.actions.ZahlungenEntfernen;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ABRECHNUNG")
@SequenceGenerator(name = "ABRECHNUNG_SEQ", sequenceName = "ABRECHNUNG_SEQ")
public class Abrechnung {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "ABRECHNUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ABRECHNUNG_SEQ")
    private java.lang.Long AbrechnungId;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "MONAT")
    private int monat;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "JAHR")
    private int jahr;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS")
    private AbrechnungsStatus status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MandantId")
    private Mandant mandant;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TYP")
    private AbrechnungsTyp typ = AbrechnungsTyp.INITIAL;

    @Basic
    @Column(name = "BEZEICHNUNG")
    private String bezeichnung;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "NUMMER")
    private int nummer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RUN_STATUS")
    private RunStatus runStatus = RunStatus.CREATED;

    @Basic
    @Column(name = "ANGELEGT")
    private Date angelegt = new Date();

    // TODO Zeitraum zeitraum;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abrechnung", fetch = FetchType.LAZY)
    private Set<Buchung> buchung = new HashSet<>();

    public void addBuchung(Buchung buchung) {
        this.buchung.add(buchung);
    };

    public void removeBuchung(Buchung buchung) {
        this.buchung.remove(buchung);
    };

    public Abrechnung createOrGetN�chsteAbrechnung(
            @NotNull SachKontoProvider provider) {
        AbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();

        abrechnungRepository.save(this);
        Mandant mandant = provider.getMandantRepository().save(getMandant());
        List<Abrechnung> liste = abrechnungRepository.getAbrechnung(
                getMandant(), getNummer() + 1);
        if (liste.isEmpty()) {
            Abrechnung neu = new Abrechnung();
            neu.setNummer(getNummer() + 1);
            neu.setMandant(mandant);
            neu.setMonat(getMonat());
            neu.setJahr(getJahr());
            neu.setTyp(getTyp());
            return abrechnungRepository.save(neu);
        }
        return abrechnungRepository.save(liste.get(0));
    }

    public Optional<Abrechnung> getVorherigeAbrechnung(
            @NotNull SachKontoProvider provider) {
        AbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();
        List<Abrechnung> liste = abrechnungRepository.getAbrechnung(
                getMandant(), getNummer() - 1);
        if (liste.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(liste.get(0));
    }

    public Abrechnung abschlei�en(SachKontoProvider provider, int zinsDauer,
            double zinssatz) {

        ZahlungenEntfernen zahlungenEntfernen = new ZahlungenEntfernen(provider);

        SaldoAusgleichen ausgleichen = new SaldoAusgleichen(provider,
                "Guthaben", "Schulden");

        SchuldenInDieAbrechnung schulden�bertragen = new SchuldenInDieAbrechnung(
                provider, "Schulden �bernehmen", zinssatz);
        zahlungenEntfernen.entferneZahlungsauftr�geFallsRestguthaben(this);
        ausgleichen.saldoAusgleichen(this);
        Abrechnung n�chsteAbrechnung = this
                .createOrGetN�chsteAbrechnung(provider);
        schulden�bertragen.�bertragen(n�chsteAbrechnung, zinsDauer);
        return n�chsteAbrechnung;
    }

}
