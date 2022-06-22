package br.edu.ufsj.tp.Repository;

import br.edu.ufsj.tp.Model.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CityRepository extends MongoRepository<City, String> {
    Optional<City> findByCity(String city);
}
