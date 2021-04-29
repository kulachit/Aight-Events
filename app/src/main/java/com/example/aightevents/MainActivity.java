package com.example.aightevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUIFirebase(currentUser);
    }

    public void updateUIFirebase(FirebaseUser user) {

        if(user == null){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this,StartPage.class));
                    finish();
                }
            },3000);
        }
        else {
            final Boolean[] API1 = {false};
            final Boolean[] API2 = {false};
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection("admin").document(user.getEmail());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            admin();
                            return;
                        }
                        else {
                            API1[0] = true;
                            if (API1[0] && API2[0]) {
                                user();
                            }
                        }
                    }
                }
            });
            DocumentReference refOrg = db.collection("organizer").document(user.getEmail());
            refOrg.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            organizer();
                            return;
                        }
                        else {
                            API2[0] = true;
                            if (API1[0] && API2[0]) {
                                user();
                            }
                        }
                    }
                }
            });
        }
    }

    public void admin() {
        startActivity(new Intent(this, admin_page.class));
        finish();
    }

    public void organizer() {
        startActivity(new Intent(this, organizer_page.class));
        finish();
    }

    public void user() {
        //startActivity(new Intent(this, home_page.class));
        startActivity(new Intent(this, participant_page.class));
        finish();
    }
}