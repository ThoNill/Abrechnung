package org.nill.abrechnung.tests;

import org.nill.abrechnung.entities.Überweisung;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.repositories.ÜberweisungRepository;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.zahlungen.values.BIC;
import org.nill.zahlungen.values.BankVerbindung;
import org.nill.zahlungen.values.IBAN;

public class MitÜberweisungenTest extends AbrechnungBasisTest {

    public MitÜberweisungenTest() {
        super();
    }

    protected void createÜberweisung(String von, double betrag, int nummer,
            IMandant mandant, ÜberweisungRepository überweisungRepository) {
        Überweisung ü = new Überweisung();

        ü.setVon(new BankVerbindung(new IBAN(von), new BIC("INGDDEFF")));
        ü.setAn(new BankVerbindung(new IBAN("DE02300209000106531065"), new BIC(
                "CMCIDEDD")));
        ü.setBetrag(Geld.createAmount(betrag));
        ü.setVerwendungszweck("V " + nummer);
        ü = überweisungRepository.save(ü);
        ü.setIMandat(mandant);
    }

    protected void createÜberweisung(String von, int anz, IMandant mandant,
            ÜberweisungRepository überweisungRepository) {
        for (int i = 0; i < anz; i++) {
            createÜberweisung(von, 1.2 * i, i, mandant, überweisungRepository);
        }

    }

}