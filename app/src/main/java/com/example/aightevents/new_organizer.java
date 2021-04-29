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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class new_organizer extends AppCompatActivity {

    EditText name;
    EditText number;
    EditText email;
    EditText college;
    Organizer organizer = new Organizer();
    private FirebaseAuth mAuth;
    FirebaseUser orgUser;

    private com.google.android.gms.auth.api.signin.GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_organizer);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void logOut(View logout){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }

                });

        startActivity(new Intent(this,StartPage.class));
        finish();
    }

    public void updateUI (GoogleSignInAccount gsia){
        if(gsia == null){
            startActivity(new Intent(this, StartPage.class));
            finish();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "couldn't logout",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,100);
            toast.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createOrganizer(View view){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        name = findViewById(R.id.orgName);
        number = findViewById(R.id.orgPhone);
        email = findViewById(R.id.orgEmail);
        college = findViewById(R.id.orgCollege);
        String nameText = name.getText().toString();
        String numberText = number.getText().toString();
        String emailText = email.getText().toString();
        String collegeText = college.getText().toString();
        int flag = fieldCheck(nameText, numberText, emailText, collegeText);
        if(flag == 0) {
            createNewOrganizer(emailText);

                organizer.setName(nameText);
                organizer.setNumber(numberText);
                organizer.setEmail(emailText);
                organizer.setCollege(collegeText);
                organizer.setUserID("not set yet");

                Map<String, Object> map = new HashMap<>();
                map.put("college", collegeText);
                map.put("college", emailText);
                map.put("college", nameText);
                map.put("college", numberText);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("organizer").document(emailText)
                    .set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Event added successfully",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 10);
                            toast.show();
                            startActivity(new Intent(new_organizer.this,admin_page.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error", "Error adding document", e);
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Event not added, retry",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 10);
                            toast.show();
                        }
                    });
            }
    }

    public void createNewOrganizer(String email){
        mAuth.createUserWithEmailAndPassword(email, "123456")
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("error", "can't create user with given email");
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "createUserWithEmail:success");
                        }

                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int fieldCheck(String nameText, String numberText, String emailText, String collegeText){
        int error = 0;
        if(nameText.isEmpty()){
            error = 1;
            name.setBackground(getDrawable(R.drawable.red_round_border));
            name.setHint("Enter organizer name");
            name.setHintTextColor(Color.RED);
        }
        else{
            name.setBackground(getDrawable(R.drawable.white_round_border));
            name.setHint("Organizer name");
            name.setHintTextColor(Color.GRAY);
        }

        if(numberText.isEmpty()){
            error = 1;
            number.setBackground(getDrawable(R.drawable.red_round_border));
            number.setHint("Enter phone number");
            number.setHintTextColor(Color.RED);
        }
        else{
            number.setBackground(getDrawable(R.drawable.white_round_border));
            number.setHint("Phone number");
            number.setHintTextColor(Color.GRAY);
        }

        if(emailText.isEmpty()){
            error = 1;
            email.setBackground(getDrawable(R.drawable.red_round_border));
            email.setHint("Enter email ID");
            email.setHintTextColor(Color.RED);
        }
        else{
            email.setBackground(getDrawable(R.drawable.white_round_border));
            email.setHint("Email ID)");
            email.setHintTextColor(Color.GRAY);
        }

        if(collegeText.isEmpty()){
            error = 1;
            college.setBackground(getDrawable(R.drawable.red_round_border));
            college.setHint("Enter college name");
            college.setHintTextColor(Color.RED);
        }
        else{
            college.setBackground(getDrawable(R.drawable.white_round_border));
            college.setHint("College name");
            college.setHintTextColor(Color.GRAY);
        }
        return error;
    }

    public void backToHome(View backHome) {
        startActivity(new Intent(this,admin_page.class));
        finish();
    }
}