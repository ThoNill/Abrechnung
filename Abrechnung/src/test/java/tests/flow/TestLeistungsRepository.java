package tests.flow;

import tests.konten.TestSachKonto;

public class TestLeistungsRepository extends LeitungsGeb�hrRepository {

    public TestLeistungsRepository() {
        super(TestSachKonto.BETRAG, 1);
    }

}
