package app.repositories;

import org.springframework.data.repository.CrudRepository;

import app.entities.KontoBewegung;

public interface KontoBewegungRepository extends
        CrudRepository<KontoBewegung, Long> {

}
