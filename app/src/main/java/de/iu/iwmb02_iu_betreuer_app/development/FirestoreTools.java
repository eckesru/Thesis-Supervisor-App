package de.iu.iwmb02_iu_betreuer_app.development;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;

public class FirestoreTools implements DatabaseTools {
    private static final String TAG = "FirestoreTools";

    private final CollectionReference studentsCollection;
    private final CollectionReference supervisorsCollection;
    private final CollectionReference thesesCollection;
    private final FirebaseFirestoreDao firebaseFirestoreDao;
    private final SampleDataGenerator sampleDataGenerator;

    public FirestoreTools() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        studentsCollection = firestore.collection("students");
        supervisorsCollection = firestore.collection("supervisors");
        thesesCollection = firestore.collection("theses");
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();
        sampleDataGenerator = new SampleDataGenerator();
    }

    @Override
    public void deleteStudentsDB() {
        studentsCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
                    document.getReference().delete();
                    Log.d(TAG, "StudentDB deleted");
                }
            }
        });
    }

    @Override
    public void deleteSupervisorDB() {
        supervisorsCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
                    document.getReference().delete();
                    Log.d(TAG, "SupervisorDB deleted");
                }
            }
        });
    }

    @Override
    public void deleteThesesDB() {
        thesesCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
                    document.getReference().delete();
                    Log.d(TAG, "ThesesDB deleted");
                }
            }
        });

    }

    @Override
    public void deleteAllDBs() {
        deleteStudentsDB();
        deleteSupervisorDB();
        deleteThesesDB();
    }

    @Override
    public void populateDatabasesWithSampleData() {
        ArrayList<Student> students = sampleDataGenerator.getStudents();
        for (Student student: students) {
            firebaseFirestoreDao.saveNewStudent(student);
        }

        ArrayList<Supervisor> supervisors = sampleDataGenerator.getSupervisors();
        for (Supervisor supervisor: supervisors) {
            firebaseFirestoreDao.saveNewSupervisor(supervisor);
        }
    }

}
