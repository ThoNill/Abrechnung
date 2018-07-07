package boundingContext.abrechnung.flow.handler;

import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.geb�hren.Geb�hrFabrik;
import boundingContext.abrechnung.helper.Geb�hrenBerechnung;
import boundingContext.daten.Geb�hrRepository;

public interface AbrechnungsKonfigurator {
    Geb�hrFabrik erzeugeGeb�hrFabrik(int geb�hrArt);

    Geb�hrRepository<SachKonto> erzeugeGeb�hrRepository(int datenArt);

    Geb�hrenBerechnung erzeugeGeb�hrenBerechner(GebuehrDefinition definition);
}
