package boundingContext.abrechnung;

import boundingContext.abrechnung.entities.Abrechnung;
import ddd.Repository;

public interface AbrechnungsDatenRepository<ID> extends Repository {
    void markiereDaten(Abrechnung<ID> abrechnung);
}
