/*****************************************************************************
* Copyright (C) 2017-2018 Hanson Yu  All rights reserved.
------------------------------------------------------------------------------
* File Module		: 	MainActivity.java
* Description		: 	MainActivity operation center
* Created			: 	2017.06.16.
* Author			: 	Yu Weifeng
* Function List 		: 	
* Last Modified 	: 	
* History			: 	
******************************************************************************/


package com.example.user.app_bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/*****************************************************************************
-Class			: MainActivity
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/19	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
public class MainActivity extends Activity
{
    private Button btConnectButton=null;
    private Button btStopScan=null;
    private TextView tvForView=null;
    private ProgressBar pbScan=null;
    /**搜索BLE终端*/
    private BluetoothAdapter pBluetoothAdapter;
    private BluetoothManager pBluetoothManager;
	private Context mContext;
    private BluetoothDevice mDevice;
    private String BleAddress="BleAddress:";
    BluetoothLeScanner mScanner=null;
    /*****************************************************************************
    -Class			: ConnectButtonListener
    -Description	: 
    * Modify Date	  Version		 Author 		  Modification
    * -----------------------------------------------
    * 2017/06/19	  V1.0.0		 Yu Weifeng 	  Created
    ******************************************************************************/
    class ConnectButtonListener implements View.OnClickListener
    {
        
	/*****************************************************************************
	-Fuction		: onClick
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/19	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
        public void onClick(View v)
        {
            //判断设备是否支持BLE,
            if(v.getId()==R.id.CONNECTBUTTON)
            {
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
                {
                    Toast.makeText(getApplicationContext(),"您的手机不支持ble功能",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    pBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
                    pBluetoothAdapter = pBluetoothManager.getAdapter();
                    if(null==pBluetoothAdapter)
                    {
                        Toast.makeText(getApplicationContext(), "没有蓝牙设备", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (!pBluetoothAdapter.isEnabled())
                        {
                            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);	// 弹对话框的形式提示用户开启蓝牙
                            //mBluetoothAdapter.enable(); // 强制开启，不推荐使用
                        }
                        else
                        {
                            //Toast.makeText(getApplicationContext(), "蓝牙已开启", Toast.LENGTH_SHORT).show();
                            mScanner = pBluetoothAdapter.getBluetoothLeScanner();  // android5.0把扫描方法单独弄成一个对象了
                            mScanner.stopScan(mScanCallback);   // 停止扫描
                            mScanner.startScan(mScanCallback);  // 开始扫描
                            pbScan.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
            else if(v.getId()==R.id.StopScan)
            {
                if(null==mScanner)
                {
                    Toast.makeText(getApplicationContext(), "请先开始扫描", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mScanner.stopScan(mScanCallback);   // 停止扫描
                    BluetoothMsg.BluetoothRemoteDevice=mDevice;
                    pbScan.setVisibility(View.GONE);
                    Intent in=new Intent(MainActivity.this,BluetoothActivity.class);
                    startActivity(in);
                }

            }
            else
            {
            }
        }

    }
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            // callbackType：确定这个回调是如何触发的
            // result：包括4.3版本的蓝牙信息，信号强度rssi，和广播数据scanRecord
            mDevice=result.getDevice();
	    if(null==mDevice||null==mDevice.getName())
	    {
	    }
	    else
	    {
	            if(mDevice.getName().equals("FireBLE"))
	            {
	                mScanner.stopScan(mScanCallback);   // 停止扫描
	                BluetoothMsg.BluetoothRemoteDevice=mDevice;
	                pbScan.setVisibility(View.GONE);
	                Intent in=new Intent(MainActivity.this,BluetoothActivity.class);
	                startActivity(in);
	            }
		    else{
		    }
	    }

        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            // 批量回调，一般不推荐使用，使用上面那个会更灵活
        }
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(getApplicationContext(), "扫描失败", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btConnectButton=(Button)findViewById(R.id.CONNECTBUTTON);
        btConnectButton.setOnClickListener(new ConnectButtonListener());
        btStopScan=(Button)findViewById(R.id.StopScan);
        btStopScan.setOnClickListener(new ConnectButtonListener());
        tvForView=(TextView)findViewById(R.id.CONNECTVIEW);
        pbScan=(ProgressBar)findViewById(R.id.progressBarScan);
        pbScan.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
