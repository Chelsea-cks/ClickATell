package za.co.communityevents.communityeventsplatform;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findByOrganiserId(String organiserId);
	List<Event> findByStatus(String status);
}