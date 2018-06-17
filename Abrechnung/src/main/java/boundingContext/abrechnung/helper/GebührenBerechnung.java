package boundingContext.abrechnung.helper;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.geb�hren.Geb�hr;
import boundingContext.abrechnung.geb�hren.Geb�hrFabrik;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.daten.Geb�hrRepository;
import boundingContext.gemeinsam.BetragsB�ndel;

public class Geb�hrenBerechnung extends SachKontoDelegate{
    private GebuehrDefinition definition;
    private Geb�hrRepository<SachKonto> daten;
    private Geb�hrFabrik geb�hrFabrik;

    public Geb�hrenBerechnung(SachKontoProvider sachKontoProvider,GebuehrDefinition definition,
            Geb�hrRepository<SachKonto> daten, Geb�hrFabrik geb�hrFabrik) {
        super(sachKontoProvider);
        this.definition = definition;
        this.daten = daten;
        this.geb�hrFabrik = geb�hrFabrik;
    }

    public BetragsB�ndel<SachKonto> geb�hrDazu(Abrechnung abrechnung,
            BetragsB�ndel<SachKonto> b�ndel) {
        MonetaryAmount basisBetrag = daten.getGeb�hrenBasis(abrechnung);
        MonetaryAmount geb�hrBetrag = erzeugeGeb�hr().apply(basisBetrag);
        b�ndel.put(sachKontoFrom(definition.getKontoNr())c,
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

    public BuchungsAuftrag<SachKonto> markierenUndberechnen(Abrechnung abrechnung) {
        daten.markieren(abrechnung);
        return berechnen(abrechnung);
    }

}
