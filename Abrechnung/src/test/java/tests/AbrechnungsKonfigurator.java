package tests;

import app.entities.GebuehrDefinition;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.daten.GebührRepository;

public interface AbrechnungsKonfigurator {
    GebührFabrik erzeugeGebührFabrik(int gebührArt);

    GebührRepository<Position> erzeugeGebührRepository(int datenArt);

    GebührenBerechnung erzeugeGebührenBerechner(GebuehrDefinition definition);
}
