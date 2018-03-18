package blood.com.bloodbank;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private SQLiteHelper db;
    private HashMap<String, Components> map;
    private String name;
    CardView myOrders,myAds,buy,sell, stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final CardView fetch = (CardView) findViewById(R.id.fetch);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference bankRef = database.getReference("BankComponents");
        myAds= (CardView) findViewById(R.id.myAds);
        myOrders= (CardView) findViewById(R.id.myOrders);
        buy= (CardView) findViewById(R.id.buy);
        sell= (CardView) findViewById(R.id.sell);
        stats = (CardView) findViewById(R.id.stats);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this,SellActivity.class);
                startActivity(intent);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddEventActivity.class));
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this,BuyActivity.class);
                startActivity(intent);
            }
        });
        myAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyAds.class));
            }
        });
        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyOrders.class));
            }
        });
        db = new SQLiteHelper(this);
        name = db.getAllValues().get("name");

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FetchActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Saman:
                startActivity(new Intent(HomeActivity.this, SamanActivity.class));
                return true;
            case R.id.myEvent:
                startActivity(new Intent(HomeActivity.this, MyEvents.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
}