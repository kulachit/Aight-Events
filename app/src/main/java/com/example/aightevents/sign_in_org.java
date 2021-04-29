package com.example.aightevents;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncorti.slidetoact.SlideToActView;

public class sign_in_org extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_org);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        SlideToActView sta = (SlideToActView) findViewById(R.id.swipeSI);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                emailText = findViewById(R.id.emailOrg);
                passwordText = findViewById(R.id.passwordOrg);
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                int flag = checkForInput(email, password);
                if (flag == 1) {
                    slideToActView.resetSlider();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int checkForInput(String email, String password) {
        int error = 0;
        if (email.isEmpty()) {
            error = 1;
            emailText.setBackground(getDrawable(R.drawable.red_round_border));
            emailText.setHint("Please enter email ID");
            emailText.setHintTextColor(Color.RED);
        } else {
            emailText.setBackground(getDrawable(R.drawable.white_round_border));
            emailText.setHint("Email");
            emailText.setHintTextColor(Color.WHITE);
        }

        if (password.isEmpty()) {
            error = 1;
            passwordText.setBackground(getDrawable(R.drawable.red_round_border));
            passwordText.setHint("Please Enter Password");
            passwordText.setHintTextColor(Color.RED);


        } else {
            passwordText.setBackground(getDrawable(R.drawable.white_round_border));
            passwordText.setHint("Password");
            passwordText.setHintTextColor(Color.WHITE);

        }
        if (error == 0) {
            login(email, password);
        }
        return error;
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(this, new OnFailureListener() {
                    public void onFailure(@NonNull Exception e) {
                        SlideToActView sta = (SlideToActView) findViewById(R.id.swipeSI);
                        notifyUser(e.getLocalizedMessage());
                        sta.resetSlider();
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUIFirebase(user);
                        }

                        // ...
                    }
                });

    }

    public void backToHome(View backHome) {

        startActivity(new Intent(this, StartPage.class));
        finish();
    }

    public void notifyUser(String error) {
        Toast toast = Toast.makeText(getApplicationContext(),
                error,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    public void updateUIFirebase(FirebaseUser user) {

        if (user == null) {
            //error stuff
        } else {
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
        FirebaseUser org = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("organizer").document(org.getEmail()).update("userID",org.getUid());

        startActivity(new Intent(this, organizer_page.class));
        finish();
    }

    public void user() {
        startActivity(new Intent(this, StartPage.class));
        finish();
    }
}