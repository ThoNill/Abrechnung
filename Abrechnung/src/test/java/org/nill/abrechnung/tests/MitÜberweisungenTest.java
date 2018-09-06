package org.nill.abrechnung.tests;

import org.nill.abrechnung.entities.�berweisung;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.repositories.�berweisungRepository;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.zahlungen.values.BIC;
import org.nill.zahlungen.values.BankVerbindung;
import org.nill.zahlungen.values.IBAN;

public class Mit�berweisungenTest extends AbrechnungBasisTest {

    public Mit�berweisungenTest() {
        super();
    }

    protected void create�berweisung(String von, double betrag, int nummer,
            IMandant mandant, �berweisungRepository �berweisungRepository) {
        �berweisung � = new �berweisung();

        �.setVon(new BankVerbindung(new IBAN(von), new BIC("INGDDEFF")));
        �.setAn(new BankVerbindung(new IBAN("DE02300209000106531065"), new BIC(
                "CMCIDEDD")));
        �.setBetrag(Geld.createAmount(betrag));
        �.setVerwendungszweck("V " + nummer);
        � = �berweisungRepository.save(�);
        �.setIMandat(mandant);
    }

    protected void create�berweisung(String von, int anz, IMandant mandant,
            �berweisungRepository �berweisungRepository) {
        for (int i = 0; i < anz; i++) {
            create�berweisung(von, 1.2 * i, i, mandant, �berweisungRepository);
        }

    }

}