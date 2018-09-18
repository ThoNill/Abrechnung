Abrechnung
=====

Abstrakte Abrechnung zum Abrechnen von Mandanten.

Zentrales Interface ist die IAbrechnung:

https://github.com/ThoNill/Abrechnung/blob/master/Abrechnung/src/main/java/org/nill/abrechnung/interfaces/IAbrechnung.java

mit zwei Methoden. Eine um Gebühren zu berechnen: berechneDieGebühren
und die zweite um eine IAbrechnung in einem Abrechnungszeitraum
abzuschließen: abschließen.

Die eigentliche Erzeugung einer Buchung erfolgt mit einem Interface IGebührBerechnung

https://github.com/ThoNill/Abrechnung/blob/master/Abrechnung/src/main/java/org/nill/abrechnung/interfaces/IGeb%C3%BChrBerechnung.java

