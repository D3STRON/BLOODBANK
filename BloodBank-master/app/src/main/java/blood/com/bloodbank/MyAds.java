package blood.com.bloodbank;

import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.TextView;

        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class MyAds extends AppCompatActivity {
    private RecyclerView mMyAds;
    private DatabaseReference mDatabase;
    private SQLiteHelper db = new SQLiteHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        mMyAds = (RecyclerView) findViewById(R.id.myAds);
        mMyAds.setHasFixedSize(true);
        mMyAds.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AddsModel,AdsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AddsModel,AdsViewHolder>(
                AddsModel.class,
                R.layout.myadsrow,
                AdsViewHolder.class,
                mDatabase.child("BloodBanks").child(db.getAllValues().get("name")).child("myAds")
        ) {

            @Override
            protected void populateViewHolder(AdsViewHolder viewHolder, AddsModel model, int position) {
                viewHolder.setBloodGroup(model.getBloodgroup());
                viewHolder.setComponent(model.getComponentname());
                viewHolder.setMaxAmount(model.getMaxamount());
                viewHolder.setPricePerUnit(model.getPriceperunit());
                viewHolder.setBuyer(model.getBuyer());
            }
        };
        mMyAds.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AdsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public AdsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setBloodGroup(String BloodGroup){
            TextView bloodgroup = (TextView) mView.findViewById(R.id.bloodGroupVal);
            bloodgroup.setText(BloodGroup);
        }
        public void setComponent(String Component){
            TextView component = (TextView) mView.findViewById(R.id.componentVal);
            component.setText(Component);
        }
        public void setMaxAmount(String Amount){
            TextView amount = (TextView) mView.findViewById(R.id.maxamountVal);
            amount.setText(Amount);
        }
        public void setPricePerUnit(String PricePerUnit){
            TextView pricePerUnit = (TextView) mView.findViewById(R.id.maxamountVal);
            pricePerUnit.setText(PricePerUnit);
        }
        public void setBuyer(String Buyer){
            TextView buyer = (TextView) mView.findViewById(R.id.buyerVal);
            buyer.setText(Buyer);
        }
    }
}