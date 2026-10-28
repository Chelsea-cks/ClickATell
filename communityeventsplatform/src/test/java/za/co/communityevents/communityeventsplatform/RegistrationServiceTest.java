
package za.co.communityevents.communityeventsplatform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void visitorCanRegisterForPublishedEvent() {
        Event event = new Event();
        event.setStatus("PUBLISHED");
        event.setCapacity(30);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        when(registrationRepository.countByEventId(1L))
                .thenReturn(0L);

          when(registrationRepository.save(any(Registration.class)))
            .thenAnswer(invocation -> {
                Registration registration =
                        invocation.getArgument(0);
                return registration;
            });
        Registration result =
                registrationService.registerForEvent(1L);

       assertEquals(1L, result.getEventId());
       assertNotNull(result.getRegisteredAt());
      

        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    void visitorCannotRegisterForUnpublishedEvent() {
        Event event = new Event();
        event.setStatus("PENDING_REVIEW");
        event.setCapacity(30);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registrationService.registerForEvent(1L)
        );

        assertEquals(
                "You can only register for published events",
                exception.getMessage()
        );

        verify(registrationRepository, never())
                .save(any(Registration.class));
    }

    @Test
    void visitorCannotRegisterWhenEventIsFull() {
        Event event = new Event();
        event.setStatus("PUBLISHED");
        event.setCapacity(2);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        when(registrationRepository.countByEventId(1L))
                .thenReturn(2L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registrationService.registerForEvent(1L)
        );

        assertEquals(
                "This event has reached its registration capacity",
                exception.getMessage()
        );

        verify(registrationRepository, never())
                .save(any(Registration.class));
    }
}
