package tests.flow;

import tests.konten.TestSachKonto;

public class TestLeistungsRepository extends LeitungsGebührRepository {

    public TestLeistungsRepository() {
        super(TestSachKonto.BETRAG, 1);
    }

}
