package org.nill.abrechnung.interfaces;

import java.util.List;

import org.nill.zahlungen.values.IBAN;

public interface I�berweisungRepository {

    public List<IBAN> getOffeneIBAN();

    public List<I�berweisung> getOffene�berweisungen(IBAN iban);

    public List<I�berweisung> get�berweisungen(IAusgangsDatei datei);

    public I�berweisung save(I�berweisung �berweisung);

}