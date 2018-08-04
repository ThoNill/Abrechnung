package boundingContext.abrechnung.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import boundingContext.abrechnung.actions.GebührenBerechnung;
import boundingContext.abrechnung.actions.SaldoAusgleichen;
import boundingContext.abrechnung.actions.SchuldenInDieAbrechnung;
import boundingContext.abrechnung.aufzählungen.AbrechnungsStatus;
import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufzählungen.ParameterKey;
import boundingContext.abrechnung.aufzählungen.RunStatus;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.flow.handler.AbrechnungsKonfigurator;
import boundingContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.zahlungen.actions.ZahlungenEntfernen;
import boundingContext.zahlungen.values.MonatJahr;
import boundingContext.zahlungen.values.MonatJahrAdapter;
import boundingContext.zahlungen.values.TypeReference;

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

    @Basic
    @Column(name = "MJ")
    @Convert(converter = MonatJahrAdapter.class)
    private MonatJahr mj;

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

    public Abrechnung createOrGetNächsteAbrechnung(
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
            neu.setMj(getMj());
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

    public Abrechnung abschleißen(SachKontoProvider provider, int zinsDauer,
            double zinssatz, double mwstsatz) {

        ZahlungenEntfernen zahlungenEntfernen = new ZahlungenEntfernen(provider);

        SaldoAusgleichen ausgleichen = new SaldoAusgleichen(provider,
                "Guthaben", "Schulden");

        SchuldenInDieAbrechnung schuldenÜbertragen = new SchuldenInDieAbrechnung(
                provider, "Schulden übernehmen", zinssatz, mwstsatz);
        zahlungenEntfernen.entferneZahlungsaufträgeFallsRestguthaben(this);
        ausgleichen.saldoAusgleichen(this);
        Abrechnung nächsteAbrechnung = this
                .createOrGetNächsteAbrechnung(provider);
        schuldenÜbertragen.übertragen(nächsteAbrechnung, zinsDauer);
        return nächsteAbrechnung;
    }

    public void berechneDieGebühren(SachKontoProvider provider,
            AbrechnungsKonfigurator konfigurator, AbrechnungsArt abrechnungsArt) {
        Set<GebuehrDefinition> liste = getMandant().getGebuehrDefinitionen();

        for (GebuehrDefinition definition : liste) {
            bucheDenAuftrag(
                    provider,
                    berechneDenAuftrag(provider, konfigurator, abrechnungsArt,
                            definition));
        }
    }

    private BuchungsAuftrag<SachKonto> berechneDenAuftrag(
            SachKontoProvider provider, AbrechnungsKonfigurator konfigurator,
            AbrechnungsArt abrechnungsArt, GebuehrDefinition definition) {

        GebührenBerechnung berechnung = konfigurator.erzeugeGebührenBerechner(
                definition, provider, abrechnungsArt);
        return berechnung.markierenUndberechnen(this);
    }

    private void bucheDenAuftrag(SachKontoProvider provider,
            BuchungsAuftrag<SachKonto> auftrag) {
        new EinBucher(provider).erzeugeDifferenzBuchung(auftrag, this);
    }

    double getÜberzahlungsZins(SachKontoProvider provider) {
        return provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.ZINS_ÜBERZAHLUNGEN, TypeReference.ALLE, getMj());

    }

    double getGanzeMwst(SachKontoProvider provider) {
        return provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.MWST_GANZ, TypeReference.ALLE, getMj());

    }

    double getHalbeMwst(SachKontoProvider provider) {
        return provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.MWST_HALB, TypeReference.ALLE, getMj());

    }
}
