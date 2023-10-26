package de.iu.iwmb02_iu_betreuer_app.development;

import java.util.ArrayList;
import java.util.Arrays;

import de.iu.iwmb02_iu_betreuer_app.data.LanguageEnum;
import de.iu.iwmb02_iu_betreuer_app.data.StudyFieldEnum;
import de.iu.iwmb02_iu_betreuer_app.data.StudyProgramEnum;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;

public class SampleDataGenerator {
    private final ArrayList<Student> students;
    private final ArrayList<Supervisor> supervisors;

    SampleDataGenerator(){
        students = new ArrayList<Student>();
        supervisors = new ArrayList<Supervisor>();
        populateLists();
    }

    private void populateLists(){
        //Students
        students.add(new Student("PDFKi7OU2fdNKP0PXtknTIdGYsZ2","Max","Mustermann","B.Sc.","m.mustermann@mail.com", StudyProgramEnum.computerscience.getStringResId(), "M.Sc."));
        students.add(new Student("bN5kMCYX0rZCrdaic69rWFRzzml2","Anna","Schmidt","B.Sc.","a.schmidt@mail.com", StudyProgramEnum.mathematics.getStringResId(), "M.Sc."));
        students.add(new Student("NSqdIbZZ7AMH1tII092KwwjxYSs2","Tom","Becker","","t.becker@mail.com", StudyProgramEnum.history.getStringResId(), "B.A."));

        //Supervisors
        //TODO: reduce img resolution to 200x200
        supervisors.add(new Supervisor(
                "xshcPBn95LbTxxd0Ltb5vj1dXl72",
                "David",
                "Müller",
                "Prof.",
                "d.mueller@mail.com",
                new ArrayList<Integer>(Arrays.asList(StudyFieldEnum.datascience_ai.getStringResId(),StudyFieldEnum.design_media.getStringResId())),
                "profilepictures/profile_d_mueller.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<Integer>(Arrays.asList(LanguageEnum.german.getStringResId(),LanguageEnum.english.getStringResId()))));

        supervisors.add(new Supervisor(
                "zBpj74f5NaOENmIIMAQksKF44cJ2",
                "Michael",
                "Hindley",
                "Prof. Dr.",
                "m.hindley@mail.com",
                new ArrayList<Integer>(Arrays.asList(StudyFieldEnum.business_management.getStringResId(),StudyFieldEnum.personell_law.getStringResId())),
                "profilepictures/profile_m_hindley.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<Integer>(Arrays.asList(LanguageEnum.english.getStringResId()))));

        supervisors.add(new Supervisor(
                "Np7QGaYv1TaCuJy0z2Dq2AHDlVZ2",
                "Magdalena",
                "Schulz",
                "Prof. Dr.",
                "m.schulz@mail.com",
                new ArrayList<Integer>(Arrays.asList(StudyFieldEnum.engineeringsciences.getStringResId(),StudyFieldEnum.planning_controlling.getStringResId())),
                "profilepictures/profile_m_schulz.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<Integer>(Arrays.asList(StudyFieldEnum.business_management.getStringResId(),StudyFieldEnum.personell_law.getStringResId()))));
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Supervisor> getSupervisors() {
        return supervisors;
    }
}
