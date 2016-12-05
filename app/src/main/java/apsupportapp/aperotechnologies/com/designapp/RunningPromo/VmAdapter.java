package apsupportapp.aperotechnologies.com.designapp.RunningPromo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.R;


public class VmAdapter extends BaseAdapter {
    Context context;
    int coordinate_gridview;
    private LayoutInflater mInflater;

    ArrayList<String> arrayList;
    public VmAdapter(Context context, int coordinate_gridview, ArrayList<String> arrayList) {
        this.context=context;
        this.coordinate_gridview=coordinate_gridview;
        this.arrayList=arrayList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_vm_child, null);

           // holder.text1 = (TextView) convertView.findViewById(R.id.txtDays);
            holder.imgView = (ImageView) convertView.findViewById(R.id.imageView1);


            convertView.setTag(holder);

        } else {
            holder=(ViewHolder)convertView.getTag();

        }
        Log.e("position is", String.valueOf(+position));

        Picasso.with(context)
                .load(arrayList.get(position))
                .into(holder.imgView);


        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,VmPreview.class);
                Log.e("vm adapter position is", String.valueOf(+position));
                intent.putExtra("VM_ADP",position);
                context.startActivity(intent);


            }
        });




        return convertView;
    }



    public class ViewHolder

    {
        ImageView imgView;
        //TextView text1;

    }
}
