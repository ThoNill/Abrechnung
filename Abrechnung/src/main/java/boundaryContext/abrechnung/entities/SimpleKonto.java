package boundaryContext.abrechnung.entities;


public class SimpleKonto {
    int art;
    int nummer;
    String bezeichnung;

    public SimpleKonto(int art, int nummer, String bezeichnung) {
        super();
        this.art = art;
        this.nummer = nummer;
        this.bezeichnung = bezeichnung;
    }

    public int getArt() {
        return art;
    }

    public int getNummer() {
        return nummer;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public Long getId() {
        return 1000 * (long) art + nummer;
    }

}
