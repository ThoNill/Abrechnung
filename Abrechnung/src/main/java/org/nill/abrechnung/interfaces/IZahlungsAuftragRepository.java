package org.nill.abrechnung.interfaces;

import java.util.List;

public interface IZahlungsAuftragRepository {

    public List<IZahlungsAuftrag> getOffeneZahlungen(IAbrechnung abrechnung,
            int art);

    public IZahlungsAuftrag save(IZahlungsAuftrag zahlungsAuftrag);

}