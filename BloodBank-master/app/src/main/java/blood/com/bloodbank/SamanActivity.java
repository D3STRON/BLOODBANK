package blood.com.bloodbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SamanActivity extends AppCompatActivity {

    private String name;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saman_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.ok:
                startActivity(new Intent(SamanActivity.this, HomeActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saman);

        SQLiteHelper db = new SQLiteHelper(this);
        name = db.getAllValues().get("name");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference samanReference = database.getReference("BankComponents");

        final EditText bloodValue = (EditText)findViewById(R.id.bloodValue);
        final EditText bloodCost = (EditText)findViewById(R.id.bloodCost);
        final EditText plateValue = (EditText)findViewById(R.id.plateValue);
        final EditText plateCost = (EditText)findViewById(R.id.plateCost);
        final EditText rbcValue = (EditText)findViewById(R.id.rbcValue);
        final EditText rbcCost = (EditText)findViewById(R.id.rbcCost);
        final EditText wbcValue = (EditText)findViewById(R.id.wbcValue);
        final EditText wbcCost = (EditText)findViewById(R.id.wbcCost);
        final EditText plasmaValue = (EditText)findViewById(R.id.plasmaValue);
        final EditText plasmaCost = (EditText)findViewById(R.id.plasmaCost);

        List<String> bloodGroups = new ArrayList<>();
        bloodGroups.add("A+");
        bloodGroups.add("A-");
        bloodGroups.add("B+");
        bloodGroups.add("B-");
        bloodGroups.add("O+");
        bloodGroups.add("O-");
        bloodGroups.add("AB+");
        bloodGroups.add("AB-");

        ArrayAdapter<String> bgAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodGroups);
        bgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final Spinner groupSpinner = (Spinner)findViewById(R.id.groupSpinner);
        groupSpinner.setAdapter(bgAdapter);

        final Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Components> map = new HashMap<>();
                map.put("blood", new Components(bloodValue.getText().toString(), bloodCost.getText().toString()));
                map.put("platelets", new Components(plateValue.getText().toString(), plateCost.getText().toString()));
                map.put("wbc", new Components(wbcValue.getText().toString(), wbcCost.getText().toString()));
                map.put("rbc", new Components(rbcValue.getText().toString(), rbcCost.getText().toString()));
                map.put("plasma", new Components(plasmaValue.getText().toString(), plasmaCost.getText().toString()));

                samanReference.child(name).child(groupSpinner.getSelectedItem().toString()).setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Successfully added "+groupSpinner.getSelectedItem().toString()+" values",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
    }
}