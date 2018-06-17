package boundaryContext.abrechnung.helper;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.aufz�hlungen.Position;
import boundingContext.abrechnung.geb�hren.Geb�hr;
import boundingContext.abrechnung.geb�hren.Geb�hrFabrik;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.daten.Geb�hrRepository;
import boundingContext.gemeinsam.BetragsB�ndel;

public class Geb�hrenBerechnung {
    private GebuehrDefinition definition;
    private Geb�hrRepository<Position> daten;
    private Geb�hrFabrik geb�hrFabrik;

    public Geb�hrenBerechnung(GebuehrDefinition definition,
            Geb�hrRepository<Position> daten, Geb�hrFabrik geb�hrFabrik) {
        super();
        this.definition = definition;
        this.daten = daten;
        this.geb�hrFabrik = geb�hrFabrik;
    }

    public BetragsB�ndel<Position> geb�hrDazu(Abrechnung abrechnung,
            BetragsB�ndel<Position> b�ndel) {
        MonetaryAmount basisBetrag = daten.getGeb�hrenBasis(abrechnung);
        MonetaryAmount geb�hrBetrag = erzeugeGeb�hr().apply(basisBetrag);
        b�ndel.put(Position.values()[definition.getKontoNr()],
                geb�hrBetrag.negate());
        if (definition.getMwstSatz() > 0.0) {
            MonetaryAmount mwstBetrag = Geld.round(geb�hrBetrag
                    .multiply(definition.getMwstSatz()));
            b�ndel.put(Position.values()[definition.getMwstKonto()],
                    mwstBetrag.negate());
        }
        return b�ndel;
    }

    private Geb�hr erzeugeGeb�hr() {
        return geb�hrFabrik.createGeb�hr(definition.getParameter());
    }

    public BuchungsAuftrag<Position> berechnen(Abrechnung abrechnung) {
        BetragsB�ndel<Position> b�ndel = daten.getBetr�ge(abrechnung);
        b�ndel = geb�hrDazu(abrechnung, b�ndel);
        Beschreibung beschreibung = new Beschreibung(
                definition.getBuchungsArt(), definition.getBuchungstext());
        return new BuchungsAuftrag<>(beschreibung, b�ndel);
    }

    public BuchungsAuftrag<Position> markierenUndberechnen(Abrechnung abrechnung) {
        daten.markieren(abrechnung);
        return berechnen(abrechnung);
    }

}
