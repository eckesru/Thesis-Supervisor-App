package de.iu.iwmb02_iu_betreuer_app.data.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class FirebaseFirestoreDao implements StudentDao, SupervisorDao, ThesisDao{
    private static FirebaseFirestoreDao instance;
    private static final String TAG = "FirebaseFirestoreDao";

    private final CollectionReference studentsCollectionRef;
    private final CollectionReference supervisorsCollectionRef;
    private final CollectionReference thesesCollectionRef;


    public static FirebaseFirestoreDao getInstance() {
        if (instance == null) {
            instance = new FirebaseFirestoreDao();
        }
        return instance;
    }

    private FirebaseFirestoreDao() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        studentsCollectionRef = firestore.collection("students");
        supervisorsCollectionRef = firestore.collection("supervisors");
        thesesCollectionRef = firestore.collection("theses");
    }

    @Override
    public void getStudent(String studentId, Callback<Student> callback) {
        studentsCollectionRef.document(studentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "Student data: "+documentSnapshot.getData());
                    Student student = documentSnapshot.toObject(Student.class)  ;
                    callback.onCallback(student);
                }else{
                    Log.d(TAG, "No student found with ID: " + studentId);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "getStudent failed with ", e);
            }
        });
    }

    @Override
    public void saveStudent(String studentId, Student student) {
        studentsCollectionRef.document(studentId).set(student).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Student "+studentId+" successfully saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error updating supervisor", e);
            }
        });
    }

    @Override
    public void getSupervisor(String supervisorId, Callback<Supervisor> callback) {
        supervisorsCollectionRef.document(supervisorId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "Supervisor data: "+documentSnapshot.getData());
                    Supervisor supervisor = documentSnapshot.toObject(Supervisor.class)  ;
                    callback.onCallback(supervisor);
                }else{
                    Log.d(TAG, "No supervisor found with ID: " + supervisorId);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "getSupervisor failed with ", e);
            }
        });
    }

    @Override
    public void saveSupervisor(String supervisorId, Supervisor supervisor) {
        supervisorsCollectionRef.document(supervisorId).set(supervisor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Supervisor "+supervisorId+" successfully saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error updating supervisor", e);
            }
        });
    }

    @Override
    public void getThesis(String thesisId, Callback<Thesis> callback) {
        thesesCollectionRef.document(thesisId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "Thesis data: "+documentSnapshot.getData());
                    Thesis thesis= documentSnapshot.toObject(Thesis.class)  ;
                    callback.onCallback(thesis);
                }else{
                    Log.d(TAG, "No thesis found with ID: " + thesisId);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "getThesis failed with ", e);
            }
        });

    }

    @Override
    public void saveNewThesis(Thesis thesis) {
        thesesCollectionRef.add(thesis).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Thesis added with ID: "+ documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error adding thesis", e);
            }
        });
    }

    @Override
    public void updateThesis(String thesisId, Thesis thesis) {
        thesesCollectionRef.document(thesisId).set(thesis).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Thesis "+thesisId+" successfully updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error updating supervisor", e);
            }
        });
    }

    public CollectionReference getStudentsCollectionRef() {
        return studentsCollectionRef;
    }

    public CollectionReference getSupervisorsCollectionRef() {
        return supervisorsCollectionRef;
    }

    public CollectionReference getThesesCollectionRef() {
        return thesesCollectionRef;
    }
}
