package tests;

public class TestGeb�hrenDefinition {
    private boolean abrechnungA;
    private double geb�hrA;
    private boolean abrechnungB;
    private double geb�hrB;
    private double fixgeb�hr;
    private double mindesgeb�hr;

    public TestGeb�hrenDefinition(boolean abrechnungA, double geb�hrA,
            boolean abrechnungB, double geb�hrB, double fixgeb�hr,
            double mindesgeb�hr) {
        super();
        this.abrechnungA = abrechnungA;
        this.geb�hrA = geb�hrA;
        this.abrechnungB = abrechnungB;
        this.geb�hrB = geb�hrB;
        this.fixgeb�hr = fixgeb�hr;
        this.mindesgeb�hr = mindesgeb�hr;
    }

    public boolean isAbrechnungA() {
        return abrechnungA;
    }

    public double getGeb�hrA() {
        return geb�hrA;
    }

    public boolean isAbrechnungB() {
        return abrechnungB;
    }

    public double getGeb�hrB() {
        return geb�hrB;
    }

    public double getFixgeb�hr() {
        return fixgeb�hr;
    }

    public double getMindesgeb�hr() {
        return mindesgeb�hr;
    }
}
