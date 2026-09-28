package beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.inject.Named;
import model.CampusEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("campusEventBean")
@SessionScoped
public class CampusEventBean implements Serializable {


    private List<CampusEvent> eventList = new ArrayList<>();

    // our event id starts as 2601 as per assignment
    private int nextEventId = 2601;

    // bound to the formfields in createvent.xhtml
    private CampusEvent newEvent = new CampusEvent();

    public List<CampusEvent> getEventList() {
        return eventList;
    }

    public CampusEvent getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(CampusEvent newEvent) {
        this.newEvent = newEvent;
    }


    // the createEvent is called once every field is validatedd in the form.
    //creates the event, increases the iD for the nextevent, adds it to our list, and more
    public String createEvent() {
        newEvent.setEventId(nextEventId);
        nextEventId++;

        eventList.add(newEvent);

        // // our success message. using flash so it persists through the redirect
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "success",
                        "Event " + newEvent.getEventName() + " has been created"));

        // rsets the form so eveything is blank again
        newEvent = new CampusEvent();

        return "view-events?faces-redirect=true";
    }
}