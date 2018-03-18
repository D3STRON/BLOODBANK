package blood.com.bloodbank;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class MarketActivity extends AppCompatActivity {
    ListView sellers;
    CustomAdapter customAdapter;
    SQLiteHelper db;
    FloatingActionButton mfab;
    DatabaseReference sellersfirebase = FirebaseDatabase.getInstance().getReference("Market").child("Sell");
    ArrayList<MarketSellModel> sellerslist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        db= new SQLiteHelper(this);
        sellers= (ListView)findViewById(R.id.sellers);
        mfab = (FloatingActionButton) findViewById(R.id.fab);
        customAdapter =new CustomAdapter(sellerslist,MarketActivity.this,getIntent());
        sellers.setAdapter(customAdapter);
        sellersfirebase = sellersfirebase.child(getIntent().getStringExtra("bloodgroup")).child(getIntent().getStringExtra("component"));
        sellersfirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    MarketSellModel temp= fire.getValue(MarketSellModel.class);
                    if(TextUtils.isEmpty(temp.getBuyid()) && !temp.getName().matches(db.getAllValues().get("name")) && Integer.parseInt(temp.getMaxamount())>=  Integer.parseInt(getIntent().getStringExtra("amount"))){
                    sellerslist.add(temp);}
                }
                putDistance(sellerslist,getIntent().getDoubleExtra("lat",0),getIntent().getDoubleExtra("lon",0));
                sort(sellerslist,0,sellerslist.size()-1);
                Toast.makeText(MarketActivity.this, Integer.toString(sellerslist.size()), Toast.LENGTH_SHORT).show();
                customAdapter.refresh(sellerslist);
                sellers.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketActivity.this, MapsActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",sellerslist);
                intent.putExtra("BUNDLE",args);
                startActivity(intent);
            }
        });

    }

    private void putDistance(ArrayList<MarketSellModel> sellerList, double lat, double lon){
        for(int i=0;i<sellerList.size();i++)
        {
            double dist= distance(lat,lon,Double.parseDouble(sellerList.get(i).getLatitude()),Double.parseDouble(sellerList.get(i).getLongitude()));
            sellerList.get(i).setDistance(Double.toString(dist));
            //Toast.makeText(MarketActivity.this, sellerList.get(i).distance, Toast.LENGTH_SHORT).show();
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    static void sort(ArrayList<MarketSellModel> sellerList, int low, int high) {// merge sort
        if (low < high) {
            ArrayList<MarketSellModel> temp = new ArrayList<>();
            int mid = low + (high - low) / 2;
            int a = low, b = mid + 1;
            sort(sellerList, low, mid);
            sort(sellerList, mid + 1, high);
            while (a <= mid || b <= high) {
                if (a <= mid && b <= high) {
                    if (Double.parseDouble(sellerList.get(a).distance) <= Double.parseDouble(sellerList.get(b).distance)) {
                        temp.add(sellerList.get(a));
                        a++;
                    } else {
                        temp.add(sellerList.get(b));
                        b++;
                    }
                } else if (a <= mid && b > high) {
                    temp.add(sellerList.get(a));
                    a++;
                } else if (b <= high && a > mid) {
                    temp.add(sellerList.get(b));
                    b++;
                }
            }
            for (int i = 0; i < temp.size(); i++) {
                sellerList.set(i + low, temp.get(i));
            }
        }
    }



    class CustomAdapter extends BaseAdapter implements ListAdapter{
        ArrayList<MarketSellModel> sellerlist = new ArrayList<>();
        Context context;
        Intent intent;
        CustomAdapter(ArrayList<MarketSellModel> sellerlist, Context context, Intent intent){
            this.sellerlist= sellerlist;
            this.context= context;
            this.intent=intent;
        }
        @Override
        public int getCount() {
            return sellerlist.size();
        }

        @Override
        public Object getItem(int position) {
            return sellerlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        public void refresh(ArrayList<MarketSellModel> sellerlist){
            this.sellerlist= sellerlist;
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row, null);
            }
            Button placeorder= (Button)view.findViewById(R.id.placeorder);
            TextView priceperunit = (TextView)view.findViewById(R.id.priceperunit);
            TextView maxamount = (TextView)view.findViewById(R.id.maxamount);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView location = (TextView)view.findViewById(R.id.location);
            TextView distance = (TextView)view.findViewById(R.id.distance);
            priceperunit.setText(sellerlist.get(position).getPriceperunit() +" ₹");
            maxamount.setText(sellerlist.get(position).getMaxamount() +" dl");
            name.setText(sellerlist.get(position).getName());
            location.setText(sellerlist.get(position).getLocation());
            distance.setText(sellerlist.get(position).getDistance());

            placeorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sellerlist.get(position).setBuyid(intent.getStringExtra("buyid"));
                    sellerlist.get(position).setBuyer(db.getAllValues().get("name"));
                    DatabaseReference sellersfirebase = FirebaseDatabase.getInstance().getReference("Market").child("Sell").child(intent.getStringExtra("bloodgroup")).child(intent.getStringExtra("component"));
                    sellersfirebase.child(sellerlist.get(position).getId()).setValue(sellerlist.get(position));
                    FirebaseDatabase.getInstance().getReference("BloodBanks").child(sellerlist.get(position).getName()).child("myAds").child(sellerlist.get(position).getId()).child("buyer").setValue(db.getAllValues().get("name"));
                    FirebaseDatabase.getInstance().getReference("BloodBanks").child(sellerlist.get(position).getName()).child("myAds").child(sellerlist.get(position).getId()).child("buyid").setValue(intent.getStringExtra("buyid"));
                    FirebaseDatabase.getInstance().getReference("BloodBanks").child(db.getAllValues().get("name")).child("myOrders").child(intent.getStringExtra("buyid")).child("seller").setValue(sellerlist.get(position).getName());
                    FirebaseDatabase.getInstance().getReference("BloodBanks").child(db.getAllValues().get("name")).child("myOrders").child(intent.getStringExtra("buyid")).child("sellid").setValue(sellerlist.get(position).getId());
                    //Toast.makeText(context,sellersfirebase.getKey(), Toast.LENGTH_SHORT).show();
                    sellerlist.clear();
                    sellersfirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot fire : dataSnapshot.getChildren())
                            {
                                MarketSellModel temp= fire.getValue(MarketSellModel.class);
                                if(TextUtils.isEmpty(temp.getBuyid())){
                                    sellerslist.add(temp);}
                            }
                            putDistance(sellerslist,getIntent().getDoubleExtra("lat",0),getIntent().getDoubleExtra("lon",0));
                            sort(sellerslist,0,sellerslist.size()-1);
                            Toast.makeText(MarketActivity.this, Integer.toString(sellerslist.size()), Toast.LENGTH_SHORT).show();
                            customAdapter.refresh(sellerslist);
                            sellers.setAdapter(customAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                Intent intent1 = new Intent(MarketActivity.this,MyOrders.class);
                finish();
                startActivity(intent1);
                }
            });
            return view;
        }
    }


}



