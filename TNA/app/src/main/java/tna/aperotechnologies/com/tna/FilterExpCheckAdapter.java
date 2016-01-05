package tna.aperotechnologies.com.tna;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


//Filter expandable list view adapter. This is for line list filter list
public class FilterExpCheckAdapter extends BaseExpandableListAdapter
{

    private Context context;
    private ArrayList<String> optCodeParent;
    private ArrayList<ArrayList<ActivityOptions>> optCodeChild;
    private LayoutInflater inflater;
    int grpPos;
    String first;
    Configuration_Parameter m_config=Configuration_Parameter.getInstance();
    SharedPreferences sharedpreferences;
    private ArrayList array;
    ExpandableListView explvOptionCode;


    public FilterExpCheckAdapter(final Context context, ArrayList<String> optCodeParent, ArrayList<ArrayList<ActivityOptions>> optCodeChild, ExpandableListView explvOptionCode)
    {
        this.context = context;
		this.optCodeParent = optCodeParent;
        this.optCodeChild = optCodeChild;
        this.first=first;
        inflater = LayoutInflater.from(context);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.explvOptionCode = explvOptionCode;


        //Log.e("optCodeChild.size()"," "+optCodeChild.size());


    }

    public Object getChild(int groupPosition, int childPosition)
    {
        return optCodeChild.get( groupPosition ).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition)
    {
        return (long)( groupPosition*1024+childPosition );  // Max 1024 children per group
    }



    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        grpPos=groupPosition;
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.optcode_child_row, parent, false);

        final ActivityOptions c = (ActivityOptions)getChild(groupPosition, childPosition);


        final CheckBox cb = (CheckBox)v.findViewById(R.id.check1);
        //Here

        cb.setText(c.getActDesc());
        cb.setChecked(c.getActChecked());


        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    //Defined as a field in the adapter/fragment
                    CheckBox last = m_config.lastcheckedBox;
                    CheckBox current = (CheckBox) view.findViewById(R.id.check1);
                    if (last != null) {
                        Log.e("last ", " " + last.isChecked() + " " + current.isChecked());
                        if (last.isChecked()) {
                            last.setChecked(true);
                        }


                        if (current.isChecked() == false) {
                            current.setChecked(false);
                            m_config.OptCodePar = "";
                            m_config.OptCodeChild = "Option Code";
                            current = null;
                        }

                        if (current != null) {
                            current.setChecked(false);
                        }


                    } else {
                        current.setChecked(true);
                        m_config.OptCodePar = optCodeChild.get(groupPosition).get(childPosition).getActId();
                        m_config.OptCodeChild = optCodeChild.get(groupPosition).get(childPosition).getActDesc();

                    }

                    //Log.e("isSelected count ", " " + current.getText() + "");
                    //Log.e(" ", " " + optCodeChild.get(groupPosition).get(childPosition).getActId());
                    m_config.lastcheckedBox = current;





                /* how to deselect previous checkbox and select current checkbox
                CheckBox last = m_config.checkedBox;  //Defined as a field in the adapter/fragment
                CheckBox current = (CheckBox) view.findViewById(R.id.check1);
                if (last != null) {
                    Log.e("last ", " " + last.isChecked() + " " + current.isChecked());
                    if(last.isChecked()){
                        last.setChecked(false);
                        }

                        current.setChecked(true);

                        m_config.OptCodePar = optCodeChild.get(groupPosition).get(childPosition).getActId();
                        m_config.OptCodeChild = optCodeChild.get(groupPosition).get(childPosition).getActDesc();
                        m_config.checkedBox = current;

                    }
                 */


                }
            }

            );

            return v;
        }

    public int getChildrenCount(int groupPosition)
    {
        return optCodeChild.get( groupPosition ).size();
    }

    public Object getGroup(int groupPosition)
    {
        return optCodeParent.get( groupPosition );
    }

    public int getGroupCount()
    {
        return optCodeParent.size();
    }

    public long getGroupId(int groupPosition)
    {
        return (long)( groupPosition*1024 );  // To be consistent with getChildId
    } 

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.optcode_group_row, parent, false);
        String gt = (String)getGroup(groupPosition);
        LinearLayout layoutparent = (LinearLayout)v.findViewById(R.id.layoutparent);
		TextView OptCodeParentValue = (TextView)v.findViewById( R.id.childname );
		if( gt != null ){
            OptCodeParentValue.setText(gt);
        }

        for(int k=0;k<optCodeParent.size();k++)
        {
            explvOptionCode.expandGroup(k);

        }

        layoutparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                return;
            }
        });



        return v;
    }

    public boolean hasStableIds()
    {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    } 

    public void onGroupCollapsed (int groupPosition) {} 
    public void onGroupExpanded(int groupPosition) {}
}
