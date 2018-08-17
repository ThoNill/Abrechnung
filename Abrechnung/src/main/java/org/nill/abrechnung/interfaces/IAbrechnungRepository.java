package org.nill.abrechnung.interfaces;

import java.util.List;

import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.allgemein.values.MonatJahr;

public interface IAbrechnungRepository {

    public List<IAbrechnung> getAbrechnung(IMandant mand, int n);

    public Integer getLetzteAbgerechneteAbrechnung(IMandant mand,
            AbrechnungsStatus status, MonatJahr mj);

    public Integer getLetzteAbrechnung(IMandant mand, MonatJahr mj);

    public Integer getLetzteAbrechnung(IMandant mandant);

    public IAbrechnung saveIAbrechnung(IAbrechnung abrechnung);

    public IAbrechnung findOne(long abrechnungId);

    public long count();

}