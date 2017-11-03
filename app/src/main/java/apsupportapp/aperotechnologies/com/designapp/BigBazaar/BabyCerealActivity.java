package apsupportapp.aperotechnologies.com.designapp.BigBazaar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.BigBazaar.Adapter.CustomGrid;
import apsupportapp.aperotechnologies.com.designapp.R;


/**
 * Created by pamrutkar on 02/11/17.
 */

public class BabyCerealActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rel_back, rel_changeView;
    private LinearLayout sort_linear, filter_linear;
    Context context;
    private Button btn_grid, btn_list;
    private RecyclerView gridView;
    ArrayList<String> listProducts, listSort, listFilter;
    private ListView select_list;
    private AlertDialog dialog;
    private CustomGrid adapter;
    private String viewString;
    int apperanceTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_cereal);
        context = this;
        getSupportActionBar().hide();
        initialize_ui();
    }

    private void initialize_ui() {
        listProducts = new ArrayList<String>();
        sort_linear = (LinearLayout) findViewById(R.id.sort_linear);
        filter_linear = (LinearLayout) findViewById(R.id.filter_linear);
        rel_back = (RelativeLayout) findViewById(R.id.rel_back);
        rel_changeView = (RelativeLayout) findViewById(R.id.rel_changeView);
        rel_changeView.setTag(apperanceTag);
        btn_grid = (Button) findViewById(R.id.btn_grid);
//        btn_list = (Button) findViewById(R.id.btn_list);
        sort_linear.setOnClickListener(this);
        rel_changeView.setOnClickListener(this);
        rel_back.setOnClickListener(this);
        filter_linear.setOnClickListener(this);
        apperanceTag = (int)rel_changeView.getTag();
        apperanceTag = 0;
        gridView = (RecyclerView) findViewById(R.id.gridview);
        gridView.setLayoutManager(new GridLayoutManager(context, 2));
        viewString = "gridView";
        adapter = new CustomGrid(context, listProducts, viewString);
        gridView.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_linear:
                sortFunction();
                break;
            case R.id.rel_changeView:
                changeViewFunction();
                break;
            case R.id.filter_linear:
                filterFunction();
                break;
            case R.id.rel_back:
                onBackPressed();
                break;
        }

    }

    private void changeViewFunction()
    {
        if(apperanceTag == 0)
        {
            apperanceTag = 1;
            btn_grid.setBackgroundResource(R.mipmap.home);
            gridView.setLayoutManager(new LinearLayoutManager(context));
            viewString = "listView";
            adapter = new CustomGrid(context, listProducts, viewString);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
        else if(apperanceTag == 1)
        {
            apperanceTag = 0;
            btn_grid.setBackgroundResource(R.mipmap.barcodeicon);
            gridView.setLayoutManager(new GridLayoutManager(context,2));
            viewString = "gridView";
            adapter = new CustomGrid(context, listProducts, viewString);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }


    }

    private void filterFunction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.list_sort_filter, null);
        TextView txt_title = (TextView) v.findViewById(R.id.txt_title);
        txt_title.setText("Filter By");
        select_list = (ListView) v.findViewById(R.id.select_list);
        // search function for search store code from list.
        String[] filterList = new String[]{
                "Brands",
                "Discount",
                "Price",
                "Age",
                "Shop for",
                "Colors",
                "Material",
        };

//        spinnerAdapter = new Sort_filter_Adapter(arrayList, BabyCerealActivity.this);
//        select_list.setAdapter(spinnerAdapter);
//        spinnerAdapter.notifyDataSetChanged();

//        requestSortAPI();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, filterList);

        select_list.setDivider(null);
        // Assign adapter to ListView
        select_list.setAdapter(adapter);

        // ListView Item Click Listener
        select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) select_list.getItemAtPosition(position);
                dialog.dismiss();

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }

    private void sortFunction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.list_sort_filter, null);
        TextView txt_title = (TextView) v.findViewById(R.id.txt_title);
        txt_title.setText("Sort By");
        select_list = (ListView) v.findViewById(R.id.select_list);
        // search function for search store code from list.
        String[] sortList = new String[]{
                "New Arrivals",
                "Best Seller",
                "Highest Discount",
                "Price: Low to High",
                "Price: High to Low",
                "Name A to Z",
                "Name Z to A",
        };

//        spinnerAdapter = new Sort_filter_Adapter(arrayList, BabyCerealActivity.this);
//        select_list.setAdapter(spinnerAdapter);
//        spinnerAdapter.notifyDataSetChanged();

//        requestSortAPI();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, sortList);

        select_list.setDivider(null);
        // Assign adapter to ListView
        select_list.setAdapter(adapter);

        // ListView Item Click Listener
        select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) select_list.getItemAtPosition(position);
                dialog.dismiss();

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        builder.setView(v);
        dialog = builder.create();
        dialog.show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
