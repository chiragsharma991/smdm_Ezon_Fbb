package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.ToDo_Modal;
import apsupportapp.aperotechnologies.com.designapp.R;

/**
 * Created by csuthar on 06/03/17.
 */

public class StockDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private final ProgressBar detailProcess;
    private  HashMap<Integer, ArrayList<ToDo_Modal>> HashMapSubChild;
    private  Context context;
    private  ArrayList<ToDo_Modal> list;
    private  boolean[] Toggle;
    public  boolean[] HeadercheckList;  //list for check header
    public OnPress onPressInterface;
    private Set<Pair<Integer, Integer>> CheckedItems ;
    public boolean[] visibleItems;
    public ArrayList<String> selectedOptionList,selectedSizeList;
    DetailsHeaderChildAdapter detailsHeaderChildAdapter;

    public StockDetailsAdapter(ArrayList<ToDo_Modal> list, HashMap<Integer, ArrayList<ToDo_Modal>> hashmapList, Context context, ProgressBar detailProcess) {
        this.list=list;
        this.context=context;//
        Toggle= new boolean[list.size()];
        HeadercheckList= new boolean[list.size()];
        onPressInterface=(OnPress)context;
        HashMapSubChild=hashmapList;
        CheckedItems=new HashSet<Pair<Integer,Integer>>();
        visibleItems=new boolean[list.size()];
        this.detailProcess=detailProcess;
        selectedOptionList = new ArrayList<String>();
        selectedSizeList = new ArrayList<String>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_stock_details_child, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if(holder instanceof Holder)
        {
            if(position < list.size())
            {
                HandlePositionOnSet(holder,position);

                ((Holder)holder).Detail_Soh.setText(""+Math.round(list.get(position).getStkOnhandQty()));
                ((Holder)holder).Detail_optionLevel.setText(list.get(position).getLevel());
                ((Holder)holder).Detail_reqQty.setText(""+Math.round(list.get(position).getStkOnhandQtyRequested()));
                ((Holder)holder).Detail_Git.setText(""+Math.round(list.get(position).getStkGitQty()));
                ((Holder)holder).Detail_AviQty.setText(""+Math.round(list.get(position).getStkQtyAvl()));
                ((Holder)holder).Detail_sellThru.setText(""+Math.round(list.get(position).getSellThruUnits()));

                ((Holder)holder).Detail_headerCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(((CheckBox)view).isChecked())
                        {
                            //Header check is enable when view is open
                            HeadercheckList[position]=true;
                            visibleItems[position]=true;
                            notifyItemChanged(position);
                        }else
                        {
                            HeadercheckList[position]=false;
                            visibleItems[position]=true;
                            notifyItemChanged(position);
                        }
                    }
                });
                ((Holder)holder).Detail_optionLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(detailProcess.getVisibility()==View.GONE)
                        {
                            if(Toggle[position]==true)
                            {
                                Toggle[position]=false;
                                notifyDataSetChanged();

                            }
                            else
                            {
                                Toggle[position]=true;
                                if(HashMapSubChild.get(position).isEmpty())
                                {
                                    onPressInterface.onPress(position);

                                }
                                else
                                {
                                    notifyDataSetChanged();

                                }
                            }
                        }
                    }
                });
                detailsHeaderChildAdapter =new DetailsHeaderChildAdapter(visibleItems,HashMapSubChild,HeadercheckList,CheckedItems,context,position,StockDetailsAdapter.this);
                ((Holder)holder).detailsLinear.setAdapter(detailsHeaderChildAdapter);
            }
        }
    }

    private void HandlePositionOnSet(RecyclerView.ViewHolder holder, int position)
    {
        if(Toggle[position])
        {
            ((Holder)holder).Sizeslayout.setVisibility(View.VISIBLE);

        }else
        {
            ((Holder)holder).Sizeslayout.setVisibility(View.GONE);

        }
        if(HeadercheckList[position])
        {
            ((Holder)holder).Detail_headerCheck.setChecked(true);

        }
        else
        {
            ((Holder)holder).Detail_headerCheck.setChecked(false);

        }
    }


    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder
    {

        private final TextView Detail_Soh,Detail_reqQty,Detail_Git,Detail_AviQty,Detail_sellThru;
        private TextView Detail_optionLevel;
        public LinearLayout Sizeslayout;
        public  CheckBox Detail_headerCheck;
        protected RecyclerView detailsLinear;
        public static View view_border;
        public Holder(View itemView)
        {
            super(itemView);
            Detail_optionLevel=(TextView)itemView.findViewById(R.id.detail_optionLevel);
            Detail_reqQty=(TextView)itemView.findViewById(R.id.detail_reqQty);
            Detail_Soh=(TextView)itemView.findViewById(R.id.detail_Soh);
            Detail_Git=(TextView)itemView.findViewById(R.id.detail_Git);
            Detail_AviQty=(TextView)itemView.findViewById(R.id.detail_AviQty);
            Sizeslayout=(LinearLayout)itemView.findViewById(R.id.detail_size);
            view_border = (View)itemView.findViewById(R.id.view_border);
            detailsLinear=(RecyclerView)itemView.findViewById(R.id.details_headerChild);
            Detail_headerCheck=(CheckBox) itemView.findViewById(R.id.detail_headerCheck);
            Detail_sellThru = (TextView)itemView.findViewById(R.id.detail_sellThru);
        }
    }

    public JSONArray OnSubmit(String Mc_name_code, String prodLevel3Desc, String store_code) {


       for(int i = 0;i<list.size();i++)
       {

           //for size list
//           if(!HashMapSubChild.isEmpty())
//           {
               for(int j = 0; j<HashMapSubChild.get(i).size();j++)
               {
                   Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(i,j);
                   if(CheckedItems.contains(Tag))
                   {
                       selectedSizeList.add(HashMapSubChild.get(i).get(j).getLevelCode());
                   }

               }
//           }
//           else {
               // for option List
               if (HeadercheckList[i]) {
                   selectedOptionList.add((list.get(i).getLevelCode()));

               }
           }


//       }
        // for option List converted to string array to string
        String[] optionArray = (String[]) selectedOptionList.toArray(new String[0]);
        String optionList = Arrays.toString(optionArray);
        optionList = optionList.replace("[", "");
        optionList = optionList.replace("]", "");
        optionList = optionList.replace(", ", ",");

        // for size List converted to string array to string
        String[] sizeArray = (String[]) selectedSizeList.toArray(new String[0]);
        String sizeList = Arrays.toString(sizeArray);
        sizeList = sizeList.replace("[", "");
        sizeList = sizeList.replace("]", "");
        sizeList = sizeList.replace(", ", ",");

        Mc_name_code = Mc_name_code.replaceAll("%20"," ");
        prodLevel3Desc = prodLevel3Desc.replaceAll("%20"," ");

        JSONArray jsonarray=new JSONArray();
        try
        {
            JSONObject obj = new JSONObject();
            if(!sizeList.equals(""))
            {

                obj.put("option",optionList);
                obj.put("prodAttribute4",sizeList);
                obj.put("prodLevel6Code",Mc_name_code);//MCCodeDesc
                obj.put("prodLevel3Code",prodLevel3Desc);//prodLevel3Desc
                //   obj.put("deviceId",deviceId);
                obj.put("storeCode",store_code);
                jsonarray.put(obj);

            }
            else
            {

                obj.put("option",optionList);
//                obj.put("prodAttribute4",sizeList);
                obj.put("prodLevel6Code",Mc_name_code);//MCCodeDesc
                obj.put("prodLevel3Code",prodLevel3Desc);//prodLevel3Desc
                //   obj.put("deviceId",deviceId);
                obj.put("storeCode",store_code);
                jsonarray.put(obj);

            }



//                if(HashMapSubChild.)   //fst start with subchild if no one select in subchild then it will go header selection.
//                {
//
//                        Pair<Integer, Integer> Tag = new Pair<Integer, Integer>(i,j);
//                        if(CheckedItems.contains(Tag))
//                        {
//                        }
//
//                }
//                else
//                {
//                    if(HeadercheckList[i])
//                    {
//
//                        JSONObject obj = new JSONObject();
////                        obj.put("option",list.get(i).getLevel());
////                        obj.put("prodLevel6Code",MCCodeDesc);
//                        obj.put("option",optionList);
////                        obj.put("prodAttribute4",HashMapSubChild.get(i).get(j).getLevel());
//                        obj.put("prodLevel6Code",MCCodeDesc);//MCCodeDesc
//                        obj.put("prodLevel3Code",prodLevel3Desc);//prodLevel3Desc
//                        //  obj.put("deviceId",deviceId);
//                        jsonarray.put(obj);
//                      }
//                }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
       return jsonarray;
    }

}
