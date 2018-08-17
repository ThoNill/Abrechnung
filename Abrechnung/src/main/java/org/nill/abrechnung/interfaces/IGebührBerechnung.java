package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

public interface IGeb�hrBerechnung {

    public BuchungsAuftrag<SachKonto> berechnen(IAbrechnung abrechnung);

    public BuchungsAuftrag<SachKonto> markierenUndberechnen(
            IAbrechnung abrechnung);

}