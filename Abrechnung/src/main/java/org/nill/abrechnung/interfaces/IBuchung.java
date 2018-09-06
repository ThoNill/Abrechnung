package org.nill.abrechnung.interfaces;

import java.util.Date;
import java.util.Set;

import org.nill.abrechnung.values.KontoBewegung;
import org.nill.allgemein.values.TypeReference;

public interface IBuchung {

    public void addBewegungen(KontoBewegung bewegungen);

    public void removeBewegungen(KontoBewegung bewegungen);

    public void addBezug(TypeReference bezug);

    public void removeBezug(TypeReference bezug);

    public void setIAbrechnung(IAbrechnung abrechnung2);

    public java.lang.Long getBuchungId();

    public void setBuchungId(java.lang.Long buchungId);

    public Date getBuchungsDatum();

    public void setBuchungsDatum(Date buchungsDatum);

    public String getText();

    public void setText(String text);

    public int getArt();

    public void setArt(int art);

    public Set<KontoBewegung> getBewegungen();

    public void setBewegungen(Set<KontoBewegung> bewegungen);

    public Set<TypeReference> getBezüge();

    public void setBezüge(Set<TypeReference> bezüge);

    public IAbrechnung getAbrechnung();

}