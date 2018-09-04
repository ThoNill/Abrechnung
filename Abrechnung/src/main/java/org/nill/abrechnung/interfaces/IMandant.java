package org.nill.abrechnung.interfaces;

import java.util.Optional;
import java.util.Set;

import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.allgemein.values.MonatJahr;

/**
 * Repräsentiert einen Mandanten, für den {@link IAbrechnung} erstellt werden.
 * 
 * 
 * @author javaman
 *
 */
public interface IMandant {

    public void addAbrechnung(IAbrechnung a);

    public void addZahlungsDefinitionen(ZahlungsDefinition d);

    public void addGebuehrDefinitionen(IGebührDefinition d);

    public Optional<IAbrechnung> getLetzteAbgerechneteAbrechnung(
            Umgebung provider, MonatJahr mj, AbrechnungsTyp typ);

    public IAbrechnung createNeueAbrechnung(Umgebung provider,
            MonatJahr mj, AbrechnungsTyp typ);

    public java.lang.Long getMandantId();

    public void setMandantId(java.lang.Long mandantId);

    public String getName();

    public void setName(String name);

    public Set<? extends IAbrechnung> getAbrechnung();

    public Set<? extends IGebührDefinition> getGebuehrDefinitionen();

    public Set<ZahlungsDefinition> getZahlungsDefinitionen();

 
}