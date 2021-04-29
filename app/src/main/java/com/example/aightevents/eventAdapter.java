package com.example.aightevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;


public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.EventHolder>{

    private Context mcontext;
    private List<Event> mevents;


    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
    }

//    public EventAdapter(Context context, List<Event> events){
//        mcontext = context;
//        mevents = events;
//
//    }

    @Override
    protected void onBindViewHolder(@NonNull EventHolder eventHolder, int i, @NonNull Event event) {
        eventHolder.textViewName.setText(event.getEventName());
        eventHolder.textViewDate.setText(event.getDate());
        eventHolder.textViewCategory.setText(event.getEventType());
        eventHolder.textViewCollege.setText(event.getCollege());
        eventHolder.textViewDescription.setText(event.getDescription());
        eventHolder.textViewLink.setText(event.getLink());
        eventHolder.textViewImageLink.setText(event.getImageLink());
        //StorageReference storageRef = FirebaseStorage.getInstance().getReference("images");
        //Glide.with(this.load(event.getImageLink()).into(imageViewImage);
        //eventHolder.itemView.setOnClickListener(onItemClicked(event));
        Picasso.get()
                .load(event.getImageLink())
                .fit()
                .centerCrop()
                .into(eventHolder.imageViewImage);
    }


    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
        return new EventHolder(v);
    }

    class EventHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewDate;
        public TextView textViewCategory;
        public TextView textViewCollege;
        public TextView textViewDescription;
        public TextView textViewLink;
        public TextView textViewImageLink;
        public ImageView imageViewImage;

        public EventHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.eventName);
            textViewDate = itemView.findViewById(R.id.date);
            textViewCategory = itemView.findViewById(R.id.categoryFilter);
            textViewCollege = itemView.findViewById(R.id.collegeFilter);
            textViewDescription = itemView.findViewById(R.id.descriptionEvent);
            textViewLink = itemView.findViewById(R.id.link);
            imageViewImage = itemView.findViewById(R.id.eventImage);
            textViewImageLink = itemView.findViewById(R.id.imageLink);
        }
    }
}
