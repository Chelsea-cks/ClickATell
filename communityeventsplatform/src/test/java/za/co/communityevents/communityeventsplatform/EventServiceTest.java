
package za.co.communityevents.communityeventsplatform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void createEventRejectsBlankTitle() {
        Event event = new Event();
        event.setTitle("");
        event.setDescription("A community coding workshop");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.createEvent(event)
        );

        assertEquals("Event title is required", exception.getMessage());

        verify(eventRepository, never()).save(event);
    }
    
@Test
void createEventRejectsZeroCapacity() {
    Event event = new Event();
    event.setTitle("Community Coding Workshop");
    event.setDescription("A beginner-friendly workshop.");
    event.setCapacity(0);
    event.setDateTime(
            java.time.LocalDateTime.now().plusDays(7)
    );
    event.setOrganiserId("organiser1");

    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> eventService.createEvent(event)
    );

    assertEquals(
            "Event capacity must be greater than zero",
            exception.getMessage()
    );

    verify(eventRepository, never()).save(event);
}

}
