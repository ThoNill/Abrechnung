package ordinal;

import boundingContext.abrechnung.aufz�hlungen.AbrechnungsTyp;

public class AbrechnungsTypConverter extends OrdinalConverter<AbrechnungsTyp> {
    public AbrechnungsTypConverter() {
        super(AbrechnungsTyp.values());
    }
}
