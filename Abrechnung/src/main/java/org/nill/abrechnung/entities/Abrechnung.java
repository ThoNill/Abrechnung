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
import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsStatus;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsTyp;
import org.nill.abrechnung.aufz�hlungen.ParameterKey;
import org.nill.abrechnung.aufz�hlungen.RunStatus;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IAbrechnungRepository;
import org.nill.abrechnung.interfaces.IGeb�hrBerechnung;
import org.nill.abrechnung.interfaces.IGeb�hrDefinition;
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
    public IAbrechnung createOrGetN�chsteAbrechnung(Umgebung provider) {
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
    public IAbrechnung abschlie�en(Umgebung provider) {
        int zinsDauer = provider.getParameterRepository().getIntZeitWert(
                ParameterKey.�BERZAHLUNGSTAGE, TypeReference.ALLE, getMj());
        double zinssatz = provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.ZINS_�BERZAHLUNGEN, TypeReference.ALLE, getMj());
        double mwstsatz = provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.MWST_GANZ, TypeReference.ALLE, getMj());
        return abschlie�en(provider, zinsDauer, zinssatz, mwstsatz);
    }

    /**
     * Diese Methode greift auf ein paar Helfer zur�ck, die einzelne Abschnitte
     * des Abschu�es einer Abrechnung abkapseln
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
    public IAbrechnung abschlie�en(Umgebung provider, int zinsDauer,
            double zinssatz, double mwstsatz) {

        ZahlungenEntfernen zahlungenEntfernen = new ZahlungenEntfernen(provider);
        zahlungenEntfernen.entferneZahlungsauftr�geFallsRestguthaben(this);

        SaldoAusgleichen ausgleichen = new SaldoAusgleichen(provider,
                "Guthaben", "Schulden");
        ausgleichen.saldoAusgleichen(this);

        IAbrechnung n�chsteAbrechnung = createOrGetN�chsteAbrechnung(provider);

        SchuldenInDieAbrechnung schulden�bertragen = new SchuldenInDieAbrechnung(
                provider, "Schulden �bernehmen", zinssatz, mwstsatz);
        schulden�bertragen.�bertragen(n�chsteAbrechnung, zinsDauer);
        return provider.getAbrechnungRepository().saveIAbrechnung(
                n�chsteAbrechnung);
    }

    @Override
    public void berechneDieGeb�hren(Umgebung provider,
            AbrechnungsKonfigurator konfigurator, AbrechnungsArt abrechnungsArt) {
        Set<Geb�hrDefinition> liste = getMandant().getGebuehrDefinitionen();

        for (IGeb�hrDefinition definition : liste) {
            bucheDenAuftrag(
                    provider,
                    berechneDenAuftrag(provider, konfigurator, abrechnungsArt,
                            definition));
        }
    }

    private BuchungsAuftrag<SachKonto> berechneDenAuftrag(Umgebung provider,
            AbrechnungsKonfigurator konfigurator,
            AbrechnungsArt abrechnungsArt, IGeb�hrDefinition definition) {

        IGeb�hrBerechnung berechnung = konfigurator.erzeugeGeb�hrenBerechner(
                definition, provider, abrechnungsArt);
        return berechnung.markierenUndberechnen(this);
    }

    private void bucheDenAuftrag(Umgebung provider,
            BuchungsAuftrag<SachKonto> auftrag) {
        new EinBucher(provider).erzeugeDifferenzBuchung(auftrag, this);
    }

    double get�berzahlungsZins(Umgebung provider) {
        return provider.getParameterRepository().getDoubleZeitWert(
                ParameterKey.ZINS_�BERZAHLUNGEN, TypeReference.ALLE, getMj());

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
