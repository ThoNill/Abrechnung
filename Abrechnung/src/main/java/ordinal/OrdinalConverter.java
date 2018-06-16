package ordinal;

import javax.persistence.AttributeConverter;

public class OrdinalConverter<ORDTYP extends OrdinalTyp> implements
        AttributeConverter<ORDTYP, Integer> {
    private ORDTYP[] map;

    public OrdinalConverter(ORDTYP[] map) {
        super();
        this.map = map;
    }

    @Override
    public Integer convertToDatabaseColumn(OrdinalTyp o) {
        if (o != null) {
            return new Integer(o.ordinal());
        }
        return new Integer(0);
    }

    @Override
    public ORDTYP convertToEntityAttribute(Integer i) {
        return map[i];
    }
}
