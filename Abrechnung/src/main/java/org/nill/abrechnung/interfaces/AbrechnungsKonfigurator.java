package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.SachKonto;

/**
 * Ein {@link AbrechnungsKonfigurator} erzeugt aufgrund der gebührArt und
 * datenArt die in einer {@link IGebührDefinition} abgelegt sind,
 * {@link GebührFabrik} und {@link GebührRepository}, außerdem erzeugt er einen
 * {@link IGebührBerechnung} aus der {@link AbrechnungsArt} und der
 * {@link Umgebung}
 * 
 * @author Thomas Nill
 *
 */
public interface AbrechnungsKonfigurator {

    GebührFabrik erzeugeGebührFabrik(int gebührArt);

    GebührRepository<SachKonto> erzeugeGebührRepository(int datenArt);

    IGebührBerechnung erzeugeGebührenBerechner(IGebührDefinition definition,
            Umgebung umgebung, AbrechnungsArt abrechnungsArt);
}
