package de.iu.iwmb02_iu_betreuer_app.data.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.iu.iwmb02_iu_betreuer_app.data.ThesisStateEnum;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class FirebaseFirestoreDao implements StudentDao, SupervisorDao, ThesisDao, UserDao{
    private static FirebaseFirestoreDao instance;
    private static final String TAG = "FirebaseFirestoreDao";

    private final CollectionReference studentsCollectionRef;
    private final CollectionReference supervisorsCollectionRef;
    private final CollectionReference thesesCollectionRef;
    private final CollectionReference usersCollectionRef;


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
        usersCollectionRef = firestore.collection("users");
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

                //save document reference in user collection
                DocumentReference studentRef = studentsCollectionRef.document(studentId);
                Map<String, DocumentReference> docData = new HashMap<>();
                docData.put("userRef",studentRef);
                usersCollectionRef.document(studentId).set(docData);
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

                //save document reference in user collection
                DocumentReference supervisorRef = supervisorsCollectionRef.document(supervisorId);
                Map<String, DocumentReference> docData = new HashMap<>();
                docData.put("userRef",supervisorRef);
                usersCollectionRef.document(supervisorId).set(docData);
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

    @Override
    public void checkIfOpenThesisExistsForStudentId(String studentId, Callback<Thesis> callback) {
        thesesCollectionRef
                .whereEqualTo("studentId", studentId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        for(DocumentSnapshot ds : documents){
                            Thesis thesis = ds.toObject(Thesis.class);
                            String state = thesis.getThesisState();
                            if(state != ThesisStateEnum.canceled.name() && state != ThesisStateEnum.completed.name()){
                                callback.onCallback(thesis);
                                return;
                            }
                        }
                    }
                    callback.onCallback(null);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Thesis query failed", e);
                    callback.onCallback(null);
                });
    }

    @Override
    public void getUser(String userId, Callback<User> callback) {
        usersCollectionRef.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DocumentReference userRef = (DocumentReference) documentSnapshot.get("userRef");
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Log.d(TAG, "User data: " + documentSnapshot.getData());
                            DocumentReference docRef = documentSnapshot.getReference();
                            User user = null;
                            if(docRef.getPath().startsWith("student")){
                                user = documentSnapshot.toObject(Student.class);
                            } else if (docRef.getPath().startsWith("supervisor")) {
                                user = documentSnapshot.toObject(Supervisor.class);
                            }
                            callback.onCallback(user);
                        }else{
                            Log.d(TAG, "No user found with ID: " + userId);
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "No user with Id: " + userId, e);
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
