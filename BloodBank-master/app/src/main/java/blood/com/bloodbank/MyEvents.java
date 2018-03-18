package blood.com.bloodbank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyEvents extends AppCompatActivity {
    private RecyclerView mMyEvents;
    private DatabaseReference mDatabase;
    private SQLiteHelper db = new SQLiteHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        mMyEvents = (RecyclerView) findViewById(R.id.myEvents);
        mMyEvents.setHasFixedSize(true);
        mMyEvents.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<EventModel,EventsViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<EventModel, EventsViewHolder>(
                EventModel.class,
                R.layout.myeventsrow,
                EventsViewHolder.class,
                mDatabase.child("BloodBanks").child(db.getAllValues().get("name")).child("MyEvents")
        ) {
            @Override
            protected void populateViewHolder(EventsViewHolder viewHolder, EventModel model, int position) {
                viewHolder.setName(model.getEventName());
                viewHolder.setAddress(model.getEventAddress());
                viewHolder.setDate(model.getEventDate());
                viewHolder.setDescription(model.getEventDescription());

            }
        };
        mMyEvents.setAdapter(firebaseRecyclerAdapter);
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public EventsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String Name){
            TextView name = (TextView) mView.findViewById(R.id.NameVal);
            name.setText(Name);
        }
        public void setDescription(String Description){
            TextView description = (TextView) mView.findViewById(R.id.DescriptionVal);
            description.setText(Description);
        }
        public void setAddress(String Address){
            TextView address = (TextView) mView.findViewById(R.id.addressVal);
            address.setText(Address);
        }
        public void setDate(String Date){
            TextView date = (TextView) mView.findViewById(R.id.dateVal);
            date.setText(Date);
        }
    }
}