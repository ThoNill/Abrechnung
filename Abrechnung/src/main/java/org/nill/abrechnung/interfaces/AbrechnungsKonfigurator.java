package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;

public interface AbrechnungsKonfigurator {
    Geb�hrFabrik erzeugeGeb�hrFabrik(int geb�hrArt);

    Geb�hrRepository<SachKonto> erzeugeGeb�hrRepository(int datenArt);

    IGeb�hrBerechnung erzeugeGeb�hrenBerechner(IGeb�hrDefinition definition,
            SachKontoProvider sachKontoProvider, AbrechnungsArt abrechnungsArt);
}
