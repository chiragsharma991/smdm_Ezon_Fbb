package apsupportapp.aperotechnologies.com.designapp;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

/**
 * This activity has a margin.
 */
public class SmallCaptureActivity extends CaptureActivity {
    @Override
    protected CompoundBarcodeView initializeContent() {
        setContentView(R.layout.capture_small);
        return (CompoundBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
