package boundingContext.abrechnung.repositories;

import java.util.List;

import javax.money.MonetaryAmount;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.KontoBewegung;
import boundingContext.abrechnung.entities.�berweisung;

public interface �berweisungRepository extends CrudRepository<�berweisung, Long> {


}
