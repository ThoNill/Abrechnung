package org.nill.abrechnung.aufz�hlungen;

import org.nill.basiskomponenten.ordinal.OrdinalTyp;

public enum AbrechnungsTyp implements OrdinalTyp {
    INITIAL, TEILABRECHNUNG, WOCHENABRECHNUNG, MONATSABRECHNUNG, JAHRESABRECHNUNG;
}
