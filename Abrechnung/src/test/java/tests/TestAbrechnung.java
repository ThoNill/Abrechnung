package tests;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.AbrechnungsStatus;
import boundingContext.abrechnung.entities.AbrechnungsTyp;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.RunStatus;
import boundingContext.abrechnung.entities.Zeitraum;

public class TestAbrechnung extends TestEntity implements Abrechnung<Long> {
    private static AtomicInteger generatorDerNummern = new AtomicInteger();
    private Zeitraum zeitraum;
    private Mandant<Long> mandant;
    private int nummer = 1;
    private Date angelegt;
    private RunStatus runStatus;
    private AbrechnungsTyp typ;
    private AbrechnungsStatus status;

    public TestAbrechnung(Zeitraum zeitraum, Mandant<Long> mandant,
            AbrechnungsTyp typ) {
        super();
        this.zeitraum = zeitraum;
        this.mandant = mandant;
        this.typ = typ;
        this.nummer = generatorDerNummern.incrementAndGet();
        this.angelegt = new Date();
        this.runStatus = RunStatus.INAKTIV;
        this.status = AbrechnungsStatus.INITIAL;
    }

    public TestAbrechnung() {
        super();
    }

    @Override
    public String getBezeichnung() {
        return null;
    }

    @Override
    public Zeitraum getZeitraum() {
        return zeitraum;
    }

    @Override
    public Mandant<Long> getMandant() {
        return mandant;
    }

    @Override
    public int getNummer() {
        return nummer;
    }

    @Override
    public Date getAngelegt() {
        return angelegt;
    }

    @Override
    public RunStatus getRunStatus() {
        return runStatus;
    }

    @Override
    public AbrechnungsTyp getTyp() {
        return typ;
    }

    @Override
    public AbrechnungsStatus getStatus() {
        return status;
    }

    public static void setGeneratorDerNummern(AtomicInteger generatorDerNummern) {
        TestAbrechnung.generatorDerNummern = generatorDerNummern;
    }

    @Override
    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }

    @Override
    public void setTyp(AbrechnungsTyp typ) {
        this.typ = typ;
    }

    @Override
    public void setStatus(AbrechnungsStatus status) {
        this.status = status;
    }
}
