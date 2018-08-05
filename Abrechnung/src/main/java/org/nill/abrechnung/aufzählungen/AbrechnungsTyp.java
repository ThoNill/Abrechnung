package org.nill.abrechnung.aufzählungen;

import org.nill.basiskomponenten.ordinal.OrdinalTyp;

public enum AbrechnungsTyp implements OrdinalTyp {
    INITIAL, TEILABRECHNUNG, WOCHENABRECHNUNG, MONATSABRECHNUNG, JAHRESABRECHNUNG;
}
