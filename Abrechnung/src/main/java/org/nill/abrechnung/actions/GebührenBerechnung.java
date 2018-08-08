package org.nill.abrechnung.actions;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.flow.payloads.AbrechnungsArt;
import org.nill.abrechnung.gebühren.Gebühr;
import org.nill.abrechnung.gebühren.GebührFabrik;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.buchhaltung.eingang.SachKontoDelegate;

public class GebührenBerechnung extends SachKontoDelegate {
    private GebuehrDefinition definition;
    private GebührRepository<SachKonto> daten;
    private GebührFabrik gebührFabrik;
    private AbrechnungsArt abrechnungsArt;

    public GebührenBerechnung(SachKontoProvider sachKontoProvider,
            GebuehrDefinition definition, GebührRepository<SachKonto> daten,
            GebührFabrik gebührFabrik, AbrechnungsArt abrechnungsArt) {
        super(sachKontoProvider);
        this.definition = definition;
        this.daten = daten;
        this.gebührFabrik = gebührFabrik;
        this.abrechnungsArt = abrechnungsArt;
    }

    public BetragsBündel<SachKonto> gebührDazu(Abrechnung abrechnung,
            BetragsBündel<SachKonto> bündel) {
        MonetaryAmount basisBetrag = daten.getGebührenBasis(abrechnung);
        MonetaryAmount gebührBetrag = erzeugeGebühr().apply(basisBetrag);
        bündel.put(sachKontoFrom(definition.getKontoNr()),
                gebührBetrag.negate());
        if (definition.getMwstSatz() > 0.0) {
            MonetaryAmount mwstBetrag = Geld.round(gebührBetrag
                    .multiply(definition.getMwstSatz()));
            bündel.put(sachKontoFrom(definition.getMwstKonto()),
                    mwstBetrag.negate());
        }
        return bündel;
    }

    private Gebühr erzeugeGebühr() {
        return gebührFabrik.createGebühr(definition.getParameter());
    }

    public BuchungsAuftrag<SachKonto> berechnen(Abrechnung abrechnung) {
        BetragsBündel<SachKonto> bündel = daten.getBeträge(abrechnung);
        bündel = gebührDazu(abrechnung, bündel);
        Beschreibung beschreibung = new Beschreibung(
                definition.getBuchungsArt(), definition.getBuchungstext());
        return new BuchungsAuftrag<>(beschreibung, bündel);
    }

    public BuchungsAuftrag<SachKonto> markierenUndberechnen(
            Abrechnung abrechnung) {
        if (!AbrechnungsArt.NACHBERCHNEN.equals(abrechnungsArt)) {
            daten.markieren(abrechnung);
        }
        return berechnen(abrechnung);
    }

}
