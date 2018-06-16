package boundingContext.abrechnung.entities;

import ddd.Fabric;

public interface AbrechnungsFabrik<ID> extends Fabric<Abrechnung<ID>> {
    Abrechnung<ID> create(Mandant<ID> mandant, Zeitraum zeitraum,
            AbrechnungsTyp typ);
}
