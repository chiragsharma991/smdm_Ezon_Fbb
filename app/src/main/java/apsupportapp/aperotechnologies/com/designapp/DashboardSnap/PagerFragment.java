package apsupportapp.aperotechnologies.com.designapp.DashboardSnap;

/**
 * Created by csuthar on 21/09/17.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.gson.Gson;
import apsupportapp.aperotechnologies.com.designapp.R;

public class PagerFragment extends Fragment {


    private ImageView Pager_image;
    private int position;
    private String TAG="PagerFragment";
    private Gson gson;
    private String listOfimage;

    int[] mResources = {
            R.mipmap.tkk_mobile_unit,
            R.mipmap.moto_e4_app,
    };

    public PagerFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.marketing_image_adapter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        position= getArguments().getInt("position");
        initView(view);
        setData(position);
    }

    private void setData(int position) {
        Pager_image.setImageResource(mResources[position]);

    }

    public void initView(View view) {
        Pager_image = (ImageView) view.findViewById(R.id.image_market);

    }


}

