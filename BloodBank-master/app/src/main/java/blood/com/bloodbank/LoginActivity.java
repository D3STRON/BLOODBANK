package blood.com.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.instantapps.ActivityCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog pd;
    private SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd = new ProgressDialog(LoginActivity.this);

        pd.setMessage("Please Wait...");
        auth = FirebaseAuth.getInstance();
        db = new SQLiteHelper(LoginActivity.this);
        final TextInputEditText loginID = (TextInputEditText)findViewById(R.id.loginId);
        final TextInputEditText password = (TextInputEditText)findViewById(R.id.loginPassword);
        final Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                login(loginID.getText().toString(), password.getText().toString());
            }
        });
    }

    private void login(final String id, final String password) {
        auth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(pd.isShowing())
                                pd.dismiss();
                            if(db.isExist())
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            else startActivity(new Intent(LoginActivity.this, FormActivity.class));
                            finish();
                        }
                        else {
                            auth.createUserWithEmailAndPassword(id, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                if(pd.isShowing())
                                                    pd.dismiss();
                                                startActivity(new Intent(LoginActivity.this, FormActivity.class));
                                                finish();
                                            }
                                            else {
                                                if(pd.isShowing())
                                                    pd.dismiss();
                                                Toast.makeText(getApplicationContext(),"Incorrect Password", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}