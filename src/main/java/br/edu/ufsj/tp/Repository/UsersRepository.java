package br.edu.ufsj.tp.Repository;

import br.edu.ufsj.tp.Model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UsersRepository extends MongoRepository<Users, String> {
    @Query("{email:'?0'}")
    Users findByEmail(String email);
}
