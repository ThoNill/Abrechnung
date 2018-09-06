package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;

/**
 * Ein {@link AbrechnungsKonfigurator} erzeugt aufgrund der geb�hrArt und
 * datenArt die in einer {@link IGeb�hrDefinition} abgelegt sind,
 * {@link Geb�hrFabrik} und {@link Geb�hrRepository}, au�erdem erzeugt er einen
 * {@link IGeb�hrBerechnung} aus der {@link AbrechnungsArt} und der
 * {@link Umgebung}
 * 
 * @author Thomas Nill
 *
 */
public interface AbrechnungsKonfigurator {

    Geb�hrFabrik erzeugeGeb�hrFabrik(int geb�hrArt);

    Geb�hrRepository<SachKonto> erzeugeGeb�hrRepository(int datenArt);

    IGeb�hrBerechnung erzeugeGeb�hrenBerechner(IGeb�hrDefinition definition,
            Umgebung umgebung, AbrechnungsArt abrechnungsArt);
}
