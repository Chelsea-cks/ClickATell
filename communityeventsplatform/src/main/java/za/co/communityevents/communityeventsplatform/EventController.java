
package za.co.communityevents.communityeventsplatform;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    // the visitors can view published events
    @GetMapping
    public List<Event> getPublishedEvents() {
        return eventService.getPublishedEvents();
    }
    //View an event by its ID.
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }
    	//Organiser creates a new event.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@Valid @RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @GetMapping("/admin/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
    //Demo endpoint: view the events for a specific organiser.
    @GetMapping("/organiser/{organiserId}")
    public List<Event> getEventsForOrganiser(
            @PathVariable String organiserId) {
        return eventService.getEventsForOrganiser(organiserId);
    }
    @PatchMapping("/{id}/publish")
    public Event publishEvent(@PathVariable Long id) {
        return eventService.publishEvent(id);
}
}
