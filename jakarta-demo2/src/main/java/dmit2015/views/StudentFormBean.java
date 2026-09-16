package dmit2015.views;


import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;

@Named

@ViewScoped

//whenever using viewscoped, you must add implements serializable
public class StudentFormBean implements Serializable {

    private int submissionCount;
    private String fullName;
    private String program;
    private boolean fullTime;

    //our get/set methods
    //the submission count only has a getter. doesn't need a setter
    public int getSubmissionCount() {
        return submissionCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }

    //methods
    public void Submit() {
        // increases increment by 1 each time submit is clicked
        submissionCount ++;

        //facemessage allows us to show custom output to the user through javabeans
        FacesMessage message = new FacesMessage(
                //different types of facemessage but we're gonna use info type. the %s gets replaced with the variables
                FacesMessage.SEVERITY_INFO, "form submitted",
                String.format("welcome %s to %s program (%s)", fullName, program, fullTime ? "fullTime" : "Part-time")
        );

        FacesContext.getCurrentInstance().addMessage(null, message);
        //resets the variables after submitting
        fullName = null;
        program = null;
        fullTime = true;

    }


}
