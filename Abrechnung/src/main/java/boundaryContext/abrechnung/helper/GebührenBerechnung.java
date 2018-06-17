package boundaryContext.abrechnung.helper;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.aufzählungen.Position;
import boundingContext.abrechnung.gebühren.Gebühr;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.daten.GebührRepository;
import boundingContext.gemeinsam.BetragsBündel;

public class GebührenBerechnung {
    private GebuehrDefinition definition;
    private GebührRepository<Position> daten;
    private GebührFabrik gebührFabrik;

    public GebührenBerechnung(GebuehrDefinition definition,
            GebührRepository<Position> daten, GebührFabrik gebührFabrik) {
        super();
        this.definition = definition;
        this.daten = daten;
        this.gebührFabrik = gebührFabrik;
    }

    public BetragsBündel<Position> gebührDazu(Abrechnung abrechnung,
            BetragsBündel<Position> bündel) {
        MonetaryAmount basisBetrag = daten.getGebührenBasis(abrechnung);
        MonetaryAmount gebührBetrag = erzeugeGebühr().apply(basisBetrag);
        bündel.put(Position.values()[definition.getKontoNr()],
                gebührBetrag.negate());
        if (definition.getMwstSatz() > 0.0) {
            MonetaryAmount mwstBetrag = Geld.round(gebührBetrag
                    .multiply(definition.getMwstSatz()));
            bündel.put(Position.values()[definition.getMwstKonto()],
                    mwstBetrag.negate());
        }
        return bündel;
    }

    private Gebühr erzeugeGebühr() {
        return gebührFabrik.createGebühr(definition.getParameter());
    }

    public BuchungsAuftrag<Position> berechnen(Abrechnung abrechnung) {
        BetragsBündel<Position> bündel = daten.getBeträge(abrechnung);
        bündel = gebührDazu(abrechnung, bündel);
        Beschreibung beschreibung = new Beschreibung(
                definition.getBuchungsArt(), definition.getBuchungstext());
        return new BuchungsAuftrag<>(beschreibung, bündel);
    }

    public BuchungsAuftrag<Position> markierenUndberechnen(Abrechnung abrechnung) {
        daten.markieren(abrechnung);
        return berechnen(abrechnung);
    }

}
