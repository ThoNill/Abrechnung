package org.nill.zahlungen.values;

import javax.persistence.AttributeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MonatJahrAdapter extends XmlAdapter<Integer, MonatJahr> implements
        AttributeConverter<MonatJahr, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MonatJahr attribute) {
        return attribute.getMJ();
    }

    @Override
    public MonatJahr convertToEntityAttribute(Integer dbData) {
        return new MonatJahr(dbData.intValue());
    }

    @Override
    public MonatJahr unmarshal(Integer v) throws Exception {
        return new MonatJahr(v.intValue());
    }

    @Override
    public Integer marshal(MonatJahr v) throws Exception {
        return v.getMJ();
    }

}