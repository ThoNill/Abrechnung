package boundingContext.abrechnung.aufzählungen;

import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;

interface RepositoryProvider {

    MandantRepository getMandantRepository();

    AbrechnungRepository getAbrechnungRepository();

    BuchungRepository getBuchungRepository();

    ZahlungsAuftragRepository getZahlungsAuftragRepository();

    ÜberweisungRepository getÜberweisungRepository();

}