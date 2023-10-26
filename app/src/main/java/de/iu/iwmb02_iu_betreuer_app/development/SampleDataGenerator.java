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
        students.add(new Student("PDFKi7OU2fdNKP0PXtknTIdGYsZ2","Max","Mustermann","B.Sc.","m.mustermann@mail.com", StudyProgramEnum.computerscience.name(), "M.Sc."));
        students.add(new Student("bN5kMCYX0rZCrdaic69rWFRzzml2","Anna","Schmidt","B.Sc.","a.schmidt@mail.com", StudyProgramEnum.mathematics.name(), "M.Sc."));
        students.add(new Student("NSqdIbZZ7AMH1tII092KwwjxYSs2","Tom","Becker","","t.becker@mail.com", StudyProgramEnum.history.name(), "B.A."));

        //Supervisors
        //TODO: reduce img resolution to 200x200
        supervisors.add(new Supervisor(
                "xshcPBn95LbTxxd0Ltb5vj1dXl72",
                "David",
                "Müller",
                "Prof.",
                "d.mueller@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.datascience_ai.name(),StudyFieldEnum.design_media.name())),
                "profilepictures/profile_d_mueller.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.german.name(),LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "zBpj74f5NaOENmIIMAQksKF44cJ2",
                "Michael",
                "Hindley",
                "Prof. Dr.",
                "m.hindley@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.business_management.name(),StudyFieldEnum.personell_law.name())),
                "profilepictures/profile_m_hindley.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "Np7QGaYv1TaCuJy0z2Dq2AHDlVZ2",
                "Magdalena",
                "Schulz",
                "Prof. Dr.",
                "m.schulz@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.engineeringsciences.name(),StudyFieldEnum.planning_controlling.name())),
                "profilepictures/profile_m_schulz.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.german.name(),LanguageEnum.english.name()))));
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Supervisor> getSupervisors() {
        return supervisors;
    }
}
