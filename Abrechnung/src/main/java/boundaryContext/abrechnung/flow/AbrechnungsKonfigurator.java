package boundaryContext.abrechnung.flow;

import boundaryContext.abrechnung.entities.GebuehrDefinition;
import boundaryContext.abrechnung.helper.Geb�hrenBerechnung;
import boundingContext.abrechnung.aufz�hlungen.Position;
import boundingContext.abrechnung.geb�hren.Geb�hrFabrik;
import boundingContext.daten.Geb�hrRepository;

public interface AbrechnungsKonfigurator {
    Geb�hrFabrik erzeugeGeb�hrFabrik(int geb�hrArt);

    Geb�hrRepository<Position> erzeugeGeb�hrRepository(int datenArt);

    Geb�hrenBerechnung erzeugeGeb�hrenBerechner(GebuehrDefinition definition);
}
