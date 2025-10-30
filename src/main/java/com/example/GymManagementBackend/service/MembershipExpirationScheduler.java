// From GymManagementBackend/src/main/java/com/example/GymManagementBackend/service/MembershipExpirationScheduler.java

package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.model.Subscription;
import com.example.GymManagementBackend.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j // Logger from Lombok
public class MembershipExpirationScheduler {

    private final SubscriptionRepository subscriptionRepository;

    /**
     * This scheduled task runs at 1:00 AM every day to check for and expire memberships.
     * The cron expression "0 0 1 * * *" means:
     * Seconds (0)
     * Minutes (0)
     * Hours (1) - 1:00 AM
     * Day of Month (*) - Every day
     * Month (*) - Every month
     * Day of Week (*) - Every day of the week
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void expireOldMemberships() {
        log.info("Starting scheduled job: expiring old memberships.");

        // Find all users who currently have a membership plan assigned.
        List<Subscription> activeSubscriptions = subscriptionRepository.findByActiveTrue();


        if (activeSubscriptions.isEmpty()) {
            log.info("No active memberships to check. Job finished.");
            return;
        }

        int expiredCount = 0;
        for (Subscription subscription : activeSubscriptions) {
            // Check if the user's subscription end date has passed.
            if (subscription.getEndDate() != null && subscription.getEndDate().isBefore(LocalDate.now())) {
                log.info("Expiring membership for user with email: {}", subscription.getUser().getEmail());

                // Remove the membership plan from the user by setting the fields to null.
                subscription.setActive(false);


                // Save the updated user object.
                subscriptionRepository.save(subscription);
                expiredCount++;
            }
        }

        log.info("Scheduled job finished. Expired {} memberships.", expiredCount);
    }
}