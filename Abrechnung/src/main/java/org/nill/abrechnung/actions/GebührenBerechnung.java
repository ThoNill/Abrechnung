package org.nill.abrechnung.actions;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.interfaces.Gebühr;
import org.nill.abrechnung.interfaces.GebührFabrik;
import org.nill.abrechnung.interfaces.GebührRepository;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IGebührBerechnung;
import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.interfaces.UmgebungDelegate;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

public class GebührenBerechnung extends UmgebungDelegate implements IGebührBerechnung {
    private IGebührDefinition definition;
    private GebührRepository<SachKonto> daten;
    private GebührFabrik gebührFabrik;
    private AbrechnungsArt abrechnungsArt;

    public GebührenBerechnung(Umgebung umgebung,
            IGebührDefinition definition, GebührRepository<SachKonto> daten,
            GebührFabrik gebührFabrik, AbrechnungsArt abrechnungsArt) {
        super(umgebung);
        this.definition = definition;
        this.daten = daten;
        this.gebührFabrik = gebührFabrik;
        this.abrechnungsArt = abrechnungsArt;
    }

    @Override
    public BuchungsAuftrag<SachKonto> berechnen(IAbrechnung abrechnung) {
        BetragsBündel<SachKonto> bündel = daten.getBeträge(abrechnung);
        bündel = gebührDazu(abrechnung, bündel);
        Beschreibung beschreibung = new Beschreibung(
                definition.getBuchungsArt(), definition.getBuchungstext());
        return new BuchungsAuftrag<>(beschreibung, bündel);
    }

    @Override
    public BuchungsAuftrag<SachKonto> markierenUndberechnen(
            IAbrechnung abrechnung) {
        if (!AbrechnungsArt.NACHBERCHNEN.equals(abrechnungsArt)) {
            daten.markieren(abrechnung);
        }
        return berechnen(abrechnung);
    }

    
    
    private BetragsBündel<SachKonto> gebührDazu(IAbrechnung abrechnung,
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

  
}
