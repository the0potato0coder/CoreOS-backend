// From GymManagementBackend/src/main/java/com/example/GymManagementBackend/repository/UserRepository.java

package com.example.GymManagementBackend.repository;

import com.example.GymManagementBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address. Used for login and registration checks.
     * The result is wrapped in an Optional as a user might not exist.
     *
     * @param email The email to search for.
     * @return An Optional containing the User if found, otherwise empty.
     */
    Optional<User> findByEmail(String email);
}