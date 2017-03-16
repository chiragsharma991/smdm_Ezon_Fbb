package apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.Tab_fragment;

import android.view.View;

/**
 * Created by pamrutkar on 10/03/17.
 */

public interface OnScanBarcode {

    void onScan(View view, int position, String check, TransferDetailsAdapter transferDetailsAdapter);

}
