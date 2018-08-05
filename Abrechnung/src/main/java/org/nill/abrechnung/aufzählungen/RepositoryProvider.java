package org.nill.abrechnung.aufz‰hlungen;

import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ParameterRepository;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
import org.nill.abrechnung.repositories.‹berweisungRepository;

interface RepositoryProvider {

    MandantRepository getMandantRepository();

    AbrechnungRepository getAbrechnungRepository();

    BuchungRepository getBuchungRepository();

    ZahlungsAuftragRepository getZahlungsAuftragRepository();

    ‹berweisungRepository get‹berweisungRepository();

    ParameterRepository getParameterRepository();

    AusgangsDateiRepository getAusgangsDateiRepository();

}