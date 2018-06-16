package boundingContext.abrechnung;

import java.util.List;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.BuchungsAuftr�ge;

public interface AbrechnungsDatenRepositoryFabrik<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY extends AbrechnungsDatenRepository<ID>> {
    DATENREPOSITORY markierendesRepository(Abrechnung<ID> abrechnung);

    DATENREPOSITORY nachberechnungsRepository(Abrechnung<ID> abrechnung);

    DATENREPOSITORY stornierendesRepository(Abrechnung<ID> abrechnung);

    List<TeilAbrechnungsBeschreibung<KEY, GEB�HRENDEFINITION, DATENREPOSITORY>> getTeilAbrechnungsBeschreibungen();

    List<TeilAbrechnungsBeschreibung<KEY, GEB�HRENDEFINITION, BuchungsAuftr�ge<ID, KEY>>> getSammelAbrechnungsBeschreibungen();
}
