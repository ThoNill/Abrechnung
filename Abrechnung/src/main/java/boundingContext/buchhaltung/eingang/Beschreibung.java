package boundingContext.buchhaltung.eingang;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ddd.Value;

@Getter
@AllArgsConstructor
public class Beschreibung implements Value {
    private int art;
    private String text;

}
