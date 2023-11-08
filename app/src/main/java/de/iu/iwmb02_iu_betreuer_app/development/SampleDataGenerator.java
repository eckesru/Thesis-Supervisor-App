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
        students.add(new Student("jMMkdmuAgQWG99lNb17ONc41RhC2","Christina","Smith","B.A.","c.smith@mail.com", StudyProgramEnum.architecture.name(), "M.A."));
        students.add(new Student("1rDBhmJ1ThRtH6r1Fn4KsGNEmdw2","Laura","Egger","","l.egger@mail.com", StudyProgramEnum.marketing.name(), "B.A."));
        students.add(new Student("nl0PCZ6THMNSw2nTLlcs9gGe0ji2","Guido","Benesch","","g.benesch@mail.com", StudyProgramEnum.psychology.name(), "B.Sc."));

        //Supervisors
        supervisors.add(new Supervisor(
                "xshcPBn95LbTxxd0Ltb5vj1dXl72",
                "David",
                "Müller",
                "Prof.",
                "d.mueller@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.datascience_ai.name(),StudyFieldEnum.design_media.name())),
                "profilepictures/profile_d_mueller.jpg",
                "Professor David Müller is an expert in the fields of Data Science & AI and Design & Media. He joined IU on Mai 1, 2019.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.german.name(),LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "XzfMpNqjJ5VGTBgw9CdflyZmVMO2",
                "Hiroshi",
                "Tanaka",
                "Prof. Dr.",
                "h.tanaka@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.computersciences_softwaredevelopment.name(),StudyFieldEnum.business_management.name())),
                "profilepictures/profile_h_tanaka.jpg",
                "Professor Dr. Hiroshi Tanaka is an expert in Computer Sciences & Software Development and Business Management. He became a part of IU on September 5, 2015.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "PZV978pSyYVaSGsvuy1OUWsolih2",
                "Isabella",
                "Rossi",
                "Prof.",
                "i.rossi@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.projectmanagement.name(),StudyFieldEnum.marketing_communication.name(),StudyFieldEnum.personell_law.name())),
                "profilepictures/profile_i_rossi.jpg",
                "Professor Isabella Rossi excels in Project Management, Marketing & Communication, and Personnel Law. She commenced her journey at IU on Juni 30, 2018.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.german.name(),LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "Np7QGaYv1TaCuJy0z2Dq2AHDlVZ2",
                "Magdalena",
                "Schulz",
                "Prof. Dr.",
                "m.schulz@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.engineeringsciences.name(),StudyFieldEnum.planning_controlling.name())),
                "profilepictures/profile_m_schulz.jpg",
                "Professor Dr. Magdalena Schulz is a renowned scholar in Engineering Sciences and Planning & Controlling. She started her journey at IU on April 20, 2014.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.german.name(),LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "fjwRlCtFaNWFpCxfKtklOz31DKx2",
                "Maria",
                "Santos",
                "Prof. Dr.",
                "m.santos@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.architecture.name(),StudyFieldEnum.personell_law.name())),
                "profilepictures/profile_m_santos.jpg",
                "Professor Dr. Maria Santos specializes in Architecture and Personnel Law. She joined IU on Juni 2, 2017.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "zBpj74f5NaOENmIIMAQksKF44cJ2",
                "Michael",
                "Hindley",
                "Prof. Dr.",
                "m.hindley@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.business_management.name(),StudyFieldEnum.personell_law.name())),
                "profilepictures/profile_m_hindley.jpg",
                "Professor Dr. Michael Hindley specializes in Business Management and Personnel Law. He became a part of IU on August 15, 2016.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "ZWj6EtE2Nrbj9brQnGijtxCOsRP2",
                "Theresa",
                "Huber",
                "Prof. Dr.",
                "t.huber@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.business_management.name(),StudyFieldEnum.planning_controlling.name())),
                "profilepictures/profile_t_huber.jpg",
                "Professor Dr. Theresa Huber is a distinguished scholar in Business Management and Planning & Controlling. She began her academic journey at IU on July 18, 2017.",
                new ArrayList<String>(Arrays.asList(LanguageEnum.german.name(),LanguageEnum.english.name()))));

        supervisors.add(new Supervisor(
                "DEkYJGQFGCMSpjLTjNoLpDb6kJp2",
                "Ahmed",
                "Mansour",
                "Prof. Dr.",
                "a.mansour@mail.com",
                new ArrayList<String>(Arrays.asList(StudyFieldEnum.engineeringsciences.name(),StudyFieldEnum.projectmanagement.name())),
                "profilepictures/profile_a_mansour.jpg",
                "Professor Dr. Ahmed Mansour is an authority in Engineering Sciences and Project Management. He joined IU on August 7, 2014",
                new ArrayList<String>(Arrays.asList(LanguageEnum.german.name(),LanguageEnum.english.name()))));
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Supervisor> getSupervisors() {
        return supervisors;
    }
}
