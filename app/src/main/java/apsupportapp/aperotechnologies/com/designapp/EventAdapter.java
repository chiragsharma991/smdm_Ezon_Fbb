package apsupportapp.aperotechnologies.com.designapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class EventAdapter extends BaseAdapter{
    DashBoardActivity dashBoardActivity;
    ArrayList<String> eventUrlList;
    public EventAdapter(DashBoardActivity dashBoardActivity, ArrayList<String> eventUrlList) {
        this.dashBoardActivity=dashBoardActivity;
        this.eventUrlList=eventUrlList;

    }
    @Override
    public int getCount() {
        return eventUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View participentView = convertView;
        final  ViewHolder view;
        //  LayoutInflater inflater;
        if (participentView == null)// || participentView.getTag() == null)
        {
            view = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(dashBoardActivity.LAYOUT_INFLATER_SERVICE);
            participentView = inflater.inflate(R.layout.list_row, null);

            view.imgView = (ImageView) participentView.findViewById(R.id.imageview);
            Glide.with(dashBoardActivity)
                    .load(eventUrlList.get(position))
                    .into(view.imgView);

            participentView.setTag(view);
        }
        else
        {
            view = (ViewHolder) participentView.getTag();
        }
        return participentView;
    }

    public static class ViewHolder
    {
        ImageView imgView;

    }
}
