package org.nill.abrechnung.aufz�hlungen;

import org.nill.basiskomponenten.ordinal.OrdinalConverter;

public class AbrechnungsTypConverter extends OrdinalConverter<AbrechnungsTyp> {
    public AbrechnungsTypConverter() {
        super(AbrechnungsTyp.values());
    }
}
