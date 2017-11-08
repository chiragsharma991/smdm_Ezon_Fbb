package apsupportapp.aperotechnologies.com.designapp.InfantApp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter.SubCategoryAdapter;
import apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter.SubCategory_childAdapter;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 02/11/17.
 */

public class SubCategory extends AppCompatActivity implements SubCategory_childAdapter.onclick{


    private View viewpart;
    private RecyclerView recycler_verticalView;
    private String[]listdata={"Title one","Title Two","Title three","Title four"};
    private Context context=this;
    private String TAG=this.getClass().getSimpleName();
    private SubCategoryAdapter subCategoryAdapter;
    private RelativeLayout hrl_BtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_subcategory);
        getSupportActionBar().hide();
        initialiseUI();
    }

    private void initialiseUI() {
        viewpart = findViewById(android.R.id.content);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        recycler_verticalView = (RecyclerView) findViewById(R.id.recycler_verticalView);
        recycler_verticalView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler_verticalView.setItemAnimator(new DefaultItemAnimator());
        subCategoryAdapter=new SubCategoryAdapter(context,listdata);
        recycler_verticalView.setAdapter(subCategoryAdapter);
        hrl_BtnBack = (RelativeLayout)findViewById(R.id.hrl_BtnBack);
        hrl_BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onclickPostion(int parent, int child , View view) {
        Log.e(TAG, "onclickPostion interface: "+parent+child );
       // subCategoryAdapter.notifyDataSetChanged();
        Log.e(TAG, "onclickPostion: "+parent);
        if(parent == 1)
        {
            Intent intent=new Intent(context,BabyCerealActivity.class);

            CardView sharedView = (CardView) view;
            String transitionName = "details";

            ActivityOptions transitionActivityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(SubCategory.this, sharedView, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());
            }else{

                startActivity(intent);
            }
        }
        else
        {
            Intent intent=new Intent(context,ProductDetails.class);

            CardView sharedView = (CardView) view;
            String transitionName = "details";

            ActivityOptions transitionActivityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(SubCategory.this, sharedView, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());
            }else{

                startActivity(intent);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
