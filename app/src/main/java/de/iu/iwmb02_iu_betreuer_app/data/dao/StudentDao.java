package de.iu.iwmb02_iu_betreuer_app.data.dao;

import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public interface StudentDao {
    void getStudent(String studentId, Callback<Student> callback);
    void saveNewStudent(Student student);
}
