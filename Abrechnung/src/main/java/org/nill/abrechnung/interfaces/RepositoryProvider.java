package org.nill.abrechnung.interfaces;



public interface RepositoryProvider {

    IMandantRepository getMandantRepository();

    IAbrechnungRepository getAbrechnungRepository();

    IBuchungsRepository getBuchungRepository();

    IZahlungsAuftragRepository getZahlungsAuftragRepository();

    I‹berweisungRepository get‹berweisungRepository();

    IParameterRepository getParameterRepository();

    IAusgangsDateiRepository getAusgangsDateiRepository();

}