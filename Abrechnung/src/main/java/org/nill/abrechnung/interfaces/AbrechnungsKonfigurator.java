package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.SachKonto;

public interface AbrechnungsKonfigurator {
    GebührFabrik erzeugeGebührFabrik(int gebührArt);

    GebührRepository<SachKonto> erzeugeGebührRepository(int datenArt);

    IGebührBerechnung erzeugeGebührenBerechner(IGebührDefinition definition,
            SachKontoProvider sachKontoProvider, AbrechnungsArt abrechnungsArt);
}
