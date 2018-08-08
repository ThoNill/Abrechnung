package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.actions.GebührRepository;
import org.nill.abrechnung.actions.GebührenBerechnung;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.flow.payloads.AbrechnungsArt;
import org.nill.abrechnung.gebühren.GebührFabrik;

public interface AbrechnungsKonfigurator {
    GebührFabrik erzeugeGebührFabrik(int gebührArt);

    GebührRepository<SachKonto> erzeugeGebührRepository(int datenArt);

    GebührenBerechnung erzeugeGebührenBerechner(GebuehrDefinition definition,
            SachKontoProvider sachKontoProvider, AbrechnungsArt abrechnungsArt);
}
