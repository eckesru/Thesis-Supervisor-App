package de.iu.iwmb02_iu_betreuer_app.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Supervisor extends User{
    private ArrayList<String> studyFields;
    private String profilePictureUrl;
    private String profileDescription;
    private ArrayList<String> languages;

    public Supervisor() {
        // needed for Firebase Firestore
    }

    public Supervisor(String supervisorId, String nameFirst, String nameLast, String nameTitle, String email, ArrayList<String> studyFields,String profilePictureUrl, String profileDescription, ArrayList<String> languages) {
        super(supervisorId, nameFirst, nameLast, nameTitle, email);
        this.studyFields = studyFields;
        this.profilePictureUrl = profilePictureUrl;
        this.profileDescription = profileDescription;
        this.languages = languages;
    }

    public ArrayList<String> getStudyFields() {
        return studyFields;
    }

    public void setStudyFields(ArrayList<String> studyFields) {
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

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public String getFullName() {
        String title = getNameTitle();
        String first = getNameFirst();
        String last = getNameLast();

        return (title.isEmpty() ? "" : title + " ") + first + " " + last;
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
