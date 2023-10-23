package de.iu.iwmb02_iu_betreuer_app.development;

import java.util.ArrayList;
import java.util.Arrays;

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
        students.add(new Student("k4gOSldMysb1wYgfRwckQ8wduX92","Max","Mustermann","B.Sc.","m.mustermann@mail.com", "Informatik", "M.Sc."));
        students.add(new Student("br0IbQqJ88RkGKb3XfLNsQpCBOC3","Anna","Schmidt","B.Sc.","a.schmidt@mail.com", "Mathematik", "M.Sc."));
        students.add(new Student("qHAf5dqRnAR8QWRhk2sXJWtUVBs1","Tom","Becker","","t.becker@mail.com", "Geschichte", "B.A."));

        //Supervisors
        supervisors.add(new Supervisor(
                "vMaLqkIwmRVYynBn6QsgK5xA41E2",
                "David",
                "Müller",
                "Prof.",
                "d.mueller@mail.com",
                new ArrayList<String>(Arrays.asList("Data Science & Articifial Intelligence","Design & Medien")),
                "profilepictures/supervisor_m2.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<String>(Arrays.asList("Deutsch","Englisch"))));

        supervisors.add(new Supervisor(
                "Fx8Sls2yl9QZmr37Z5GuEfaBjKo2",
                "Michael",
                "Hindley",
                "Prof. Dr.",
                "m.hindley@mail.com",
                new ArrayList<String>(Arrays.asList("Betriebswirtschaft & Management","Personal & Recht")),
                "profilepictures/supervisor_m1.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<String>(Arrays.asList("Englisch"))));

        supervisors.add(new Supervisor(
                "hD81mfggRmO21RqXz6M6BGeyKK23",
                "Magdalena",
                "Schulz",
                "Prof. Dr.",
                "m.schulz@mail.com",
                new ArrayList<String>(Arrays.asList("Ingenieurswissenschaften","Planung & Controlling")),
                "profilepictures/supervisor_f1.jpg",
                "Dies ist eine Profilbeschreibung.",
                new ArrayList<String>(Arrays.asList("Deutsch","Englisch"))));
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Supervisor> getSupervisors() {
        return supervisors;
    }
}
