package org.nill.abrechnung.aufzählungen;

import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ParameterRepository;
import org.nill.abrechnung.repositories.ZahlungsAuftragRepository;
import org.nill.abrechnung.repositories.ÜberweisungRepository;

interface RepositoryProvider {

    MandantRepository getMandantRepository();

    AbrechnungRepository getAbrechnungRepository();

    BuchungRepository getBuchungRepository();

    ZahlungsAuftragRepository getZahlungsAuftragRepository();

    ÜberweisungRepository getÜberweisungRepository();

    ParameterRepository getParameterRepository();

    AusgangsDateiRepository getAusgangsDateiRepository();

}