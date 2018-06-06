package com.ntucap.itcm.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntucap.itcm.R;

public class SettingsActivity extends ITCMActivity implements View.OnClickListener{

    private static final int REQUEST_ENABLE_BT = 1;

    boolean mBluetoothOn;

    LinearLayout mBluetoothLayout;
    BluetoothAdapter mBluetoothAdapter;
    TextView mBluetoothText;
    ImageView mIvBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mIvBackBtn = (ImageView) findViewById(R.id.iv_nav_act_settings);
        mBluetoothLayout = (LinearLayout) findViewById(R.id.ll_bluetooth_act_settings);
        mBluetoothText = (TextView) findViewById(R.id.tv_bluetooth_act_settings);
        bindListeners();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            toast("Your Device Does Not Support Bluetooth");
            mBluetoothOn = mBluetoothAdapter.isEnabled();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        checkBluetoothState();
    }

    protected void bindListeners() {
        mIvBackBtn.setOnClickListener(this);
        mBluetoothLayout.setOnClickListener(this);
    }


    private void checkBluetoothState() {
        mBluetoothOn = getBluetoothState();
        mBluetoothText.setText(mBluetoothOn ? "On" : "Off");
    }

    public boolean getBluetoothState() {
        if(mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_bluetooth_act_settings:

                if (mBluetoothAdapter!= null) {
                    if(!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    } else {
                        mBluetoothAdapter.disable();
                    }
                    mBluetoothOn = !mBluetoothOn;
                    mBluetoothText.setText(mBluetoothOn ? "On" : "Off");
                }
                break;
            case R.id.iv_nav_act_settings:
                finish();
                break;
        }
    }
}
