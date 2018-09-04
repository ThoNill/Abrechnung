package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.actions.Geb�hrenBerechnung;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

/**
 * Eine {@link Geb�hrenBerechnung} berechnet einen BuchungsAuftrag
 * damit dieser sp�ter gebucht werden kann.
 * Dazu kann sie ein {@link Geb�hrRepository} und {@link Geb�hr} 
 * benutzen.   
 * 
 * @author Thomas Nill
 *
 */
public interface IGeb�hrBerechnung {

    /**
     * Berechnet eine neuen {@link BuchungsAuftrag} aus der {@link IAbrechnung}
     * 
     * @param abrechnung
     * @return
     */
    public BuchungsAuftrag<SachKonto> berechnen(IAbrechnung abrechnung);

    /**
     * Markiert Werte als zu einer {@link IAbrechnung} geh�rig und berechnet einen neuen 
     * {@link BuchungsAuftrag} aus der {@link IAbrechnung}
     * 
     * @param abrechnung
     * @return
     */
     public BuchungsAuftrag<SachKonto> markierenUndberechnen(IAbrechnung abrechnung);

}