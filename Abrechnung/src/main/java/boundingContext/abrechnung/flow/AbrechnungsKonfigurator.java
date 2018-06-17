package boundingContext.abrechnung.flow;

import boundingContext.abrechnung.aufzählungen.Position;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.abrechnung.helper.GebührenBerechnung;
import boundingContext.daten.GebührRepository;

public interface AbrechnungsKonfigurator {
    GebührFabrik erzeugeGebührFabrik(int gebührArt);

    GebührRepository<Position> erzeugeGebührRepository(int datenArt);

    GebührenBerechnung erzeugeGebührenBerechner(GebuehrDefinition definition);
}
