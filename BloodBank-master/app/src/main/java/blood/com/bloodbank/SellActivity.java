package blood.com.bloodbank;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Spinner bloodgroup, component;
    EditText maxamount, priceperunit;
    private SQLiteHelper db;
    String id;
    Button confirm;
    DatabaseReference bloodbankfirebase ;
    DatabaseReference marketfirebase = FirebaseDatabase.getInstance().getReference("Market");
    AddsModel addsModel = new AddsModel();
    private Double lat,lon;
    MarketSellModel marketSellModel = new MarketSellModel();;
    GoogleApiClient gac;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        db = new SQLiteHelper(SellActivity.this);
        bloodbankfirebase = FirebaseDatabase.getInstance().getReference("BloodBanks").child(db.getAllValues().get("name")).child("myAds");
        bloodgroup = (Spinner) findViewById(R.id.bloodgroup);
        component = (Spinner) findViewById(R.id.component);
        maxamount = (EditText) findViewById(R.id.maxamount);
        priceperunit = (EditText) findViewById(R.id.priceperunit);


        //Location related stuff
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac = builder.build();

        confirm = (Button) findViewById(R.id.confirm);
        ArrayAdapter<String> bloodgroups = new ArrayAdapter<String>(SellActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bloodgroups));
        bloodgroups.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodgroup.setAdapter(bloodgroups);
        bloodgroup.setSelection(1);
        ArrayAdapter<String> components = new ArrayAdapter<String>(SellActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.components));
        components.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        component.setAdapter(components);
        component.setSelection(1);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conformation();
            }
        });

    }
    protected void onStart() {
        super.onStart();
        if(gac !=null)
            gac.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(gac!= null)
        {
            gac.disconnect();

        }
    }


    public void conformation() {
        final AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        final View conformationview = layoutInflater.inflate(R.layout.conformationdialog, null);
        dialogbuilder.setView(conformationview);
        dialogbuilder.setTitle("CONFROMATION");
        dialogbuilder.setMessage("Are You Sure?");
        Button confirm = (Button) conformationview.findViewById(R.id.confirm);
        Button cancel = (Button) conformationview.findViewById(R.id.cancel);
        final AlertDialog conformationdialog = dialogbuilder.create();
        conformationdialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SellActivity.this, bloodgroup.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                id = bloodbankfirebase.push().getKey();
                addsModel.setBloodgroup(bloodgroup.getSelectedItem().toString());
                addsModel.setBuyer("");
                addsModel.setBuyid("");
                addsModel.setComponentname(component.getSelectedItem().toString());
                addsModel.setId(id);
                addsModel.setMaxamount(maxamount.getText().toString());
                addsModel.setPriceperunit(priceperunit.getText().toString());
                marketSellModel.setBuyer("");
                marketSellModel.setBuyid("");
                marketSellModel.setDistance("0");
                marketSellModel.setId(id);
                marketSellModel.setLocation(db.getAllValues().get("location"));
                marketSellModel.setMaxamount(maxamount.getText().toString());
                marketSellModel.setName(db.getAllValues().get("name"));
                marketSellModel.setPriceperunit(priceperunit.getText().toString());
                Toast.makeText(SellActivity.this, priceperunit.getText().toString(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("BankComponents").child(db.getAllValues().get("name")).child(bloodgroup.getSelectedItem().toString()).child(component.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            Toast.makeText(SellActivity.this, "You dont that bloodgroup!", Toast.LENGTH_SHORT).show();
                        }
                        else if(Integer.parseInt(dataSnapshot.child("value").getValue().toString())>=Integer.parseInt(maxamount.getText().toString())){
                            marketfirebase.child("Sell").child(bloodgroup.getSelectedItem().toString()).child(component.getSelectedItem().toString()).child(id).setValue(marketSellModel);
                            bloodbankfirebase.child(id).setValue(addsModel);
                            finish();
                        }else{
                            Toast.makeText(SellActivity.this, "Your inventroy has lesser amount that you wish to sell!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                conformationdialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conformationdialog.dismiss();
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        loc = LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc!=null) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();

            marketSellModel.setLatitude(lat+"");
            marketSellModel.setLongitude(lon+"");

            Log.d("Lat",lat+"");
            Log.d("Lon",lon+"");
        }
        else
        {
            Toast.makeText(this, "Please Enable Gps/Come In Open Area", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}




/*
package blood.com.bloodbank;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Spinner bloodgroup, component;
    EditText maxamount, priceperunit;
    private SQLiteHelper db;

    Button confirm;
    DatabaseReference bloodbankfirebase ;
    DatabaseReference marketfirebase = FirebaseDatabase.getInstance().getReference("Market");

    private Double lat,lon;
    MarketSellModel marketSellModel = new MarketSellModel();;
    GoogleApiClient gac;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        db = new SQLiteHelper(SellActivity.this);
        bloodbankfirebase = FirebaseDatabase.getInstance().getReference("BloodBanks").child(db.getAllValues().get("name")).child("myAds");
        bloodgroup = (Spinner) findViewById(R.id.bloodgroup);
        component = (Spinner) findViewById(R.id.component);
        maxamount = (EditText) findViewById(R.id.maxamount);
        priceperunit = (EditText) findViewById(R.id.priceperunit);


        //Location related stuff
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac = builder.build();

        confirm = (Button) findViewById(R.id.confirm);
        ArrayAdapter<String> bloodgroups = new ArrayAdapter<String>(SellActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bloodgroups));
        bloodgroups.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodgroup.setAdapter(bloodgroups);
        bloodgroup.setSelection(1);
        ArrayAdapter<String> components = new ArrayAdapter<String>(SellActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.components));
        components.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        component.setAdapter(components);
        component.setSelection(1);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conformation();
            }
        });

    }
    protected void onStart() {
        super.onStart();
        if(gac !=null)
            gac.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(gac!= null)
        {
            gac.disconnect();

        }
    }


    public void conformation() {
        final AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        final View conformationview = layoutInflater.inflate(R.layout.conformationdialog, null);
        dialogbuilder.setView(conformationview);
        dialogbuilder.setTitle("CONFROMATION");
        dialogbuilder.setMessage("Are You Sure?");
        Button confirm = (Button) conformationview.findViewById(R.id.confirm);
        Button cancel = (Button) conformationview.findViewById(R.id.cancel);
        final AlertDialog conformationdialog = dialogbuilder.create();
        conformationdialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddsModel addsModel = new AddsModel();//Toast.makeText(SellActivity.this, bloodgroup.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                String id = bloodbankfirebase.push().getKey();
                addsModel.setBloodgroup(bloodgroup.getSelectedItem().toString());
                addsModel.setBuyer("");
                addsModel.setBuyid("");
                addsModel.setComponentname(component.getSelectedItem().toString());
                addsModel.setId(id);
                addsModel.setMaxamount(maxamount.getText().toString());
                addsModel.setPriceperunit(priceperunit.getText().toString());
                bloodbankfirebase.child(id).setValue(addsModel);
                marketSellModel.setBuyer("");
                marketSellModel.setBuyid("");
                marketSellModel.setDistance("0");
                marketSellModel.setId(id);
                marketSellModel.setLocation(db.getAllValues().get("location"));
                marketSellModel.setMaxamount(maxamount.getText().toString());
                marketSellModel.setName(db.getAllValues().get("name"));
                marketSellModel.setPriceperunit(priceperunit.getText().toString());
                Toast.makeText(SellActivity.this, priceperunit.getText().toString(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("BankComponents").child(db.getAllValues().get("name")).child(bloodgroup.getSelectedItem().toString()).child(component.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(Integer.parseInt(dataSnapshot.child("values").getValue().toString())>=Integer.parseInt(priceperunit.getText().toString())){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                marketfirebase.child("Sell").child(bloodgroup.getSelectedItem().toString()).child(component.getSelectedItem().toString()).child(id).setValue(marketSellModel);
                finish();
                conformationdialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conformationdialog.dismiss();
            }
        });
    }

//    static void sort(ArrayList<MarketSellModel> sellerList, int low, int high) {// merge sort
//        if (low < high) {
//            ArrayList<MarketSellModel> temp = new ArrayList<>();
//            int mid = low + (high - low) / 2;
//            int a = low, b = mid + 1;
//            sort(sellerList, low, mid);
//            sort(sellerList, mid + 1, high);
//            while (a <= mid || b <= high) {
//                if (a <= mid && b <= high) {
//                    if (Integer.valueOf(sellerList.get(a).location) <= Integer.valueOf(sellerList.get(b).location)) {
//                        temp.add(sellerList.get(a));
//                        a++;
//                    } else {
//                        temp.add(sellerList.get(b));
//                        b++;
//                    }
//                } else if (a <= mid && b > high) {
//                    temp.add(sellerList.get(a));
//                    a++;
//                } else if (b <= high && a > mid) {
//                    temp.add(sellerList.get(b));
//                    b++;
//                }
//            }
//            for (int i = 0; i < temp.size(); i++) {
//                sellerList.set(i + low, temp.get(i));
//            }
//        }
//    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        loc = LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc!=null) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();

            marketSellModel.setLatitude(lat+"");
            marketSellModel.setLongitude(lon+"");

            Log.d("Lat",lat+"");
            Log.d("Lon",lon+"");
        }
        else
        {
            Toast.makeText(this, "Please Enable Gps/Come In Open Area", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
 */