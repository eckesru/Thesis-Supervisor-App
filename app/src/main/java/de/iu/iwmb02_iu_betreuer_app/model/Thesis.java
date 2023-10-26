package de.iu.iwmb02_iu_betreuer_app.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

public class Thesis {
    @DocumentId
    private String thesisId;
    private String title;
    private String studentId;
    private String primarySupervisorId;
    private String secondarySupervisorId;
    private int thesisState;
    private int billingState;
    private String exposeId;

    public Thesis() {
    }

    public Thesis(String title, String studentId, String primarySupervisorId, String secondarySupervisorId, int thesisState, int billingState, String exposeId) {
        this.title = title;
        this.studentId = studentId;
        this.primarySupervisorId = primarySupervisorId;
        this.secondarySupervisorId = secondarySupervisorId;
        this.thesisState = thesisState;
        this.billingState = billingState;
        this.exposeId = exposeId;
    }

    public String getThesisId() {
        return thesisId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPrimarySupervisorId() {
        return primarySupervisorId;
    }

    public void setPrimarySupervisorId(String primarySupervisorId) {
        this.primarySupervisorId = primarySupervisorId;
    }

    public String getSecondarySupervisorId() {
        return secondarySupervisorId;
    }

    public void setSecondarySupervisorId(String secondarySupervisorId) {
        this.secondarySupervisorId = secondarySupervisorId;
    }

    public int getThesisState() {
        return thesisState;
    }

    public void setThesisState(int thesisState) {
        this.thesisState = thesisState;
    }

    public int getBillingState() {
        return billingState;
    }

    public void setBillingState(int billingState) {
        this.billingState = billingState;
    }

    public String getExposeId() {
        return exposeId;
    }

    public void setExposeId(String exposeId) {
        this.exposeId = exposeId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Thesis{" +
                "thesisId='" + thesisId + '\'' +
                ", title='" + title + '\'' +
                ", studentId='" + studentId + '\'' +
                ", primarySupervisorId='" + primarySupervisorId + '\'' +
                ", secondarySupervisorId='" + secondarySupervisorId + '\'' +
                ", thesisState=" + thesisState +
                ", billingState=" + billingState +
                ", exposeId='" + exposeId + '\'' +
                '}';
    }
}


