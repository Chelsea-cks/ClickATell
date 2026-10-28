
package za.co.communityevents.communityeventsplatform;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Event description is required")
    @Column(nullable = false, length = 2000)
    private String description;

    @NotNull(message = "Event date and time are required")
    @Future(message = "Event date and time must be in the future")
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @NotNull(message = "Event capacity is required")
    @Positive(message = "Event capacity must be greater than zero")
    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String status = "PENDING_REVIEW";

    @NotBlank(message = "Organiser identifier is required")
    @Column(nullable = false)
    private String organiserId;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(String organiserId) {
        this.organiserId = organiserId;
    }
}
