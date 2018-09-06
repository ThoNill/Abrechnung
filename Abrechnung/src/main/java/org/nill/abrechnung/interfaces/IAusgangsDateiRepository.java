package org.nill.abrechnung.interfaces;

import java.util.List;

public interface IAusgangsDateiRepository {

    public List<IAusgangsDatei> getNichVersendeteDateien(int art);

    public IAusgangsDatei save(IAusgangsDatei d);

}