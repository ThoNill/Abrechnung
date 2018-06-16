package tests;

public class TestGebührenDefinition {
    private boolean abrechnungA;
    private double gebührA;
    private boolean abrechnungB;
    private double gebührB;
    private double fixgebühr;
    private double mindesgebühr;

    public TestGebührenDefinition(boolean abrechnungA, double gebührA,
            boolean abrechnungB, double gebührB, double fixgebühr,
            double mindesgebühr) {
        super();
        this.abrechnungA = abrechnungA;
        this.gebührA = gebührA;
        this.abrechnungB = abrechnungB;
        this.gebührB = gebührB;
        this.fixgebühr = fixgebühr;
        this.mindesgebühr = mindesgebühr;
    }

    public boolean isAbrechnungA() {
        return abrechnungA;
    }

    public double getGebührA() {
        return gebührA;
    }

    public boolean isAbrechnungB() {
        return abrechnungB;
    }

    public double getGebührB() {
        return gebührB;
    }

    public double getFixgebühr() {
        return fixgebühr;
    }

    public double getMindesgebühr() {
        return mindesgebühr;
    }
}
