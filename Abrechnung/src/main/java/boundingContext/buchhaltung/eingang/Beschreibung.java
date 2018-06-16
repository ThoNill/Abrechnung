package boundingContext.buchhaltung.eingang;

import java.util.Objects;

import ddd.Value;

public class Beschreibung implements Value {
    private int art;
    private String text;

    public Beschreibung(int art, String text) {
        super();
        this.art = art;
        this.text = Objects.requireNonNull(text);
    }

    public int getArt() {
        return art;
    }

    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + art;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Beschreibung other = (Beschreibung) obj;
        if (art != other.art)
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

}
