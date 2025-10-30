// From GymManagementBackend/src/main/java/com/example/GymManagementBackend/repository/SubscriptionRepository.java

package com.example.GymManagementBackend.repository;

import com.example.GymManagementBackend.model.Subscription;
import com.example.GymManagementBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    /**
     * Finds an active subscription for a given user.
     * @param user The user entity to find the subscription for.
     * @return An Optional containing the active Subscription, or empty if none is found.
     */
    Optional<Subscription> findByUserAndActiveTrue(User user);

    List<Subscription> findByActiveTrue();
}