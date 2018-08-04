package tests;

import betrag.Geld;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.�berweisung;
import boundingContext.abrechnung.repositories.�berweisungRepository;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;

public class Mit�berweisungenTest {

    public Mit�berweisungenTest() {
        super();
    }

    protected void create�berweisung(String von, double betrag, int nummer,
            Mandant mandant, �berweisungRepository �berweisungRepository) {
        �berweisung � = new �berweisung();

        �.setVon(new BankVerbindung(new IBAN(von), new BIC("INGDDEFF")));
        �.setAn(new BankVerbindung(new IBAN("DE02300209000106531065"), new BIC(
                "CMCIDEDD")));
        �.setBetrag(Geld.createAmount(betrag));
        �.setVerwendungszweck("V " + nummer);
        � = �berweisungRepository.save(�);
        �.setMandant(mandant);
    }

    protected void create�berweisung(String von, int anz, Mandant mandant,
            �berweisungRepository �berweisungRepository) {
        for (int i = 0; i < anz; i++) {
            create�berweisung(von, 1.2 * i, i, mandant, �berweisungRepository);
        }

    }

}