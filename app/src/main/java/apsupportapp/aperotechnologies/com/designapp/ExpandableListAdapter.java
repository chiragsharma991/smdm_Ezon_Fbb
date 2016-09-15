package apsupportapp.aperotechnologies.com.designapp;

/**
 * Created by dbhat on 14-03-2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import apsupportapp.aperotechnologies.com.designapp.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

   Context _context;
   List<String> _listDataHeader; // header titles
    // child data in format of header title, child title

    HashMap<String, List<String>> _listDataChild;

     ChildViewHolder childViewHolder;
     GroupViewHolder groupViewHolder;

    private String groupText;
    private String childText;

    private HashMap<Integer, boolean[]> mChildCheckStates;


    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        mChildCheckStates=new HashMap<Integer, boolean[]>();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView( int groupPosition,  int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);



            convertView = infalInflater.inflate(R.layout.pfilter_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);


        txtListChild.setText(childText);
//        final int mGroupPosition = groupPosition;
//        final int mChildPosition = childPosition;
//        //  I passed a text string into an activity holding a getter/setter
//        //  which I passed in through "ExpListChildItems".
//        //  Here is where I call the getter to get that text
//        childText = getChild(mGroupPosition, mChildPosition);
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) this._context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.pfilter_list_item, null);
//            childViewHolder = new ChildViewHolder();
//            childViewHolder.mChildText = (TextView) convertView
//                    .findViewById(R.id.lblListItem);
//            childViewHolder.mCheckBox = (CheckBox) convertView
//                    .findViewById(R.id.lstcheckBox);
//
//            convertView.setTag(R.layout.pfilter_list_item, childViewHolder);
//
//        } else {
//
//            childViewHolder = (ChildViewHolder) convertView
//                    .getTag(R.layout.pfilter_list_item);
//        }
//
//        childViewHolder.mChildText.setText(childText);
//
//		/*
//		 * You have to set the onCheckChangedListener to null
//		 * before restoring check states because each call to
//		 * "setChecked" is accompanied by a call to the
//		 * onCheckChangedListener
//		*/
//        childViewHolder.mCheckBox.setOnCheckedChangeListener(null);
//
//        if (mChildCheckStates.containsKey(mGroupPosition)) {
//			/*
//			 * if the hashmap mChildCheckStates<Integer, Boolean[]> contains
//			 * the value of the parent view (group) of this child (aka, the key),
//			 * then retrive the boolean array getChecked[]
//			*/
//            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
//
//            // set the check state of this position's checkbox based on the
//            // boolean value of getChecked[position]
//            childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);
//
//        } else {
//
//			/*
//			 * if the hashmap mChildCheckStates<Integer, Boolean[]> does not
//			 * contain the value of the parent view (group) of this child (aka, the key),
//			 * (aka, the key), then initialize getChecked[] as a new boolean array
//			 *  and set it's size to the total number of children associated with
//			 *  the parent group
//			*/
//            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];
//
//            // add getChecked[] to the mChildCheckStates hashmap using mGroupPosition as the key
//            mChildCheckStates.put(mGroupPosition, getChecked);
//
//            // set the check state of this position's checkbox based on the
//            // boolean value of getChecked[position]
//            childViewHolder.mCheckBox.setChecked(false);
//        }
//
//        childViewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//
//                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
//                    getChecked[mChildPosition] = isChecked;
//                    mChildCheckStates.put(mGroupPosition, getChecked);
//
//                } else {
//
//                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
//                    getChecked[mChildPosition] = isChecked;
//                    mChildCheckStates.put(mGroupPosition, getChecked);
//                }
//            }
//        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.pfilter_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
       // CheckBox checkbox= (CheckBox)convertView.findViewById(R.id.lstcheckBox);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
//        groupText = getGroup(groupPosition);
//
//        if (convertView == null) {
//
//            LayoutInflater inflater = (LayoutInflater) _context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.pfilter_list_group, null);
//            // Initialize the GroupViewHolder defined at the bottom of this document
//            groupViewHolder = new GroupViewHolder();
//            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.lblListHeader);
//            convertView.setTag(groupViewHolder);
//        } else {
//            groupViewHolder = (GroupViewHolder) convertView.getTag();
//        }
//
//        groupViewHolder.mGroupText.setText(groupText);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ChildViewHolder {
        TextView mChildText;
        CheckBox mCheckBox;
    }

    private class GroupViewHolder {
        TextView mGroupText;
    }
}

