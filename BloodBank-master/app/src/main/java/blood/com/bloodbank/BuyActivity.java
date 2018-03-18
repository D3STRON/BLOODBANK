package blood.com.bloodbank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BuyActivity extends AppCompatActivity  {
    private Spinner SpinnerGroup, SpinnerComponent;
    private Button buyButton;
    private EditText buyQuantityField;
    private DatabaseReference DatabaseReference,BloodBanksReference,BloodBankNameReference,MarketBuyReference;
    private String bloodBankName;
    public OrderModel orderModel;
    public MarketBuyModel marketBuyModel;
    String bloodComponent,bloodGroup;
    private SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        db = new SQLiteHelper(this);
        bloodBankName = db.getAllValues().get("name");
        orderModel = new OrderModel();
        marketBuyModel = new MarketBuyModel();

        SpinnerGroup = (Spinner)findViewById(R.id.SpinnerBuyGroup);
        SpinnerComponent = (Spinner)findViewById(R.id.SpinnerBuyComponent);


        buyButton = (Button)findViewById(R.id.buyButton);

        buyQuantityField = (EditText)findViewById(R.id.buyQuantity);





        DatabaseReference = FirebaseDatabase.getInstance().getReference();
        BloodBanksReference = DatabaseReference.child("BloodBanks");
        //orderModel.setAmount(buyQuantityField.getText().toString());
        MarketBuyReference = DatabaseReference.child("Market").child("Buy");
        BloodBankNameReference = BloodBanksReference.child(bloodBankName);


        List<String> bloodGroups = new ArrayList<>();
        bloodGroups.add("A+");
        bloodGroups.add("A-");
        bloodGroups.add("B+");
        bloodGroups.add("B-");
        bloodGroups.add("O+");
        bloodGroups.add("O-");
        bloodGroups.add("AB+");
        bloodGroups.add("AB-");

        ArrayAdapter<String> mAdapterGroup = new ArrayAdapter<String>(BuyActivity.this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bloodgroups));
        mAdapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerGroup.setAdapter(mAdapterGroup);
        SpinnerGroup.setSelection(1);


        ArrayAdapter<String> mAdapterComponent= new ArrayAdapter<String>(BuyActivity.this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.components));
        mAdapterComponent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerComponent.setAdapter(mAdapterComponent);
        SpinnerComponent.setSelection(1);


        SpinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodGroup = adapterView.getItemAtPosition(i).toString();
                if(i > 0){
                    orderModel.setBloodgroup(bloodGroup);
                    Toast.makeText(BuyActivity.this, bloodGroup, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SpinnerComponent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodComponent = adapterView.getItemAtPosition(i).toString();
                if(i > 0){
                    orderModel.setComponentname(bloodComponent);
                    Toast.makeText(BuyActivity.this, bloodComponent, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buyQuantityField.getText().toString().isEmpty()){
                    Toast.makeText(BuyActivity.this, "NULL", Toast.LENGTH_SHORT).show();
                }else {
                    orderModel.setAmount(buyQuantityField.getText().toString());
                    marketBuyModel.setAmount(buyQuantityField.getText().toString());
                    addOrder();
                    Toast.makeText(BuyActivity.this, buyQuantityField.getText().toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void addOrder() {

        String id = BloodBankNameReference.child("myOrders").push().getKey();
        orderModel.setId(id);
        marketBuyModel.setId(id);

        orderModel.setSeller("");
        marketBuyModel.setSeller("");
        orderModel.setSellid("");
        marketBuyModel.setSellid("");
        marketBuyModel.setLatitude("4.5");
        marketBuyModel.setLongitude("5.6");
        marketBuyModel.setDistance("0");

        marketBuyModel.setLocation("Panvel");

        marketBuyModel.setName(bloodBankName);



        BloodBankNameReference.child("myOrders").child(id).setValue(orderModel);
        MarketBuyReference.child(bloodGroup).child(bloodComponent).child(id).setValue(marketBuyModel);


    }


}