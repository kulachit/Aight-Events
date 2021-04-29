package com.example.aightevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class participant_page extends AppCompatActivity {

    RecyclerView recyclerViewHome;
    ImageView imageHomePage;

    //for search page
    RecyclerView recyclerViewSearch;
    ImageView imageLeft;
    ImageView imageRight;
    TextInputLayout textInputLayout;

    //for search page
    ImageView profileUpload;
    TextView userEmailAdd;
    Button logOut;

    private EventAdapter adapter;

    BubbleNavigationConstraintView mainView;

    private FirebaseAuth mAuth;
    private com.google.android.gms.auth.api.signin.GoogleSignInClient mGoogleSignInClient;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postRef = db.collection("post");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_page);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        getSupportActionBar().hide();

        //for home page
         recyclerViewHome = findViewById(R.id.recycler_view_home);
         imageHomePage = findViewById(R.id.imageHomePage);

        //for search page
         recyclerViewSearch = findViewById(R.id.recycler_view_search);
         imageLeft = findViewById(R.id.imageLeft);
         imageRight = findViewById(R.id.imageRight);
         textInputLayout = findViewById(R.id.textInputLayout);

        //for search page
         profileUpload = findViewById(R.id.profileUpload);
         userEmailAdd = findViewById(R.id.userEmailAdd);
         logOut = findViewById(R.id.logOut);

        //invisible
        profileUpload.setVisibility(View.INVISIBLE);
        userEmailAdd.setVisibility(View.INVISIBLE);
        logOut.setVisibility(View.INVISIBLE);

        recyclerViewSearch.setVisibility(View.INVISIBLE);
        imageLeft.setVisibility(View.INVISIBLE);
        imageRight.setVisibility(View.INVISIBLE);
        textInputLayout.setVisibility(View.INVISIBLE);

        //first time home page is viewed
        home();

        mainView = findViewById(R.id.navigation_constraint);

        mainView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                int current = mainView.getCurrentActiveItemPosition();
                switch (current) {
                    case 0:
                        //visible
                        recyclerViewHome.setVisibility(View.VISIBLE);
                        imageHomePage.setVisibility(View.VISIBLE);

                        //invisible
                        recyclerViewSearch.setVisibility(View.INVISIBLE);
                        imageLeft.setVisibility(View.INVISIBLE);
                        imageRight.setVisibility(View.INVISIBLE);
                        textInputLayout.setVisibility(View.INVISIBLE);

                        profileUpload.setVisibility(View.INVISIBLE);
                        userEmailAdd.setVisibility(View.INVISIBLE);
                        logOut.setVisibility(View.INVISIBLE);

                        home();

                        break;

                    case 1:
                        //visible
                        recyclerViewSearch.setVisibility(View.VISIBLE);
                        imageLeft.setVisibility(View.VISIBLE);
                        imageRight.setVisibility(View.VISIBLE);
                        textInputLayout.setVisibility(View.VISIBLE);

                        //invisible
                        recyclerViewHome.setVisibility(View.INVISIBLE);
                        imageHomePage.setVisibility(View.INVISIBLE);


                        profileUpload.setVisibility(View.INVISIBLE);
                        userEmailAdd.setVisibility(View.INVISIBLE);
                        logOut.setVisibility(View.INVISIBLE);

                        search();

                        break;

                    case 2:
                        //visible
                        profileUpload.setVisibility(View.VISIBLE);
                        userEmailAdd.setVisibility(View.VISIBLE);
                        logOut.setVisibility(View.VISIBLE);

                        //invisible
                        recyclerViewSearch.setVisibility(View.INVISIBLE);
                        imageLeft.setVisibility(View.INVISIBLE);
                        imageRight.setVisibility(View.INVISIBLE);
                        textInputLayout.setVisibility(View.INVISIBLE);

                        recyclerViewHome.setVisibility(View.INVISIBLE);
                        imageHomePage.setVisibility(View.INVISIBLE);

                        break;

                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("192731386323-4dgkapa8agjgcg39a3ldj951ns6glck2.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Filter filter = Filter.getInstance();
        if(!(filter.getFlag() == null)){
            searchByFilter(filter);
        }
    }

    public void home(){

        Query query = postRef.orderBy("dateOfCreation", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>().setQuery(query, Event.class).build();

        adapter = new EventAdapter(options);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHome.setAdapter(adapter);

        adapter.startListening();
    }

    public void search(){

        Query query = postRef.orderBy("dateOfCreation", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>().setQuery(query, Event.class).build();

        adapter = new EventAdapter(options);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setAdapter(adapter);

        adapter.startListening();
    }

    public void searchByFilter(Filter filter){
        String category = filter.getCategory();
        String college = filter.getCollege();

        Query query = postRef.whereEqualTo("eventType", category);

        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>().setQuery(query, Event.class).build();

        adapter = new EventAdapter(options);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setAdapter(adapter);

        adapter.startListening();
    }

    public void logOut(View logout){
        try{
            mAuth.getInstance().signOut();
        }
        catch (Exception e){
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateUI(null);
                        }

                    });
        }

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

    public void chooseFilters(View view){
        startActivity(new Intent(this, select_filter.class));
    }

    public void showMore(View view){
        try {
            TextView name = view.findViewById(R.id.eventName);
            String nem = name.getText().toString();
            Log.println(Log.ERROR, "HELLO", nem);
            Event newEvent = new Event();

            TextView textViewCollege = view.findViewById(R.id.collegeFilter);
            TextView textViewName = name;
            TextView textViewDescription = view.findViewById(R.id.umOkay);
            TextView textViewDate = view.findViewById(R.id.date);
            TextView textViewCategory = view.findViewById(R.id.categoryFilter);
            TextView textViewLink = view.findViewById(R.id.link);
            TextView textViewImageLink = view.findViewById(R.id.imageLink);


            String stringCollege = textViewCollege.getText().toString();
            String stringName = textViewName.getText().toString();
            String stringDescription = textViewDescription.getText().toString();
            String stringDate = textViewDate.getText().toString();
            String stringCategory = textViewCategory.getText().toString();
            String stringLink = textViewLink.getText().toString();
            String stringImageLink = textViewImageLink.getText().toString();

            newEvent.setCollege(stringCollege);
            newEvent.setEventName(stringName);
            newEvent.setDescription(stringDescription);
            newEvent.setDate(stringDate);
            newEvent.setEventType(stringCategory);
            newEvent.setLink(stringLink);
            newEvent.setImageLink(stringImageLink);

            Intent intent = new Intent(this, event_details.class);
            intent.putExtra("Event", newEvent);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}