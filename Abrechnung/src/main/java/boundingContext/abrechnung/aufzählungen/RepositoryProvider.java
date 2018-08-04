package boundingContext.abrechnung.aufz‰hlungen;

import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.AusgangsDateiRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ParameterRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.‹berweisungRepository;

interface RepositoryProvider {

    MandantRepository getMandantRepository();

    AbrechnungRepository getAbrechnungRepository();

    BuchungRepository getBuchungRepository();

    ZahlungsAuftragRepository getZahlungsAuftragRepository();

    ‹berweisungRepository get‹berweisungRepository();
    
    ParameterRepository getParameterRepository();
    

    AusgangsDateiRepository getAusgangsDateiRepository();

}