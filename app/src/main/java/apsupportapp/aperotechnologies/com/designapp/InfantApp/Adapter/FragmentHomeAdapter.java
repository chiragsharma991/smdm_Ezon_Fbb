package apsupportapp.aperotechnologies.com.designapp.InfantApp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by rkanawade on 02/11/17.
 */

public class FragmentHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private RecyclerView listHome;


    public FragmentHomeAdapter(Context context, RecyclerView listHome) {
        this.context = context;
        this.listHome = listHome;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v0 = inflater.inflate(R.layout.adapter_home_fragment, parent, false);
        viewHolder = new HomeHolder(v0);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class HomeHolder extends RecyclerView.ViewHolder {

        ImageView img_home;


        public HomeHolder(View v) {
            super(v);
            img_home = (ImageView) v.findViewById(R.id.img_home);

        }


    }


}
