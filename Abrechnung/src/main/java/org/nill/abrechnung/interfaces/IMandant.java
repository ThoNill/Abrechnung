package org.nill.abrechnung.interfaces;

import java.util.Optional;
import java.util.Set;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsTyp;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.allgemein.values.MonatJahr;

public interface IMandant {

    public void addAbrechnung(IAbrechnung a);

    public void addZahlungsDefinitionen(ZahlungsDefinition d);

    public void addGebuehrDefinitionen(IGeb�hrDefinition d);

    public Optional<IAbrechnung> getLetzteAbgerechneteAbrechnung(
            SachKontoProvider provider, MonatJahr mj, AbrechnungsTyp typ);

    public IAbrechnung createNeueAbrechnung(SachKontoProvider provider,
            MonatJahr mj, AbrechnungsTyp typ);

    public java.lang.Long getMandantId();

    public void setMandantId(java.lang.Long mandantId);

    public String getName();

    public void setName(String name);

    public Set<? extends IAbrechnung> getAbrechnung();

    public Set<? extends IGeb�hrDefinition> getGebuehrDefinitionen();

    public Set<ZahlungsDefinition> getZahlungsDefinitionen();

 
}