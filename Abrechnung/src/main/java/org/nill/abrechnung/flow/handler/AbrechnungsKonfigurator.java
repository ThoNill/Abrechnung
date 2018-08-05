package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.actions.Geb�hrenBerechnung;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.flow.payloads.AbrechnungsArt;
import org.nill.abrechnung.geb�hren.Geb�hrFabrik;
import org.nill.daten.Geb�hrRepository;

public interface AbrechnungsKonfigurator {
    Geb�hrFabrik erzeugeGeb�hrFabrik(int geb�hrArt);

    Geb�hrRepository<SachKonto> erzeugeGeb�hrRepository(int datenArt);

    Geb�hrenBerechnung erzeugeGeb�hrenBerechner(GebuehrDefinition definition,
            SachKontoProvider sachKontoProvider, AbrechnungsArt abrechnungsArt);
}
