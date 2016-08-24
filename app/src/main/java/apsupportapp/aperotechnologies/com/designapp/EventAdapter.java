package apsupportapp.aperotechnologies.com.designapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by ifattehkhan on 05/08/16.
 */
public class EventAdapter extends BaseAdapter{
    DashBoardActivity dashBoardActivity;
    Integer[] listItem;
    public EventAdapter(DashBoardActivity dashBoardActivity, Integer[] listItem) {
        this.dashBoardActivity=dashBoardActivity;
        this.listItem=listItem;

    }

    @Override
    public int getCount() {
        return listItem.length;
    }

    @Override
    public Object getItem(int position) {
        return listItem[position];
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
            view.imgView.setImageResource(listItem[position]);

            participentView.setTag(view);


        }
        else
        {
            view = (ViewHolder) participentView.getTag();
        }



//        view.imgView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//                notifyDataSetChanged();
//
//
//            }
//        });

        Log.d("count:  "," "+position);
        return participentView;
    }

    public static class ViewHolder
    {
        ImageView imgView;

    }
}
