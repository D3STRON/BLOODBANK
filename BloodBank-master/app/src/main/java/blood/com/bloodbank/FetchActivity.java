package blood.com.bloodbank;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FetchActivity extends AppCompatActivity {

    private Spinner bgID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        List<String> bloodGroups = new ArrayList<>();
        bloodGroups.add("A+");
        bloodGroups.add("A-");
        bloodGroups.add("B+");
        bloodGroups.add("B-");
        bloodGroups.add("O+");
        bloodGroups.add("O-");
        bloodGroups.add("AB+");
        bloodGroups.add("AB-");

        final SQLiteHelper db = new SQLiteHelper(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference bankRef = database.getReference("BankComponents");

        ArrayAdapter<String> bgAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodGroups);
        bgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bgID = (Spinner) findViewById(R.id.bgID);
        bgID.setAdapter(bgAdapter);
        final Button fetch = (Button) findViewById(R.id.fetch);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FetchActivity.this, bgID.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                Log.e("TAG",bgID.getSelectedItem().toString());
                bankRef.child(db.getAllValues().get("name")).child(bgID.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue()==null)
                        {
                            Toast.makeText(FetchActivity.this, "No entry for blood type " + bgID.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        }else {
                            TextView blVal = (TextView) findViewById(R.id.blVal);
                            blVal.setText(dataSnapshot.child("blood").child("value").getValue().toString() + " dl");
                            TextView blCost = (TextView) findViewById(R.id.blCost);
                            blCost.setText(dataSnapshot.child("blood").child("cost").getValue().toString() + " ₹/dl");


                            TextView plVal = (TextView) findViewById(R.id.plValue);
                            plVal.setText(dataSnapshot.child("platelets").child("value").getValue().toString() + " dl");
                            TextView plCost = (TextView) findViewById(R.id.plCost);
                            plCost.setText(dataSnapshot.child("platelets").child("cost").getValue().toString() + " ₹/dl");

                            TextView wbValue = (TextView) findViewById(R.id.wbValue);
                            wbValue.setText(dataSnapshot.child("wbc").child("value").getValue().toString() + " dl");
                            TextView wbCost = (TextView) findViewById(R.id.wbCost);
                            wbCost.setText(dataSnapshot.child("wbc").child("cost").getValue().toString() + " ₹/dl");

                            TextView rbVal = (TextView)findViewById(R.id.rbValue);
                            rbVal.setText(dataSnapshot.child("rbc").child("value").getValue().toString() + " dl");
                            TextView rbCost = (TextView) findViewById(R.id.rbCost);
                            rbCost.setText(dataSnapshot.child("rbc").child("cost").getValue().toString() + " ₹/dl");

                            TextView plsVal = (TextView) findViewById(R.id.plsValue);
                            plsVal.setText(dataSnapshot.child("plasma").child("value").getValue().toString() + " dl");
                            TextView plsCost = (TextView) findViewById(R.id.plsCost);
                            plsCost.setText(dataSnapshot.child("plasma").child("cost").getValue().toString() + " ₹/dl");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(FetchActivity.this, "CRITICAL ERROR!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saman_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.ok:
                startActivity(new Intent(FetchActivity.this, HomeActivity.class));
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
