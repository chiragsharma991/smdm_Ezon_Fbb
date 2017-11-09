package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport.Adapter.CustomerDetailsAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

public class CustDetailsActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recycler_custdetails;
    private Button btn_sort;
    private TextView txt_title;
    private RelativeLayout imageBtnBack1, relative_sort;
    private String from = "Basket";
    ArrayList<String> listCustomer;
    CustomerDetailsAdapter cusomerDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        getSupportActionBar().hide();
        context = this;
        initialise();
    }

    private void initialise() {

        listCustomer = new ArrayList<>();
        recycler_custdetails = (RecyclerView) findViewById(R.id.recycler_custdetails);
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        relative_sort = (RelativeLayout) findViewById(R.id.relative_sort);
        txt_title = (TextView) findViewById(R.id.txt_title);
        btn_sort = (Button) findViewById(R.id.btn_sort);

        if(from.equals("Basket"))
        {
            txt_title.setText("Customer Details");
            btn_sort.setBackgroundResource(R.mipmap.iconsort);
        }
        else
        {
            txt_title.setText("List of customers who have purchased in 0 to 7 Days");
            btn_sort.setBackgroundResource(R.mipmap.iconfilter);

        }

        int resId = R.anim.item_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recycler_custdetails.setLayoutManager(new LinearLayoutManager(context));
        recycler_custdetails.setLayoutAnimation(animation);
        cusomerDetailsAdapter = new CustomerDetailsAdapter(context,  recycler_custdetails, listCustomer);
        recycler_custdetails.setAdapter(cusomerDetailsAdapter);

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        relative_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
