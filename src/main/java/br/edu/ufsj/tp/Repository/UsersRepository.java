package br.edu.ufsj.tp.Repository;

import br.edu.ufsj.tp.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UsersRepository extends MongoRepository<User, String> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findByEmail(String email);

    Page<User> findAllBySpecialtyContainingIgnoreCaseAndCrmIsNotNull(String specialty, Pageable pageable);

    Page<User> findAllByNameContainingIgnoreCaseAndCrmIsNotNull(String name, Pageable pageable);

    Page<User> findAllByNameContainingIgnoreCaseAndSpecialtyContainingIgnoreCaseAndCrmIsNotNull(
            String name, String specialty, Pageable pageable
    );

    Page<User> findAllByCrmIsNotNull(Pageable pageable);

}
