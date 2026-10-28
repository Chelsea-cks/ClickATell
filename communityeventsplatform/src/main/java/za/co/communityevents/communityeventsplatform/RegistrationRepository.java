
package za.co.communityevents.communityeventsplatform;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    long countByEventId(Long eventId);
}
