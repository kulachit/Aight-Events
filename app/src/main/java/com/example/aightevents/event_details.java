package com.example.aightevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


public class event_details extends AppCompatActivity {
    TextView textViewCollege;
    TextView textViewName;
    TextView textViewDescription;
    TextView textViewDate;
    TextView textViewCategory;
    ImageView imageViewImage;
    Event event;

    private FirebaseAuth mAuth;
    private com.google.android.gms.auth.api.signin.GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getSupportActionBar().hide();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        event = (Event) getIntent().getSerializableExtra("Event");

        textViewCollege = findViewById(R.id.collegeFilter);
        textViewName = findViewById(R.id.eventName);
        textViewDescription = findViewById(R.id.description);
        textViewDate = findViewById(R.id.date);
        textViewCategory = findViewById(R.id.categoryFilter);
        imageViewImage = findViewById(R.id.imageDetails);


        textViewCollege.setText(event.getCollege());
        textViewName.setText(event.getEventName());
        textViewDescription.setText(event.getDescription());
        textViewDate.setText(event.getDate());
        textViewCategory.setText(event.getEventType());
        Picasso.get()
                .load(event.getImageLink())
                .fit()
                .centerCrop()
                .into(imageViewImage);
    }

    public void backToHome(View backHome) {
        finish();
    }

    public void openLink(View openlink){
        try{
            Uri uri = Uri.parse(event.getLink()); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        catch (Exception e){
            try{
                Uri uri = Uri.parse("http://" + event.getLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            catch (Exception e1){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Invalid link",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,100);
                toast.show();
            }
        }
    }
}