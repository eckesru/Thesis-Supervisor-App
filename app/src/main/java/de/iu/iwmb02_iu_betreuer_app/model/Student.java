package de.iu.iwmb02_iu_betreuer_app.model;

import androidx.annotation.NonNull;

public class Student extends User{
    private Integer studyProgram;
    private String studyLevel;

    public Student() {
        //needed for Firebase Firestore
    }

    public Student(String studentId, String nameFirst, String nameLast, String nameTitle, String email, int studyProgram, String studyLevel) {
        super(studentId, nameFirst, nameLast, nameTitle, email);
        this.studyProgram = studyProgram;
        this.studyLevel = studyLevel;
    }

    public int getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(int studyProgram) {
        this.studyProgram = studyProgram;
    }

    public String getStudyLevel() {
        return studyLevel;
    }

    public void setStudyLevel(String studyLevel) {
        this.studyLevel = studyLevel;
    }

    @NonNull
    @Override
    public String toString() {
        return "Student{" +
                " userId ='"+ getUserId() + '\'' +
                ", nameTitle='" + getNameTitle() + '\'' +
                ", nameFirst='" + getNameFirst() + '\'' +
                ", nameLast='" + getNameLast() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", studyProgram='" + studyProgram + '\'' +
                ", studyLevel='" + studyLevel + '\'' +
                '}';
    }
}
