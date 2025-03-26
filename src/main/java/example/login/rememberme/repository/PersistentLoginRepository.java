package example.login.rememberme.repository;

import example.login.rememberme.entity.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin,String> {
    Optional<PersistentLogin> findBySeries(String series);
    void deleteByUsername(String username);
}
