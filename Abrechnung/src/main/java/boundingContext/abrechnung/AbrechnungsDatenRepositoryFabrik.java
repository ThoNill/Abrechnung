package boundingContext.abrechnung;

import java.util.List;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.BuchungsAufträge;

public interface AbrechnungsDatenRepositoryFabrik<ID, KEY, GEBÜHRENDEFINITION, DATENREPOSITORY extends AbrechnungsDatenRepository<ID>> {
    DATENREPOSITORY markierendesRepository(Abrechnung<ID> abrechnung);

    DATENREPOSITORY nachberechnungsRepository(Abrechnung<ID> abrechnung);

    DATENREPOSITORY stornierendesRepository(Abrechnung<ID> abrechnung);

    List<TeilAbrechnungsBeschreibung<KEY, GEBÜHRENDEFINITION, DATENREPOSITORY>> getTeilAbrechnungsBeschreibungen();

    List<TeilAbrechnungsBeschreibung<KEY, GEBÜHRENDEFINITION, BuchungsAufträge<ID, KEY>>> getSammelAbrechnungsBeschreibungen();
}
