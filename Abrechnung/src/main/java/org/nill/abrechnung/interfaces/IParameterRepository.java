package org.nill.abrechnung.interfaces;

import org.nill.abrechnung.aufzählungen.ParameterKey;
import org.nill.allgemein.values.MonatJahr;
import org.nill.allgemein.values.TypeReference;

public interface IParameterRepository {

    public MonatJahr getMJ(ParameterKey key, TypeReference ref, MonatJahr mj);

    public String getWert(ParameterKey key, TypeReference ref);

    public String getWert(ParameterKey key, TypeReference ref, MonatJahr mj);

    public String getZeitWert(ParameterKey key, TypeReference ref,
            MonatJahr akuellesMj);

    public Double getDoubleZeitWert(ParameterKey key, TypeReference ref,
            MonatJahr akuellesMj);

    public Integer getIntZeitWert(ParameterKey key, TypeReference ref,
            MonatJahr akuellesMj);

    public IParameter save(IParameter p);

}