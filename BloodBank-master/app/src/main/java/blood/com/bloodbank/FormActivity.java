package blood.com.bloodbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        db = new SQLiteHelper(this);
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference bankReference = database.getReference("BloodBanks");
        final FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(FormActivity.this, LoginActivity.class));
            finish();
        }

        final TextInputEditText name = (TextInputEditText)findViewById(R.id.input_name);
        final TextInputEditText location = (TextInputEditText)findViewById(R.id.input_location);
        final TextInputEditText phone = (TextInputEditText)findViewById(R.id.input_phone);
        final Button next = (Button)findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankInfo bi = new BankInfo(name.getText().toString(),phone.getText().toString(),user.getEmail(),location.getText().toString());
                bankReference.child(name.getText().toString()).setValue(bi)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(FormActivity.this, SamanActivity.class);
//                            intent.putExtra("name",name.getText().toString());
//                            intent.putExtra("location",location.getText().toString());
//                            intent.putExtra("contact",phone.getText().toString());
                                    db.addInfo(phone.getText().toString(),user.getEmail(),location.getText().toString(),name.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}