package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

public interface IGebührBerechnung {

    public BuchungsAuftrag<SachKonto> berechnen(IAbrechnung abrechnung);

    public BuchungsAuftrag<SachKonto> markierenUndberechnen(
            IAbrechnung abrechnung);

}