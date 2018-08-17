package org.nill.abrechnung.interfaces;



public interface RepositoryProvider {

    IMandantRepository getMandantRepository();

    IAbrechnungRepository getAbrechnungRepository();

    IBuchungsRepository getBuchungRepository();

    IZahlungsAuftragRepository getZahlungsAuftragRepository();

    IÜberweisungRepository getÜberweisungRepository();

    IParameterRepository getParameterRepository();

    IAusgangsDateiRepository getAusgangsDateiRepository();

}