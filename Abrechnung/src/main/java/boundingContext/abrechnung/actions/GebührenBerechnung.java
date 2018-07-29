package boundingContext.abrechnung.actions;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundingContext.abrechnung.gebühren.Gebühr;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.SachKontoDelegate;
import boundingContext.daten.GebührRepository;
import boundingContext.gemeinsam.BetragsBündel;

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
