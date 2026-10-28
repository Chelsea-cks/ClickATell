
package za.co.communityevents.communityeventsplatform;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getPublishedEvents() {
        return eventRepository.findByStatus("PUBLISHED");
    }

    public List<Event> getEventsForOrganiser(String organiserId) {
        return eventRepository.findByOrganiserId(organiserId);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new EventNotFoundException(
                                "Event not found with ID: " + id));
    }

    @Transactional
    public Event createEvent(Event event) {
        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new IllegalArgumentException("Event title is required");
        }

        if (event.getDescription() == null
                || event.getDescription().isBlank()) {
            throw new IllegalArgumentException(
                    "Event description is required");
        }

        if (event.getDateTime() == null
                || !event.getDateTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Event date and time must be in the future");
        }

        if (event.getCapacity() == null || event.getCapacity() <= 0) {
            throw new IllegalArgumentException(
                    "Event capacity must be greater than zero");
        }

        if (event.getOrganiserId() == null
                || event.getOrganiserId().isBlank()) {
            throw new IllegalArgumentException(
                    "Organiser identifier is required");
        }

        event.setStatus("PENDING_REVIEW");

        return eventRepository.save(event);
    }
    @Transactional
    public Event publishEvent(Long id) {
        Event event = eventRepository.findById(id)
        .orElseThrow(() ->
        new EventNotFoundException(
            "Event not found with ID:"  + id));
            event.setStatus("PUBLISHED");
            return eventRepository.save(event);

    }
}
