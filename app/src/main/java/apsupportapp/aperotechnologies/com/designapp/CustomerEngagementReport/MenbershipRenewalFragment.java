package apsupportapp.aperotechnologies.com.designapp.CustomerEngagementReport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by pamrutkar on 09/11/17.
 */
public class MenbershipRenewalFragment extends Fragment
{
    private Context context;
    private View v;
    private RecyclerView expiring_list,exhaust_list;
    private String[]listdata={"Title one","Title Two","Title three","Title four","a","c","c"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        View view = inflater.inflate(R.layout.fragment_membershiprenwal, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        initialiseUI(v);
    }

    private void initialiseUI(View v) {

        expiring_list= (RecyclerView)v.findViewById(R.id.expiring_list);
        exhaust_list= (RecyclerView)v.findViewById(R.id.exhaust_list);
        setview();


    }

    public void setview(){

        expiring_list.setLayoutManager(new GridLayoutManager(context,2));
        ExpiringAdapter expiringAdapter = new ExpiringAdapter(context, listdata);
        expiring_list.setAdapter(expiringAdapter);

        exhaust_list.setLayoutManager(new GridLayoutManager(context,2));
        ExhaustAdapter exhaustAdapter = new ExhaustAdapter(context, listdata);
        exhaust_list.setAdapter(exhaustAdapter);


    }


    public class ExpiringAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



        private final String[] listdata;
        private Context context;
        Gson gson;


        public ExpiringAdapter(Context context, String[] listdata) {
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

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                // title = (TextView)itemView.findViewById(R.id.title);

            }

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,CustDetailsActivity.class);
                intent.putExtra("from","Recency");
                startActivity(intent);
            }
        }

    }






    public class ExhaustAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



        private final String[] listdata;
        private Context context;
        Gson gson;


        public ExhaustAdapter(Context context, String[] listdata) {
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

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                // title = (TextView)itemView.findViewById(R.id.title);

            }

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,CustDetailsActivity.class);
                intent.putExtra("from","Recency");
                startActivity(intent);
            }
        }

    }




}
