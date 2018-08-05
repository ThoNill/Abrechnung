package org.nill.abrechnung.aufzählungen;

import org.nill.basiskomponenten.ordinal.OrdinalConverter;

public class AbrechnungsTypConverter extends OrdinalConverter<AbrechnungsTyp> {
    public AbrechnungsTypConverter() {
        super(AbrechnungsTyp.values());
    }
}
