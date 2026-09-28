package model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CampusEvent implements Serializable {

    //our variables with validation
    private int eventId;

    @NotBlank(message = "Event name cannot be blank")
    @Size(min = 5, message = "event name must at least be {min} letters long")
    private String eventName;

    @NotBlank(message = "Organizer name cannot be blank")
    private String organizerName;

    private LocalDate eventDate;

    @Min(value = 1, message = "capacity must be greater than 0")
    private int capacity;

    // our default constructor
    public CampusEvent() {
    }

    // parameterized constructor
    public CampusEvent(int eventId, String eventName, String organizerName,
                       LocalDate eventDate, int capacity) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.capacity = capacity;
    }

    // the computed field
    public String getEventSummary() {
        return eventName + " - " + eventDate;
    }
}