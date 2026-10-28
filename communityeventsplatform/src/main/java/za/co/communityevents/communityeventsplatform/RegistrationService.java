
package za.co.communityevents.communityeventsplatform;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public RegistrationService(
            EventRepository eventRepository,
            RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public Registration registerForEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EventNotFoundException(
                                "Event not found with ID: " + eventId));

        if (!"PUBLISHED".equals(event.getStatus())) {
            throw new IllegalArgumentException(
                    "You can only register for published events");
        }

        long registrationCount =
                registrationRepository.countByEventId(eventId);

        if (registrationCount >= event.getCapacity()) {
            throw new IllegalArgumentException(
                    "This event has reached its registration capacity");
        }

        Registration registration = new Registration();
        registration.setEventId(eventId);
        registration.setRegisteredAt(LocalDateTime.now());

        Registration savedRegistration =
                registrationRepository.save(registration);

        System.out.println(
                "REGISTRATION_CREATED | eventId=" + eventId
                + " | registrationId=" + savedRegistration.getId());

        return savedRegistration;
    }
}
