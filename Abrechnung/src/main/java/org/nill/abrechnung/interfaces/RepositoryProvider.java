package org.nill.abrechnung.interfaces;

/**
 * Zentraler Zugriff auf die unterschiedlichen Repositories
 * 
 * @author Thomas Nill
 *
 */
public interface RepositoryProvider {

    IMandantRepository getMandantRepository();

    IAbrechnungRepository getAbrechnungRepository();

    IBuchungsRepository getBuchungRepository();

    IZahlungsAuftragRepository getZahlungsAuftragRepository();

    I‹berweisungRepository get‹berweisungRepository();

    IParameterRepository getParameterRepository();

    IAusgangsDateiRepository getAusgangsDateiRepository();

}