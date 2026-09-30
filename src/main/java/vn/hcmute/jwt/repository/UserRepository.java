package vn.hcmute.jwt.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.hcmute.jwt.entity.User;
public interface UserRepository extends JpaRepository<User, Long> { Optional<User> findByEmail(String email); }
