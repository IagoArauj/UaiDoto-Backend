package br.edu.ufsj.tp.Repository;

import br.edu.ufsj.tp.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UsersRepository extends MongoRepository<Users, String> {
    @Query("{email:'?0'}")
    Optional<Users> findByEmail(String email);

    List<Users> findAllBySpecialtyContainingAndCrmIsNotNull(String specialty);

    List<Users> findAllByNameContainingIgnoreCaseAndCrmIsNotNull(String name);

    List<Users> findAllByNameContainingIgnoreCaseAndSpecialtyContainingIgnoreCaseAndCrmIsNotNull(
            String name, String specialty
    );

    List<Users> findAllByCrmIsNotNull();

}
