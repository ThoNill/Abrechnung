package org.nill.buchhaltung.eingang;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.nill.basiskomponenten.ddd.Value;

@Getter
@AllArgsConstructor
public class Beschreibung implements Value {
    private int art;
    private String text;

}
