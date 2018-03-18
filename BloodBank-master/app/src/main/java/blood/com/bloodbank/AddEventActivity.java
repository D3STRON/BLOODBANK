package blood.com.bloodbank;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {
    private Button addEventBtn;
    private EditText bloodBankNameField, eventNameField, eventAddressField, eventDescriptionField, eventDateField;
    private String bloodBankName = "Dena Bank";
    private com.google.firebase.database.DatabaseReference DatabaseReference,BloodBanksReference,BloodBankNameReference,EventReference;
    public EventModel eventModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        addEventBtn = (Button) findViewById(R.id.addEventBtn);

        bloodBankNameField = (EditText) findViewById(R.id.bloodBankName);
        eventNameField = (EditText) findViewById(R.id.eventName);
        eventAddressField = (EditText) findViewById(R.id.eventAddress);
        eventDescriptionField = (EditText) findViewById(R.id.eventDescription);
        eventDateField = (EditText) findViewById(R.id.eventDate);

        //Database Reference Declaration
        DatabaseReference = FirebaseDatabase.getInstance().getReference();
        BloodBanksReference = DatabaseReference.child("BloodBanks");


        //event Model declaration

        eventModel = new EventModel();

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });
    }

    private void addEvent() {

        String bloodbankname = bloodBankNameField.getText().toString();
        String eventname = eventNameField.getText().toString();
        String eventdesc = eventDescriptionField.getText().toString();
        String eventaddress = eventAddressField.getText().toString();
        String eventdate = eventDateField.getText().toString();

        if(!TextUtils.isEmpty(bloodbankname) && !TextUtils.isEmpty(eventname) && !TextUtils.isEmpty(eventdesc) && !TextUtils.isEmpty(eventaddress) && !TextUtils.isEmpty(eventdate)){

            eventModel.setBloddBankName(bloodbankname);
            eventModel.setEventName(eventname);
            eventModel.setEventAddress(eventaddress);
            eventModel.setEventDescription(eventdesc);
            eventModel.setEventDate(eventdate);

            EventReference = BloodBanksReference.child(bloodbankname).child("MyEvents");

            String id = EventReference.push().getKey();
            eventModel.setEventId(id);

            EventReference.child(id).setValue(eventModel);
            startActivity(new Intent(AddEventActivity.this,HomeActivity.class));

        }else {

        }

    }
}