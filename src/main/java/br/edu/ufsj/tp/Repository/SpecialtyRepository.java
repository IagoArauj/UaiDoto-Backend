package br.edu.ufsj.tp.Repository;

import br.edu.ufsj.tp.Model.Specialty;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecialtyRepository extends MongoRepository<Specialty, String> {

}
