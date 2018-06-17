package boundaryContext.abrechnung.flow;

import boundaryContext.abrechnung.entities.GebuehrDefinition;
import boundaryContext.abrechnung.helper.GebührenBerechnung;
import boundingContext.abrechnung.aufzählungen.Position;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.daten.GebührRepository;

public interface AbrechnungsKonfigurator {
    GebührFabrik erzeugeGebührFabrik(int gebührArt);

    GebührRepository<Position> erzeugeGebührRepository(int datenArt);

    GebührenBerechnung erzeugeGebührenBerechner(GebuehrDefinition definition);
}
