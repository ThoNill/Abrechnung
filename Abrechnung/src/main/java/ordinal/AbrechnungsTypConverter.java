package ordinal;

import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;

public class AbrechnungsTypConverter extends OrdinalConverter<AbrechnungsTyp> {
    public AbrechnungsTypConverter() {
        super(AbrechnungsTyp.values());
    }
}
