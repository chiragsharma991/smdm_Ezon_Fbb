package apsupportapp.aperotechnologies.com.designapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import amobile.android.barcodesdk.api.IWrapperCallBack;
import amobile.android.barcodesdk.api.Wrapper;


/**
 * Created by pamrutkar on 22/08/16.
 */
public class ScannerActivity extends AppCompatActivity implements IWrapperCallBack, View.OnClickListener {

    private Wrapper m_wrapper = null;

    private TextView m_text;

    private Button m_toggle;

    private Button m_turnOn;

    private Button m_turnOff;

    private Button m_setting;

    private Button m_camera;

    private Button m_serviceExit;

    private Button m_scan;

    private Button m_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        m_text = (TextView) findViewById(R.id.txtText1);

       /* m_toggle = (Button) findViewById(R.id.btnToggleFlyButton);
        m_toggle.setOnClickListener(this);

        m_turnOn = (Button) findViewById(R.id.btnTurnOnFlyButton);
        m_turnOn.setOnClickListener(this);

        m_turnOff = (Button) findViewById(R.id.btnTurnOffFlyButton);
        m_turnOff.setOnClickListener(this);

        m_setting = (Button) findViewById(R.id.btnSetting);
        m_setting.setOnClickListener(this);

        m_camera = (Button) findViewById(R.id.btnCamera);
        m_camera.setOnClickListener(this);

        m_serviceExit = (Button) findViewById(R.id.btnServiceExit);
        m_serviceExit.setOnClickListener(this);*/

        m_scan = (Button) findViewById(R.id.btnScan);
        m_scan.setOnClickListener(this);

        m_exit = (Button) findViewById(R.id.btnExit);
        m_exit.setOnClickListener(this);

        m_wrapper = new Wrapper(this);




        IntentFilter filter = new IntentFilter("BROADCAST_BARCODE");
        registerReceiver(m_brc, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (m_wrapper != null) {
            if (m_wrapper.Open()) {
                m_wrapper.SetDispathBarCode(false);
                m_wrapper.SetLightMode2D(amobile.android.barcodesdk.api.Wrapper.LightMode2D.mix);
                m_wrapper.SetTimeOut(10);
            } else {
                m_wrapper = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(m_brc);

        if (m_wrapper != null) {
            m_wrapper.Close();
            m_wrapper = null;
        }

        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.btnToggleFlyButton:
                if (m_wrapper != null) {
                    m_wrapper.ToggleFlyButton(this);
                }
                break;
            case R.id.btnTurnOnFlyButton:
                if (m_wrapper != null) {
                    m_wrapper.TurnOnFlyButton(this, true);
                }
                break;
            case R.id.btnTurnOffFlyButton:
                if (m_wrapper != null) {
                    m_wrapper.TurnOnFlyButton(this, false);
                }
                break;
            case R.id.btnSetting:
                if (m_wrapper != null) {
                    m_wrapper.ShowBarcodeScanWizardSetting(this);
                }
                break;
            case R.id.btnCamera:
                if (m_wrapper != null) {
                    m_wrapper.Close();

                    m_wrapper.SetCameraAppStart(this);
                }
                break;
            case R.id.btnServiceExit:
                if (m_wrapper != null) {
                    // m_wrapper.Close();
                    m_wrapper.SetBarcodeScanWizardExit(this);
                }
                break;*/
            case R.id.btnScan:
                if (m_wrapper != null && m_wrapper.IsOpen()) {
                    m_wrapper.Stop();
                    m_wrapper.Scan();
                }
                break;
            case R.id.btnExit:
                finish();
                break;
        }
    }

    @Override
    public void onDataReady(String strData) {
        byte[] bytes = strData.getBytes();
        m_text.setText(strData);
    }

    private BroadcastReceiver m_brc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String s = intent.getStringExtra("BROADCAST_BARCODE_STRING");
                if (s != null) {
                    m_text.setText(s);
                }
            }
        }
    };

    @Override
    public void onServiceConnected() {
        // do something after connecting
        int i = 0;
    }

    @Override
    public void onServiceDisConnected() {
        // do something after disconnecting
        int i = 0;
    }
}
