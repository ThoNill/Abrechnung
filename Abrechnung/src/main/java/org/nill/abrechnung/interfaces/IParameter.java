package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufzählungen.ParameterKey;
import org.nill.allgemein.values.MonatJahr;
import org.nill.allgemein.values.TypeReference;

public interface IParameter {

    public java.lang.Long getParameterId();

    public void setParameterId(java.lang.Long parameterId);

    public ParameterKey getKey();

    public void setKey(ParameterKey key);

    public TypeReference getRef();

    public void setRef(TypeReference ref);

    public MonatJahr getMj();

    public void setMj(MonatJahr mj);

    public String getWert();

    public void setWert(String wert);

}