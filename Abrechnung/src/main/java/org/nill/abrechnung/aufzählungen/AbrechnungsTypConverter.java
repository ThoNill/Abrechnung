package org.nill.abrechnung.aufz�hlungen;

import ordinal.OrdinalConverter;

public class AbrechnungsTypConverter extends OrdinalConverter<AbrechnungsTyp> {
    public AbrechnungsTypConverter() {
        super(AbrechnungsTyp.values());
    }
}
