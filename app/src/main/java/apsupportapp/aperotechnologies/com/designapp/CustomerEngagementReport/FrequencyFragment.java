package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 08/11/17.
 */

public class FrequencyFragment extends Fragment implements TabLayout.OnTabSelectedListener
{
    private Context context;
    private View v;
    private TabLayout tabview;
    private String TAG=this.getClass().getSimpleName();
    private RecyclerView recycler_gridView;
    private FrequencyAdapter adapter;
    private String[]listdata={"Title one","Title Two","Title three","Title four"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();
        View view = inflater.inflate(R.layout.activity_cer_frequency, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        initialiseUI(v);
    }

    private void initialiseUI(View view) {

        recycler_gridView = (RecyclerView)v.findViewById(R.id.list);
        recycler_gridView.setLayoutManager(new GridLayoutManager(context,2));
        recycler_gridView.setItemAnimator(new DefaultItemAnimator());
        adapter=new FrequencyAdapter(context,listdata);
        recycler_gridView.setAdapter(adapter);
        tabview = (TabLayout)view.findViewById(R.id.tabview);
        tabview.addTab(tabview.newTab().setText("30 Days"));
        tabview.addTab(tabview.newTab().setText("60 Days"));
        tabview.addTab(tabview.newTab().setText("90 Days"));
        tabview.setOnTabSelectedListener(this);


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.i(TAG, "onTabSelected: ");
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }




    public class FrequencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



        private final String[] listdata;
        private Context context;
        Gson gson;


        public FrequencyAdapter (Context context, String[] listdata) {
            this.context = context;
            this.listdata = listdata;
            gson = new Gson();
        }


        @Override
        public int getItemCount() {
            return listdata.length;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cer_recency_gridchild, parent, false);
            return new ViewHolder(v);


        }


        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof RecencyFrequencyFragment.RecencyAdapter.ViewHolder) {
                if(position <= listdata.length) {
                    //  ((ViewHolder) holder).title.setText(listdata[position]);


                }

            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);

                // title = (TextView)itemView.findViewById(R.id.title);

            }
        }

    }
}