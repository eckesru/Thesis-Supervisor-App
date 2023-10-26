package de.iu.iwmb02_iu_betreuer_app.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Supervisor extends User{
    private ArrayList<Integer> studyFields;
    private String profilePictureUrl;
    private String profileDescription;
    private ArrayList<Integer> languages;

    public Supervisor() {
        // needed for Firebase Firestore
    }

    public Supervisor(String supervisorId, String nameFirst, String nameLast, String nameTitle, String email, ArrayList<Integer> studyFields,String profilePictureUrl, String profileDescription, ArrayList<Integer> languages) {
        super(supervisorId, nameFirst, nameLast, nameTitle, email);
        this.studyFields = studyFields;
        this.profilePictureUrl = profilePictureUrl;
        this.profileDescription = profileDescription;
        this.languages = languages;
    }

    public ArrayList<Integer> getStudyFields() {
        return studyFields;
    }

    public void setStudyFields(ArrayList<Integer> studyFields) {
        this.studyFields = studyFields;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public ArrayList<Integer> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<Integer> languages) {
        this.languages = languages;
    }


    @NonNull
    @Override
    public String toString() {
        return "Supervisor{" +
                " userId ='"+ getUserId() + '\'' +
                ", nameTitle='" + getNameTitle() + '\'' +
                ", nameFirst='" + getNameFirst() + '\'' +
                ", nameLast='" + getNameLast() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", studyFields=" + studyFields +
                ", profileDescription='" + profileDescription + '\'' +
                ", languages=" + languages +
                '}';
    }
}
