package backsideApp.sberhack2024.repository;

import backsideApp.sberhack2024.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);
}