/*****************************************************************************
* Copyright (C) 2017-2018 Hanson Yu  All rights reserved.
------------------------------------------------------------------------------
* File Module		: 	BluetoothActivity.java
* Description		: 	BluetoothActivity operation center
* Created			: 	2017.06.16.
* Author			: 	Yu Weifeng
* Function List 		: 	
* Last Modified 	: 	
* History			: 	
******************************************************************************/


package com.example.user.app_bluetooth;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/*****************************************************************************
-Class			: MainActivity
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/19	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
public class BluetoothActivity extends Activity
{
	Context mContext;
	private Button btConnect=null;
	private TextView tvReadView=null;
    private BleDevice pBleDevice=null;
    private static String uuidQppService = "0000ff92-0000-1000-8000-00805f9b34fb";
    protected static String uuidQppCharWrite = "00009600-0000-1000-8000-00805f9b34fb";
	/*****************************************************************************
	-Class			: MainActivity
	-Description	: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/19	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	class ConnectListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			pBleDevice=new BleDevice(BluetoothMsg.BluetoothRemoteDevice,mContext,uuidQppService);
			if(null==pBleDevice)
			{
				Toast.makeText(getApplicationContext(), "设备未连接!", Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(false==pBleDevice.connect())
				{
					Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
					
					BluetoothMsg.BleRemoteDevice=pBleDevice;

					Intent in=new Intent(BluetoothActivity.this,LogActivity.class);
					startActivity(in);
				}
			}
		}
	}
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetootch_activity);
		mContext = this;
		btConnect = (Button) findViewById(R.id.BUTTONSEND);
		btConnect.setOnClickListener(new ConnectListener());
        tvReadView = (TextView) findViewById(R.id.textView);
	}
	@Override
	protected  void onResume()
	{
        if(null==BluetoothMsg.BluetoothRemoteDevice)
        {
            Toast.makeText(getApplicationContext(), "未扫描到BLE设备", Toast.LENGTH_SHORT).show();
        }
        else
        {
			tvReadView.setText("设备名称："+BluetoothMsg.BluetoothRemoteDevice.getName()+"\r\n");
			tvReadView.append("MAC："+BluetoothMsg.BluetoothRemoteDevice.getAddress()+"\r\n");
        }
		super.onResume();
	}
	@Override
	protected void onDestroy()
	{
		if(null==pBleDevice)
		{
		}
		else
		{
			pBleDevice.disconnect();
			pBleDevice.close();
		}
	    super.onDestroy();
	}//发送数据
}
