package com.example.aightevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class new_event extends AppCompatActivity {

    Event event = new Event();
    PowerSpinnerView eventType;
    ImageView imageToUpload;
    TextView college;
    TextView imageHint;
    TextView eventName;
    TextView description;
    TextView date;
    TextView link;
    String dateStr;
    String eventTypeText;
    Uri selectedImage;
    String imageUrl;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("images");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_post);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        eventType = findViewById(R.id.eventType);
        eventType.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                eventTypeText = newItem;
            }
        });
    }

    public void backToHome(View backHome) {
        startActivity(new Intent(this, organizer_page.class));
        finish();
    }

    public void uploadImage(View uploadimage){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createEvent(View createevent) throws ParseException {
        addEvent();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addEvent() throws ParseException {
//        eventType = findViewById(R.id.eventType);

        college = findViewById(R.id.collegeFilter);
        eventName = findViewById(R.id.eventName);
        description = findViewById(R.id.umOkay);
        date = findViewById(R.id.date);
        link = findViewById(R.id.link);
        String eventNameText = eventName.getText().toString();
        String descriptionText = description.getText().toString();
        String collegeText = college.getText().toString();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.

        Date dateObject;

        String time;
        String dob_var = "";
        try{
            dob_var=(date.getText().toString());

            dateObject = formatter.parse(dob_var);

            dateStr = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
            //time = new SimpleDateFormat("h:mmaa").format(dateObject);
        }

        catch (java.text.ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("E11111111111", e.toString());
        }

        Toast.makeText(getBaseContext(), dateStr, Toast.LENGTH_LONG).show();
        String linkText = link.getText().toString();
        int flag = checkForInput(eventNameText, descriptionText, dob_var, linkText, collegeText);
        if(flag == 0) {
            event.setEventName(eventNameText);
            event.setDescription(descriptionText);
            event.setDate(dateStr);
            event.setLink(linkText);
            event.setEventType(eventTypeText);
            event.setCollege(collegeText);
            event.setDateOfCreation((int) System.currentTimeMillis());
            FirebaseUser currentUser = mAuth.getCurrentUser();
            event.setUserID(currentUser.getUid());
            String myDate = dateStr;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(myDate);
            long millis = date.getTime();
            event.setEventDeadline(millis);
            StorageReference pathReference = storageRef.child(".jpg");

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            try {
                event.setImageLink(imageUrl);

            }
            catch (Exception e){
                event.setImageLink("no image");
            }

            Map<String, Object> collegeDetails = new HashMap<>();
            collegeDetails.put("collegeName", event.getCollege());

            db.collection("post")
                    .add(event)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("success", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Event added successfully",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 10);
                            toast.show();
                            eventAdded();
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
            db.collection("college")
                    .add(collegeDetails)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("success", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "College added successfully",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 10);
                            toast.show();
                            eventAdded();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error", "Error adding document", e);
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "College not added, retry",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 10);
                            toast.show();
                        }
                    });
        }
    }

    public void eventAdded(){
        Upload image = new Upload();
        image.setmImageUrl(imageUrl);
        image.setName(event.getImageName());
        mDatabase.child(event.getDateOfCreation().toString()).setValue(image);
        startActivity(new Intent(this, organizer_page.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int checkForInput(String eventNameText, String descriptionText, String dateStr, String linkText, String collegeText){
        int error = 0;
        if(eventNameText.isEmpty()){
            error = 1;
            eventName.setBackground(getDrawable(R.drawable.red_round_border));
            eventName.setHint("Please enter event name");
            eventName.setHintTextColor(Color.RED);
        }
        else{
            eventName.setBackground(getDrawable(R.drawable.white_round_border));
            eventName.setHint("Event name");
            eventName.setHintTextColor(Color.GRAY);
        }

        if(descriptionText.isEmpty()){
            error = 1;
            description.setBackground(getDrawable(R.drawable.big_red_box_border));
            description.setHint("Please enter event description");
            description.setHintTextColor(Color.RED);
        }
        else{
            description.setBackground(getDrawable(R.drawable.big_white_box_border));
            description.setHint("Event description");
            description.setHintTextColor(Color.GRAY);
        }

        if(linkText.isEmpty()){
            error = 1;
            link.setBackground(getDrawable(R.drawable.red_round_border));
            link.setHint("Enter date (dd/mm/yyyy)");
            link.setHintTextColor(Color.RED);
        }
        else{
            link.setBackground(getDrawable(R.drawable.white_round_border));
            link.setHint("Enter date (dd/mm/yyyy)");
            link.setHintTextColor(Color.GRAY);
        }

        if(collegeText.isEmpty()){
            error = 1;
            college.setBackground(getDrawable(R.drawable.red_round_border));
            college.setHint("Organizing college name");
            college.setHintTextColor(Color.RED);
        }
        else{
            college.setBackground(getDrawable(R.drawable.white_round_border));
            college.setHint("Organizing college name");
            college.setHintTextColor(Color.GRAY);
        }

        if(selectedImage == null){
            error = 1;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No image selected",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,10);
            toast.show();
        }

        return error;

    }

    private String getExtention(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(uri));
    }

    private void fileUploader(){
        String name = System.currentTimeMillis()+"."+getExtention(selectedImage);
        StorageReference ref = storageRef.child(name);
        event.setImageName(name);
        ref.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        //createNewPost(imageUrl);
                                    }
                                });
                            }
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Image added",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,10);
                        toast.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Image not added",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,10);
                        toast.show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        imageToUpload = (ImageView)findViewById(R.id.eventImage);
        imageHint = findViewById(R.id.imageHint);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
            imageHint.setText("Click image to re-upload");
        }
        fileUploader();
    }

}