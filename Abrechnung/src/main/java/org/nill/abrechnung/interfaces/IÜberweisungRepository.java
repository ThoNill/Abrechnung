package org.nill.abrechnung.interfaces;

import java.util.List;

import org.nill.zahlungen.values.IBAN;

public interface IÜberweisungRepository {

    public List<IBAN> getOffeneIBAN();

    public List<IÜberweisung> getOffeneÜberweisungen(IBAN iban);

    public List<IÜberweisung> getÜberweisungen(IAusgangsDatei datei);

    public IÜberweisung save(IÜberweisung überweisung);

}