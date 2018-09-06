package org.nill.abrechnung.entities;

import java.util.Date;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.actions.SaldoAusgleichen;
import org.nill.abrechnung.actions.SchuldenInDieAbrechnung;
import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.aufzählungen.ParameterKey;
import org.nill.abrechnung.aufzählungen.RunStatus;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IAbrechnungRepository;
import org.nill.abrechnung.interfaces.IGebührBerechnung;
import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.allgemein.values.MonatJahr;
import org.nill.allgemein.values.MonatJahrAdapter;
import org.nill.allgemein.values.TypeReference;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.zahlungen.actions.ZahlungenEntfernen;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ABRECHNUNG")
@SequenceGenerator(name = "ABRECHNUNG_SEQ", sequenceName = "ABRECHNUNG_SEQ")
public class Abrechnung implements IAbrechnung {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "ABRECHNUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ABRECHNUNG_SEQ")
    private java.lang.Long abrechnungId;

    @ToString.Include
    @EqualsAndHashCode.Include
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

    @Override
    public IAbrechnung createOrGetNächsteAbrechnung(Umgebung provider) {
        IAbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();

        List<IAbrechnung> liste = abrechnungRepository.getAbrechnung(
                getMandant(), getNummer() + 1);
        if (liste.isEmpty()) {
            Abrechnung neu = new Abrechnung();
            neu.setNummer(getNummer() + 1);
            neu.setIMandant(getMandant());
            neu.setMj(getMj());
            neu.setTyp(getTyp());
            return abrechnungRepository.saveIAbrechnung(neu);
        }
        return abrechnungRepository.saveIAbrechnung(liste.get(0));
    }

    @Override
    public void setIMandant(IMandant mandant) {
        setMandant((Mandant) mandant);
    }

    @Override
    public Optional<IAbrechnung> getVorherigeAbrechnung(Umgebung provider) {
        IAbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();
        List<IAbrechnung> liste = abrechnungRepository.getAbrechnung(
                getMandant(), getNummer() - 1);
        if (liste.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(liste.get(0));
    }

    @Override
    public IAbrechnung abschließen(Umgebung provider) {
        int zinsDauer = provider.getParameterRepository().getIntZeitWert(
                ParameterKey.ÜBERZAHLUNGSTAGE, TypeReference.ALLE, getMj());
        double zinssatz = provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.ZINS_ÜBERZAHLUNGEN, TypeReference.ALLE, getMj());
        double mwstsatz = provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.MWST_GANZ, TypeReference.ALLE, getMj());
        return abschließen(provider, zinsDauer, zinssatz, mwstsatz);
    }

    /**
     * Diese Methode greift auf ein paar Helfer zurück, die einzelne Abschnitte
     * des Abschußes einer Abrechnung abkapseln
     * 
     * {@link ZahlungenEntfernen} {@link SaldoAusgleichen}
     * {@link SchuldenInDieAbrechnung}
     * 
     * @param provider
     * @param zinsDauer
     * @param zinssatz
     * @param mwstsatz
     * @return
     */
    public IAbrechnung abschließen(Umgebung provider, int zinsDauer,
            double zinssatz, double mwstsatz) {

        ZahlungenEntfernen zahlungenEntfernen = new ZahlungenEntfernen(provider);
        zahlungenEntfernen.entferneZahlungsaufträgeFallsRestguthaben(this);

        SaldoAusgleichen ausgleichen = new SaldoAusgleichen(provider,
                "Guthaben", "Schulden");
        ausgleichen.saldoAusgleichen(this);

        IAbrechnung nächsteAbrechnung = createOrGetNächsteAbrechnung(provider);

        SchuldenInDieAbrechnung schuldenÜbertragen = new SchuldenInDieAbrechnung(
                provider, "Schulden übernehmen", zinssatz, mwstsatz);
        schuldenÜbertragen.übertragen(nächsteAbrechnung, zinsDauer);
        return provider.getAbrechnungRepository().saveIAbrechnung(
                nächsteAbrechnung);
    }

    @Override
    public void berechneDieGebühren(Umgebung provider,
            AbrechnungsKonfigurator konfigurator, AbrechnungsArt abrechnungsArt) {
        Set<GebührDefinition> liste = getMandant().getGebuehrDefinitionen();

        for (IGebührDefinition definition : liste) {
            bucheDenAuftrag(
                    provider,
                    berechneDenAuftrag(provider, konfigurator, abrechnungsArt,
                            definition));
        }
    }

    private BuchungsAuftrag<SachKonto> berechneDenAuftrag(Umgebung provider,
            AbrechnungsKonfigurator konfigurator,
            AbrechnungsArt abrechnungsArt, IGebührDefinition definition) {

        IGebührBerechnung berechnung = konfigurator.erzeugeGebührenBerechner(
                definition, provider, abrechnungsArt);
        return berechnung.markierenUndberechnen(this);
    }

    private void bucheDenAuftrag(Umgebung provider,
            BuchungsAuftrag<SachKonto> auftrag) {
        new EinBucher(provider).erzeugeDifferenzBuchung(auftrag, this);
    }

    double getÜberzahlungsZins(Umgebung provider) {
        return provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.ZINS_ÜBERZAHLUNGEN, TypeReference.ALLE, getMj());

    }

    double getGanzeMwst(Umgebung provider) {
        return provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.MWST_GANZ, TypeReference.ALLE, getMj());

    }

    double getHalbeMwst(Umgebung provider) {
        return provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.MWST_HALB, TypeReference.ALLE, getMj());
    }

}
