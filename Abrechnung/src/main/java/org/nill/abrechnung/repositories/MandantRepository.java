package org.nill.abrechnung.repositories;

import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.IMandantRepository;
import org.springframework.data.repository.CrudRepository;

public interface MandantRepository extends CrudRepository<Mandant, Long>, IMandantRepository {

    default IMandant saveI(IMandant mandant) {
        return this.save((Mandant)mandant);
    }

}
