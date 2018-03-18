package blood.com.bloodbank;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    private SQLiteHelper db;
    private RecyclerView mMyOrders;
    private DatabaseReference mDatabase;
    private Double lat,lon;
    GoogleApiClient gac;
    Location loc;
    OrderModel holder;
    ArrayList<OrderModel> orderlist= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        //Location related stuff
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac = builder.build();

        db = new SQLiteHelper(this);
        mMyOrders = (RecyclerView) findViewById(R.id.myOrders);
        mMyOrders.setHasFixedSize(true);
        mMyOrders.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    protected void onStart() {
        super.onStart();
        orderlist.clear();
        if(gac !=null)
            gac.connect();

        final FirebaseRecyclerAdapter<OrderModel,OrdersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrderModel, OrdersViewHolder>(
                OrderModel.class,
                R.layout.myorderrow,
                OrdersViewHolder.class,
                mDatabase.child("BloodBanks").child(db.getAllValues().get("name")).child("myOrders")
        ) {
            @Override
            protected void populateViewHolder(OrdersViewHolder viewHolder, OrderModel model, final int position) {
                orderlist.add(model);
                final String order_key = getRef(position).getKey();
                viewHolder.setBloodGroup(model.getBloodgroup());
                viewHolder.setComponent(model.getComponentname());
                viewHolder.setAmount(model.getAmount());
                viewHolder.setSeller(model.getSeller());

                viewHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder  = orderlist.get(position);
                        //Toast.makeText(MyOrders.this, holder.getId(), Toast.LENGTH_SHORT).show();
                        if(TextUtils.isEmpty(holder.getSellid())) {
                            FirebaseDatabase.getInstance().getReference("BloodBanks").child(db.getAllValues().get("name")).child("myOrders").child(holder.getId()).removeValue();
                        }else{
                            Intent intent= new Intent(Intent.ACTION_SEND);
                            FirebaseDatabase.getInstance().getReference("BloodBanks").child(holder.getSeller()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Intent intent= new Intent(Intent.ACTION_SEND);
                                    String[] to={dataSnapshot.child("email").getValue().toString()};
                                    intent.setData(Uri.parse("mailto:"));
                                    intent.putExtra(Intent.EXTRA_EMAIL, to);
                                    intent.putExtra(Intent.EXTRA_SUBJECT,"ORDER CANCELLATION");
                                    intent.putExtra(Intent.EXTRA_TEXT,"Cancellation of order of " +holder.getAmount()+" dl of " + holder.getComponentname() +" of type "+holder.getBloodgroup()+" by company " +db.getAllValues().get("name"));
                                    intent.setType("text/plain");
                                    startActivity(Intent.createChooser(intent, "Send email"));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

                viewHolder.mConfirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder  = orderlist.get(position);
                        if(!TextUtils.isEmpty(holder.getSellid())) {
                            FirebaseDatabase.getInstance().getReference("BankComponents").child(db.getAllValues().get("name")).child(holder.getBloodgroup()).child(holder.getComponentname()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        int value = Integer.parseInt(dataSnapshot.child("value").getValue().toString());
                                        value += Integer.parseInt(holder.getAmount());
                                        FirebaseDatabase.getInstance().getReference("BankComponents").child(db.getAllValues().get("name")).child(holder.getBloodgroup()).child(holder.getComponentname()).child("value").setValue(Integer.toString(value));
                                    }
                                    else{
                                        DatabaseReference addToInventory =FirebaseDatabase.getInstance().getReference("BankComponents").child(db.getAllValues().get("name")).child(holder.getBloodgroup());
                                        Components components= new Components();
                                        components.setValue("0");
                                        components.setCost("0");
                                        addToInventory.child("blood").setValue(components);
                                        addToInventory.child("plasma").setValue(components);
                                        addToInventory.child("rbc").setValue(components);
                                        addToInventory.child("wbc").setValue(components);
                                        addToInventory.child("platelets").setValue(components);
                                        components.setValue(holder.getAmount());
                                        addToInventory.child(holder.getComponentname()).setValue(components);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            FirebaseDatabase.getInstance().getReference("BankComponents").child(holder.getSeller()).child(holder.getBloodgroup()).child(holder.getComponentname()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int value = Integer.parseInt(dataSnapshot.child("value").getValue().toString());
                                    value -= Integer.parseInt(holder.getAmount());
                                    FirebaseDatabase.getInstance().getReference("BankComponents").child(holder.getSeller()).child(holder.getBloodgroup()).child(holder.getComponentname()).child("value").setValue(Integer.toString(value));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                           FirebaseDatabase.getInstance().getReference("Market").child("Sell").child(holder.getBloodgroup()).child(holder.getComponentname()).child(holder.getSellid()).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   int maxamount = Integer.parseInt(dataSnapshot.child("maxamount").getValue().toString());
                                   maxamount-=Integer.parseInt(holder.getAmount());
                                   if(maxamount!=0) {
                                       FirebaseDatabase.getInstance().getReference("Market").child("Sell").child(holder.getBloodgroup()).child(holder.getComponentname()).child(holder.getSellid()).child("maxamount").setValue(Integer.toString(maxamount));
                                   }else{
                                       FirebaseDatabase.getInstance().getReference("Market").child("Sell").child(holder.getBloodgroup()).child(holder.getComponentname()).child(holder.getSellid()).removeValue();
                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });

                           FirebaseDatabase.getInstance().getReference("BloodBanks").child(holder.getSeller()).child("myAds").child(holder.getSellid()).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   int maxamount = Integer.parseInt(dataSnapshot.child("maxamount").getValue().toString());
                                   maxamount-=Integer.parseInt(holder.getAmount());
                                   if(maxamount!=0) {
                                       FirebaseDatabase.getInstance().getReference("BloodBanks").child(holder.getSeller()).child("myAds").child(holder.getSellid()).child("maxamount").setValue(Integer.toString(maxamount));
                                   }else{
                                       FirebaseDatabase.getInstance().getReference("BloodBanks").child(holder.getSeller()).child("myAds").child(holder.getSellid()).removeValue();
                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });
                            FirebaseDatabase.getInstance().getReference("BloodBanks").child(db.getAllValues().get("name")).child("myOrders").child(holder.getId()).removeValue();
                        }else{
                            Toast.makeText(MyOrders.this, "YOU DONT HAVE A SELLER YET"  , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder  = orderlist.get(position);
                        //Toast.makeText(MyOrders.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                        if(TextUtils.isEmpty(holder.getSellid())) {
                            Intent intent = new Intent(MyOrders.this, MarketActivity.class);
                            intent.putExtra("lat", 40.6);
                            intent.putExtra("lon", 50.4);
                            //  Toast.makeText(MyOrders.this, Double.toString(lat), Toast.LENGTH_SHORT).show();
                            intent.putExtra("bloodgroup", orderlist.get(position).getBloodgroup());
                            intent.putExtra("component", orderlist.get(position).getComponentname());
                            intent.putExtra("amount", orderlist.get(position).getAmount());
                            intent.putExtra("buyid", orderlist.get(position).getId());
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(MyOrders.this, "YOUR ORDER IS ALREADY PLACED ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        };
        mMyOrders.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(gac!= null)
        {
            gac.disconnect();

        }
    }
    public static class OrdersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        Button mConfirmBtn, cancelBtn;
        public OrdersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mConfirmBtn = (Button) mView.findViewById(R.id.confirmBtn);

            cancelBtn= (Button) mView.findViewById(R.id.cancelBtn);
        }
        public void setBloodGroup(String BloodGroup){
            TextView bloodgroup = (TextView) mView.findViewById(R.id.bloodGroupVal);
            bloodgroup.setText(BloodGroup);
        }
        public void setComponent(String Component){
            TextView component = (TextView) mView.findViewById(R.id.componentVal);
            component.setText(Component);
        }
        public void setAmount(String Amount){
            TextView amount = (TextView) mView.findViewById(R.id.amountVal);
            amount.setText(Amount);
        }
        public void setSeller(String Seller){
            TextView seller = (TextView) mView.findViewById(R.id.sellerVal);
            seller.setText(Seller);
        }
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
            //marketBuyModel.setLatitude(lat+"");
            //marketBuyModel.setLongitude(lon+"");

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
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }
}