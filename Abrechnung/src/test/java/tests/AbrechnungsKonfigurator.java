package tests;

import app.entities.GebuehrDefinition;
import boundingContext.abrechnung.geb�hren.Geb�hrFabrik;
import boundingContext.daten.Geb�hrRepository;

public interface AbrechnungsKonfigurator {
    Geb�hrFabrik erzeugeGeb�hrFabrik(int geb�hrArt);

    Geb�hrRepository<Position> erzeugeGeb�hrRepository(int datenArt);

    Geb�hrenBerechnung erzeugeGeb�hrenBerechner(GebuehrDefinition definition);
}
