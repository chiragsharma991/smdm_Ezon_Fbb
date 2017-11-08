package apsupportapp.aperotechnologies.com.designapp.InfantApp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter.CustomerDetailsAdapter;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter.FragmentHomeAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

public class CustomerDetailsActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recycler_custdetails;
    ArrayList<String> listCustomer;
    CustomerDetailsAdapter cusomerDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        initialise();
    }

    private void initialise() {

        listCustomer = new ArrayList<>();
        recycler_custdetails = (RecyclerView) findViewById(R.id.recycler_custdetails);
        recycler_custdetails.setLayoutManager(new LinearLayoutManager(context));
        cusomerDetailsAdapter = new CustomerDetailsAdapter(context,  recycler_custdetails, listCustomer);
        recycler_custdetails.setAdapter(cusomerDetailsAdapter);
    }
}
