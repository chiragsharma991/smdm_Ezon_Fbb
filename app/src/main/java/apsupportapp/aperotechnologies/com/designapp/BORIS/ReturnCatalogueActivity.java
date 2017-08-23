package apsupportapp.aperotechnologies.com.designapp.BORIS;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

public class ReturnCatalogueActivity extends AppCompatActivity {

    private RecyclerView recycler_view_return_catalogue;
    public ReturnAdapter return_adapter;
    private Context context;
    private String store;
    private SharedPreferences sharedPreferences;
    private TextView storedescription;
    private RelativeLayout imageBtnBack1;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_catalogue);
        getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        initialiseUI();
    }

    private void initialiseUI() {

        storedescription = (TextView) findViewById(R.id.txtStoreCode);
        store = sharedPreferences.getString("storeDescription", "");
        storedescription.setText(store);
        imageBtnBack1 = (RelativeLayout) findViewById(R.id.imageBtnBack1);

        if(getIntent().getExtras().getString("from") != null )
        {
            status = getIntent().getExtras().getString("status");
        }

        recycler_view_return_catalogue = (RecyclerView) findViewById(R.id.recycler_view_return_catalogue);

        recycler_view_return_catalogue.setLayoutManager(new LinearLayoutManager(context));
        return_adapter = new ReturnAdapter(context, recycler_view_return_catalogue, status);
        recycler_view_return_catalogue.setAdapter(return_adapter);

        imageBtnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}
