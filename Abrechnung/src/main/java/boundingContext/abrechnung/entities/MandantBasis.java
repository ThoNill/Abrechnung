package boundingContext.abrechnung.entities;

import java.util.ArrayList;
import java.util.List;

public class MandantBasis  {

    public MandantBasis() {
        super();
    }

    private java.lang.Long MandantId;
    private String name;
    private List<Abrechnung> abrechnung = new ArrayList<>();
    private List<GebuehrDefinition> gebuehren = new ArrayList<>();

    public java.lang.Long getMandantId() {
        return this.MandantId;
    };

    public String getName() {
        return this.name;
    };

    public List<Abrechnung> getAbrechnung() {
        return this.abrechnung;
    };

    public void setMandantId(java.lang.Long MandantId) {
        this.MandantId = MandantId;
    };

    public void setName(String name) {
        this.name = name;
    };

    public void setAbrechnung(List<Abrechnung> abrechnung) {
        this.abrechnung = abrechnung;
    };

    public void addAbrechnung(Abrechnung abrechnung) {
        this.abrechnung.add(abrechnung);
    };

    public void removeAbrechnung(Abrechnung abrechnung) {
        this.abrechnung.remove(abrechnung);
    };

    @Override
    public String toString() {
        return "MandantBasis " + " MandantId =" + this.getMandantId()
                + " Name =" + this.getName()

        ;
    }

    public Long getId() {
        return getMandantId();
    }

    public List<GebuehrDefinition> getGebuehrDefinitionen() {
        return gebuehren;
    };

    public void setGebuehrDefinitionen(List<GebuehrDefinition> gebuehen) {
        this.gebuehren = gebuehen;
    };

    public void addGebuehrDefinitionen(GebuehrDefinition gebuehrDefinition) {
        this.gebuehren.add(gebuehrDefinition);
    };

}
