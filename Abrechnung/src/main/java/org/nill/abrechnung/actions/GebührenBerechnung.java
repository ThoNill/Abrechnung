package org.nill.abrechnung.actions;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.flow.payloads.AbrechnungsArt;
import org.nill.abrechnung.geb�hren.Geb�hr;
import org.nill.abrechnung.geb�hren.Geb�hrFabrik;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.buchhaltung.eingang.SachKontoDelegate;

public class Geb�hrenBerechnung extends SachKontoDelegate {
    private GebuehrDefinition definition;
    private Geb�hrRepository<SachKonto> daten;
    private Geb�hrFabrik geb�hrFabrik;
    private AbrechnungsArt abrechnungsArt;

    public Geb�hrenBerechnung(SachKontoProvider sachKontoProvider,
            GebuehrDefinition definition, Geb�hrRepository<SachKonto> daten,
            Geb�hrFabrik geb�hrFabrik, AbrechnungsArt abrechnungsArt) {
        super(sachKontoProvider);
        this.definition = definition;
        this.daten = daten;
        this.geb�hrFabrik = geb�hrFabrik;
        this.abrechnungsArt = abrechnungsArt;
    }

    public BetragsB�ndel<SachKonto> geb�hrDazu(Abrechnung abrechnung,
            BetragsB�ndel<SachKonto> b�ndel) {
        MonetaryAmount basisBetrag = daten.getGeb�hrenBasis(abrechnung);
        MonetaryAmount geb�hrBetrag = erzeugeGeb�hr().apply(basisBetrag);
        b�ndel.put(sachKontoFrom(definition.getKontoNr()),
                geb�hrBetrag.negate());
        if (definition.getMwstSatz() > 0.0) {
            MonetaryAmount mwstBetrag = Geld.round(geb�hrBetrag
                    .multiply(definition.getMwstSatz()));
            b�ndel.put(sachKontoFrom(definition.getMwstKonto()),
                    mwstBetrag.negate());
        }
        return b�ndel;
    }

    private Geb�hr erzeugeGeb�hr() {
        return geb�hrFabrik.createGeb�hr(definition.getParameter());
    }

    public BuchungsAuftrag<SachKonto> berechnen(Abrechnung abrechnung) {
        BetragsB�ndel<SachKonto> b�ndel = daten.getBetr�ge(abrechnung);
        b�ndel = geb�hrDazu(abrechnung, b�ndel);
        Beschreibung beschreibung = new Beschreibung(
                definition.getBuchungsArt(), definition.getBuchungstext());
        return new BuchungsAuftrag<>(beschreibung, b�ndel);
    }

    public BuchungsAuftrag<SachKonto> markierenUndberechnen(
            Abrechnung abrechnung) {
        if (!AbrechnungsArt.NACHBERCHNEN.equals(abrechnungsArt)) {
            daten.markieren(abrechnung);
        }
        return berechnen(abrechnung);
    }

}
