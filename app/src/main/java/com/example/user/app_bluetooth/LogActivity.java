/*****************************************************************************
* Copyright (C) 2017-2018 Hanson Yu  All rights reserved.
------------------------------------------------------------------------------
* File Module		: 	LogActivity.java
* Description		: 	LogActivity operation center
* Created			: 	2017.06.23.
* Author			: 	Yu Weifeng
* Function List 		: 	
* Last Modified 	: 	
* History			: 	
******************************************************************************/

package com.example.user.app_bluetooth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/*****************************************************************************
-Class			: HandProtocolActivity
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/23	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
public class LogActivity extends Activity
{
	Context mContext;
	private TextView tvLogView=null;
	private EditText etSendText=null;
	private Button btEnableLog=null;
	private Button btSaveLog=null;
	private Button btClearLog=null;
	private CheckBox cbSelectAscii=null;
	private CheckBox cbSelectHex=null;
	private RadioGroup groupFormat=null;
	private RadioButton rbAscii=null;
	private RadioButton rbHex=null;
	private Button btLogSend=null;

	private boolean blUseHexFlag=false;
	private Handler pReadLogHandler=null;
	/*****************************************************************************
	-Class			: LogListener
	-Description	: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/19	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	class LogListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.buttonEnableLog:
				{
					Intent in=new Intent(LogActivity.this,HandProtocolActivity.class);
					startActivity(in);

					break;
				}
				case R.id.buttonSaveLog:
				{
					SimpleDateFormat formatter = new SimpleDateFormat ("-----yyyy年MM月dd日 HH:mm:ss-----\r\n");
					Date curDate    =   new Date(System.currentTimeMillis());//获取当前时间
					String strSaveLog = formatter.format(curDate);
					strSaveLog=strSaveLog.concat(tvLogView.getText().toString());
					saveLog(strSaveLog);
					break;
				}
				case R.id.buttonClearLog:
				{
					tvLogView.setText(null);
					break;
				}
				case R.id.buttonLogSend:
				{
					String inputText=etSendText.getText().toString();
					if(inputText.length()>0)
					{
					}
					else
					{
						inputText=new String("@@@");
					}
					BluetoothMsg.BleRemoteDevice.write(inputText.getBytes());
					break;
				}
				default:
				{
					Log.i("LogListener", "onClick err" + v.getId());
					break;
				}
			}
		}
	}
	class OnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener
	{
		public void onCheckedChanged(RadioGroup group, int checkId)
		{
			if(checkId==rbAscii.getId())
			{
				blUseHexFlag=false;
			}
			else if(checkId==rbHex.getId())
			{
				blUseHexFlag=true;
			}
			else
			{

			}

		}
	}
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_activity);
		mContext = this;
		tvLogView = (TextView) findViewById(R.id.textViewLog);
		etSendText = (EditText) findViewById(R.id.editTextSendContext);
		btEnableLog = (Button) findViewById(R.id.buttonEnableLog);
		btEnableLog.setOnClickListener(new LogListener());
		btSaveLog = (Button) findViewById(R.id.buttonSaveLog);
		btSaveLog.setOnClickListener(new LogListener());
		btClearLog= (Button) findViewById(R.id.buttonClearLog);
		btClearLog.setOnClickListener(new LogListener());
		btLogSend= (Button) findViewById(R.id.buttonLogSend);
		btLogSend.setOnClickListener(new LogListener());

		groupFormat = (RadioGroup)findViewById(R.id.radioGroupFormat);
		groupFormat.setOnCheckedChangeListener(new OnCheckedChangeListener());
		rbAscii=(RadioButton)findViewById(R.id.radioButtonAscii);
		rbHex=(RadioButton)findViewById(R.id.radioButtonHex);
	}
	@Override
	protected  void onResume()
	{
		super.onResume();
		pReadLogHandler= new Handler(new ReadLogHandler()); 
		BleCallBack oopBleCallBack=new BleCallBack();
		BluetoothMsg.BleRemoteDevice.setBLEDeviceCallback(oopBleCallBack);

	}
	@Override
	protected void onDestroy()
	{

	    super.onDestroy();
	}
	private void saveLog(String i_strLog)
	{
		String filePath = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) { // SD卡根目录的hello.text
			filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "BleToolLog.txt";
		} else  // 系统下载缓存根目录的hello.text
			filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "BleToolLog.txt";
		try
		{
			File file = new File(filePath);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file,true);
			//FileOutputStream outStream=openFileOutput("BleToolLog.txt",Context.MODE_APPEND);
			BufferedWriter mWriter=new BufferedWriter(new OutputStreamWriter(outStream));
			mWriter.write(i_strLog);
			mWriter.close();
		}
		catch (IOException e)
		{
			Log.i("LogActivity", "saveLog err"+e);
		}
	}
	private class ReadLogHandler implements Handler.Callback
	{
		public boolean handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0:
				{
					tvLogView.append((String) msg.obj);
					break;
				}
				default:
				{
					Log.i("ReadLogHandler", "ReadLogHandler err"+msg.what);
					break;
				}
			}
			return true;
		}
	}
	
	class BleCallBack implements BleDevice.BleDeviceCallback
	{
		public void onBleDeviceConnectionChange(BleDevice dev, int laststate, int newstate)
		{


		}
		public void onCharacteristicRead(BleDevice dev, UUID uuid, String data)
		{

		}
		public void onCharacteristicChanged(BleDevice dev, UUID uuid,
											byte[] data)
		{	
			Message msgLog = new Message();
			if(blUseHexFlag)
			{
				msgLog.obj =Arrays.toString(data);
				msgLog.what = 0;
			}
			else
			{
				String strLog=new String(data);
				msgLog.obj =strLog;
				msgLog.what = 0;
			}
			pReadLogHandler.sendMessage(msgLog);
			
			try
			{
				RecvDataQueue.ViewDataQueue.put(data);
			}catch(InterruptedException e)
			{
				Log.i("BleCallBack","onCharacteristicChanged:"+e);
			}
		}
		public void onServicesDiscovered(BleDevice dev)
		{

		}
		public void onCharacteristicWriteState(BleDevice dev, UUID uuid, int state)
		{

		}

   	 }
	class ViewLogThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				try
				{
					byte [] pbViewLog=(byte [])RecvLogQueue.ViewLogQueue.take();
					Message msgLog = new Message();
					if(blUseHexFlag)
					{
						msgLog.obj =Arrays.toString(pbViewLog);
						msgLog.what = 0;
					}
					else
					{
						String strLog=new String(pbViewLog);
						msgLog.obj =strLog;
						msgLog.what = 0;
					}
					pReadLogHandler.sendMessage(msgLog);
				}
				catch(InterruptedException e)
				{
					Log.i("ViewLogThread","takeViewLog err "+e);
				}
			}
		}
	}
}
class RecvLogQueue
{
	public static final int ARRAY_QUEUE_SIZE = 30;
	public static BlockingQueue<byte []> ViewLogQueue = new ArrayBlockingQueue<byte []>(ARRAY_QUEUE_SIZE);
}

