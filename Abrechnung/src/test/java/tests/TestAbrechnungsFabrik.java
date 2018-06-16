package tests;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.AbrechnungsFabrik;
import boundingContext.abrechnung.entities.AbrechnungsTyp;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.Zeitraum;

public class TestAbrechnungsFabrik implements AbrechnungsFabrik<Long> {
    private Zeitraum zeitraum;
    private Mandant<Long> mandant;
    private AbrechnungsTyp typ;

    public TestAbrechnungsFabrik() {
        super();
    }

    public TestAbrechnungsFabrik(Zeitraum zeitraum, Mandant<Long> mandant,
            AbrechnungsTyp typ) {
        super();
        this.zeitraum = zeitraum;
        this.mandant = mandant;
        this.typ = typ;
    }

    @Override
    public Abrechnung<Long> create() {
        return new TestAbrechnung(zeitraum, mandant, typ);
    }

    @Override
    public Abrechnung<Long> create(Mandant<Long> mandant, Zeitraum zeitraum,
            AbrechnungsTyp typ) {
        return new TestAbrechnungsFabrik(zeitraum, mandant, typ).create();
    }

}
