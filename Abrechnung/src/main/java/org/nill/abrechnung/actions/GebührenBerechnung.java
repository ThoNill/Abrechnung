package org.nill.abrechnung.actions;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.interfaces.Geb�hr;
import org.nill.abrechnung.interfaces.Geb�hrFabrik;
import org.nill.abrechnung.interfaces.Geb�hrRepository;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IGeb�hrBerechnung;
import org.nill.abrechnung.interfaces.IGeb�hrDefinition;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.interfaces.UmgebungDelegate;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

public class Geb�hrenBerechnung extends UmgebungDelegate implements IGeb�hrBerechnung {
    private IGeb�hrDefinition definition;
    private Geb�hrRepository<SachKonto> daten;
    private Geb�hrFabrik geb�hrFabrik;
    private AbrechnungsArt abrechnungsArt;

    public Geb�hrenBerechnung(Umgebung umgebung,
            IGeb�hrDefinition definition, Geb�hrRepository<SachKonto> daten,
            Geb�hrFabrik geb�hrFabrik, AbrechnungsArt abrechnungsArt) {
        super(umgebung);
        this.definition = definition;
        this.daten = daten;
        this.geb�hrFabrik = geb�hrFabrik;
        this.abrechnungsArt = abrechnungsArt;
    }

    @Override
    public BuchungsAuftrag<SachKonto> berechnen(IAbrechnung abrechnung) {
        BetragsB�ndel<SachKonto> b�ndel = daten.getBetr�ge(abrechnung);
        b�ndel = geb�hrDazu(abrechnung, b�ndel);
        Beschreibung beschreibung = new Beschreibung(
                definition.getBuchungsArt(), definition.getBuchungstext());
        return new BuchungsAuftrag<>(beschreibung, b�ndel);
    }

    @Override
    public BuchungsAuftrag<SachKonto> markierenUndberechnen(
            IAbrechnung abrechnung) {
        if (!AbrechnungsArt.NACHBERCHNEN.equals(abrechnungsArt)) {
            daten.markieren(abrechnung);
        }
        return berechnen(abrechnung);
    }

    
    
    private BetragsB�ndel<SachKonto> geb�hrDazu(IAbrechnung abrechnung,
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

  
}
