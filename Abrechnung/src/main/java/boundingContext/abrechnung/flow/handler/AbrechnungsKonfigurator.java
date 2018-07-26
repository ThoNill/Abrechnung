package boundingContext.abrechnung.flow.handler;

import boundingContext.abrechnung.actions.GebührenBerechnung;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.gebühren.GebührFabrik;
import boundingContext.daten.GebührRepository;

public interface AbrechnungsKonfigurator {
    GebührFabrik erzeugeGebührFabrik(int gebührArt);

    GebührRepository<SachKonto> erzeugeGebührRepository(int datenArt);

    GebührenBerechnung erzeugeGebührenBerechner(GebuehrDefinition definition,SachKontoProvider sachKontoProvider);
}
