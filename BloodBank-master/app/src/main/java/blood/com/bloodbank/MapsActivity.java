package blood.com.bloodbank;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private double lat,lon;
    ArrayList<MarketSellModel> sellersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        /*lat = intent.getDoubleExtra("Latitude",0);
        lon = intent.getDoubleExtra("Longitude",0);*/
        Bundle args = intent.getBundleExtra("BUNDLE");
        sellersList = (ArrayList<MarketSellModel>) args.getSerializable("ARRAYLIST");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for(int i=0;i<sellersList.size();i++){
            double lat = Double.parseDouble(sellersList.get(i).getLatitude());
            double lon = Double.parseDouble(sellersList.get(i).getLongitude());
            LatLng newpos = new LatLng(lat, lon);
            String name = sellersList.get(i).getName();
            mMap.addMarker(new MarkerOptions().position(newpos).title(name));
        }
        // Add a marker in Sydney and move the camera
        LatLng panvel = new LatLng(19.20,73.10);
        mMap.addMarker(new MarkerOptions().position(panvel).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng sydney = new LatLng(24.41, 80.24);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


}