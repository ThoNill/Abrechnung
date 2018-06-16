package app.repositories;

import org.springframework.data.repository.CrudRepository;

import app.entities.Mandant;

public interface MandantRepository extends CrudRepository<Mandant, Long> {

}
