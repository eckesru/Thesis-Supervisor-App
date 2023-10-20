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

public class FirestoreDao implements StudentDao, SupervisorDao, ThesisDao{
    private static final String TAG = "FirestoreDao";

    private final CollectionReference studentsCollection;
    private final CollectionReference supervisorsCollection;
    private final CollectionReference thesesCollection;

    public FirestoreDao() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        studentsCollection = firestore.collection("students");
        supervisorsCollection = firestore.collection("supervisors");
        thesesCollection = firestore.collection("theses");
    }

    @Override
    public void getStudent(String studentId) {
        studentsCollection.document(studentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "Student data: "+documentSnapshot.getData());
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
    public void saveStudent(Student student) {
       studentsCollection.add(student).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
           @Override
           public void onSuccess(DocumentReference documentReference) {
               Log.d(TAG, "Student added with ID: "+ documentReference.getId());
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Log.e(TAG, "Error adding student", e);
           }
       });
    }

    @Override
    public void getSupervisor(String supervisorId) {
        supervisorsCollection.document(supervisorId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "Supervisor data: "+documentSnapshot.getData());
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
    public void saveSupervisor(Supervisor supervisor) {
        supervisorsCollection.add(supervisor).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Supervisor added with ID: "+ documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error adding supervisor", e);
            }
        });
    }

    @Override
    public void updateSupervisor(String supervisorId, Supervisor supervisor) {
        supervisorsCollection.document(supervisorId).set(supervisor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Thesis "+supervisorId+" successfully updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error updating supervisor", e);
            }
        });
    }

    @Override
    public void getThesis(String thesisId) {
        thesesCollection.document(thesisId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "Thesis data: "+documentSnapshot.getData());
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
    public void saveThesis(Thesis thesis) {
        thesesCollection.add(thesis).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
        thesesCollection.document(thesisId).set(thesis).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}
