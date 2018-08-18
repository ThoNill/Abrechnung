package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.entities.ZahlungsAuftrag;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IZahlungsAuftrag;
import org.nill.abrechnung.interfaces.IZahlungsAuftragRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ZahlungsAuftragRepository extends
        CrudRepository<ZahlungsAuftrag, Long>, IZahlungsAuftragRepository {

    @Override
    @Query("select z from org.nill.abrechnung.entities.ZahlungsAuftrag z where z.abrechnung = :abr and z.buchungsart = :art and z.ausbezahlt is null")
    public List<IZahlungsAuftrag> getOffeneZahlungen(
            @Param("abr") IAbrechnung abrechnung, @Param("art") int art);
    
    @Override
    public default ZahlungsAuftrag save(IZahlungsAuftrag zahlungsAuftrag) {
        return this.save((ZahlungsAuftrag)zahlungsAuftrag);
    }

}
