
package za.co.communityevents.communityeventsplatform;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(
            RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/{eventId}/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public Registration registerForEvent(
            @PathVariable Long eventId) {

        return registrationService.registerForEvent(eventId);
    }
}

