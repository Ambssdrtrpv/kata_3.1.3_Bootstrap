package kata.web.repository;

import kata.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u left join fetch u.roles where u.login = :login")
    User findByLogin(String login);

    User findUserById(int id);
}
