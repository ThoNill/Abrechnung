package app.entities;

import boundingContext.buchhaltung.Konto;

public class SimpleKonto implements Konto {
    int art;
    int nummer;
    String bezeichnung;

    public SimpleKonto(int art, int nummer, String bezeichnung) {
        super();
        this.art = art;
        this.nummer = nummer;
        this.bezeichnung = bezeichnung;
    }

    @Override
    public int getArt() {
        return art;
    }

    @Override
    public int getNummer() {
        return nummer;
    }

    @Override
    public String getBezeichnung() {
        return bezeichnung;
    }

    @Override
    public Long getId() {
        return 1000 * (long) art + nummer;
    }

}
