package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.actions.GebührenBerechnung;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

/**
 * Eine {@link GebührenBerechnung} berechnet einen BuchungsAuftrag
 * damit dieser später gebucht werden kann.
 * Dazu kann sie ein {@link GebührRepository} und {@link Gebühr} 
 * benutzen.   
 * 
 * @author Thomas Nill
 *
 */
public interface IGebührBerechnung {

    /**
     * Berechnet eine neuen {@link BuchungsAuftrag} aus der {@link IAbrechnung}
     * 
     * @param abrechnung
     * @return
     */
    public BuchungsAuftrag<SachKonto> berechnen(IAbrechnung abrechnung);

    /**
     * Markiert Werte als zu einer {@link IAbrechnung} gehörig und berechnet einen neuen 
     * {@link BuchungsAuftrag} aus der {@link IAbrechnung}
     * 
     * @param abrechnung
     * @return
     */
     public BuchungsAuftrag<SachKonto> markierenUndberechnen(IAbrechnung abrechnung);

}