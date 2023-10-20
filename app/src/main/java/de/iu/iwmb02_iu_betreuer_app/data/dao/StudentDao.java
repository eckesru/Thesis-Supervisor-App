package de.iu.iwmb02_iu_betreuer_app.data.dao;

import de.iu.iwmb02_iu_betreuer_app.model.Student;

public interface StudentDao {
    void getStudent(String studentId);
    void saveNewStudent(Student student);
}
