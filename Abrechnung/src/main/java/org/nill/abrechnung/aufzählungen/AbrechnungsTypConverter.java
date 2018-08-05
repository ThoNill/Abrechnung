package org.nill.abrechnung.aufzählungen;

import ordinal.OrdinalConverter;

public class AbrechnungsTypConverter extends OrdinalConverter<AbrechnungsTyp> {
    public AbrechnungsTypConverter() {
        super(AbrechnungsTyp.values());
    }
}
