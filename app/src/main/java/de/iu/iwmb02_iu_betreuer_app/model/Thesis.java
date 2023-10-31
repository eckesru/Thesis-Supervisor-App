package de.iu.iwmb02_iu_betreuer_app.model;


import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Thesis implements Serializable {
    @DocumentId
    private String thesisId;
    private String title;
    private String studentId;
    private String primarySupervisorId;
    private String secondarySupervisorId;
    private String thesisState;
    private String billingState;
    private String exposeTitle;
    private String exposeDownloadUri;

    public Thesis() {
    }

    public Thesis(String title, String studentId, String primarySupervisorId, String secondarySupervisorId, String thesisState, String billingState,String exposeTitle, String exposeDownloadUri) {
        this.title = title;
        this.studentId = studentId;
        this.primarySupervisorId = primarySupervisorId;
        this.secondarySupervisorId = secondarySupervisorId;
        this.thesisState = thesisState;
        this.billingState = billingState;
        this.exposeTitle = exposeTitle;
        this.exposeDownloadUri = exposeDownloadUri;
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

    public String getThesisState() {
        return thesisState;
    }

    public void setThesisState(String thesisState) {
        this.thesisState = thesisState;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getExposeTitle() {
        return exposeTitle;
    }

    public String getExposeDownloadUri() {
        return exposeDownloadUri;
    }

    public void setExposeId(String exposeId) {
        this.exposeDownloadUri = exposeId;
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
                ", exposeDownloadUri='" + exposeDownloadUri + '\'' +
                '}';
    }
}


