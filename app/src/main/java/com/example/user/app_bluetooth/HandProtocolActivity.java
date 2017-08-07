/*****************************************************************************
* Copyright (C) 2017-2018 Hanson Yu  All rights reserved.
------------------------------------------------------------------------------
* File Module		: 	HandProtocolActivity.java
* Description		: 	HandProtocolActivity operation center
* Created			: 	2017.06.23.
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/*****************************************************************************
-Class			: HandProtocolActivity
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/23	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
public class HandProtocolActivity extends Activity
{
	private static char s_cDataLen=0;
	private static int s_iRecvDataLen=0;
	private static RecvDataState mRecvState=RecvDataState.IDLE;;
	private static byte [] s_pbGetDataBuf=null;
	Context mContext;
	private Handler pGetParaHandler=null;
	private boolean blJumpToLog=false;
	private ViewParaThread mViewPara=null;
	private RecvDataThread mRecvData=null;


	private Spinner spnWorkMode=null;
	private static final String[] strWorkMode = { "TCP", "UDP" }; //定义数组
	private int iWorkMode=0;
	private Button btGetWorkMode=null;
	private Button btSetWorkMode=null;

	private Spinner spnSetBps=null;
	private static final String[] strSetBps = {"1200","2400","1200","2400","4800","9600","14400",
	"19200","14400","19200","28800","38400","57600","64000","76800","115200","128000","230400","345600","460800","500000"}; 
	private int iSetBps=0;
	private Button btSetBps=null;
	
	private Button btGetOwnNum=null;
	private Button btSetOwnNum=null;
	private EditText etOwnNumView=null;
	
	private Button btGetMainGprs=null;
	private Button btSetMainGprs=null;
	private EditText etMainGprsView=null;

	private Button btGetMainApn=null;
	private Button btSetMainApn=null;
	private EditText etMainApnView=null;

	private Button btGetBackupGprs=null;
	private Button btSetBackupGprs=null;
	private EditText etBackupGprsView=null;	

	private Button btGetBackupApn=null;
	private Button btSetBackupApn=null;
	private EditText etBackupApnView=null;	

	private Button btGetFixReport=null;
	private Button btSetFixReport=null;
	private EditText etFixReportView=null;
	
	private Button btGetProvinceID=null;
	private Button btSetProvinceID=null;
	private EditText etProvinceIDView=null;

	private Button btGetCityID=null;
	private Button btSetCityID=null;
	private EditText etCityIDView=null;

	private Button btGetTerminalID=null;
	private Button btSetTerminalID=null;
	private EditText etTerminalIDView=null;

	
	private Button btClear=null;
	private Button btInit=null;
	/*****************************************************************************
	-Class			: ParaOprListener
	-Description	: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/19	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	class ParaOprListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ParaGet mGetPara=new ParaGet();
			ParaSet mSetPara=new ParaSet();
			switch(v.getId())
			{
				case R.id.buttonGetOwnNumber:
				{
					mGetPara.getOwnNum();
					break;
				}
				case R.id.buttonSetOwnNumber:
				{
					mSetPara.setOwnNum();
					break;
				}
				
				case R.id.buttonGetMainGprs:
				{
					mGetPara.getMainGprs();
					break;
				}
				case R.id.buttonSetMainGprs:
				{
					mSetPara.setMainGprs();
					break;
				}
				
				case R.id.buttonGetMainApn:
				{
					mGetPara.getMainApn();
					break;
				}
				case R.id.buttonSetMainApn:
				{
					mSetPara.setMainApn();
					break;
				}
				
				case R.id.buttonGetBackupGprs:
				{
					mGetPara.getBackupGprs();
					break;
				}
				case R.id.buttonSetBackupGprs:
				{
					mSetPara.setBackupGprs();
					break;
				}
				
				case R.id.buttonGetBackupApn:
				{
					mGetPara.getBackupApn();
					break;
				}
				case R.id.buttonSetBackupApn:
				{
					mSetPara.setBackupApn();
					break;
				}		
				
				case R.id.buttonGetWorkMode:
				{
					mGetPara.getWorkMode();
					break;
				}
				case R.id.buttonSetWorkMode:
				{
					mSetPara.setWorkMode();
					break;
				}		
				
				case R.id.buttonGetFixReport:
				{
					mGetPara.getFixedReportTime();
					break;
				}
				case R.id.buttonSetFixReport:
				{
					mSetPara.setFixedReportTime();
					break;
				}	
				
				case R.id.buttonGetProvinceID:
				{
					mGetPara.getProvinceID();
					break;
				}
				case R.id.buttonSetProvinceID:
				{
					mSetPara.setProvinceID();
					break;
				}	

				case R.id.buttonGetCityID:
				{
					mGetPara.getCityID();
					break;
				}
				case R.id.buttonSetCityID:
				{
					mSetPara.setCityID();
					break;
				}	

				case R.id.buttonGetTerminalID:
				{
					mGetPara.getTerminalID();
					break;
				}
				case R.id.buttonSetTerminalID:
				{
					mSetPara.setTerminalID();
					break;
				}	

				case R.id.buttonInit:
				{
					mSetPara.FactoryReset();
					break;
				}
				case R.id.buttonClear:
				{
					ClearPara();
					break;
				}	
				case R.id.buttonSetBps:
				{
					String strBps="AT+BAUD="+iSetBps+"\r\n";
					Log.i("ParaOprListener", "SetBps:"+strBps);
					BluetoothMsg.BleRemoteDevice.write(strBps.getBytes());
					break;
				}
				default:
				{
					Log.i("ParaOprListener", "onClick err");
					break;
				}
			}
		}
	}
	
	/*****************************************************************************
	-Class			: ParaOprListener
	-Description	: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/19	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	class SpinnerOprListener implements Spinner.OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
		{
			switch(arg0.getId())
			{
				case R.id.spinnerWorkMode:
				{
					String cardNumber = strWorkMode[arg2];
					iWorkMode=arg2;
					//设置显示当前选择的项
					arg0.setVisibility(View.VISIBLE);	
				break;
				}
				
				case R.id.spinnerSetBps:
				{
					iSetBps=arg2;
					//设置显示当前选择的项
					arg0.setVisibility(View.VISIBLE);	
				break;
				}

			}

		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{
			// TODO Auto-generated method stub
		}
	}
	@Override
	public void onBackPressed()
	{
		Intent in=new Intent(HandProtocolActivity.this,LogActivity.class);
		startActivity(in);
	}
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ble_activity);
		mContext = this;
		
		btGetOwnNum = (Button) findViewById(R.id.buttonGetOwnNumber);
		btGetOwnNum.setOnClickListener(new ParaOprListener());
		btSetOwnNum = (Button) findViewById(R.id.buttonSetOwnNumber);
		btSetOwnNum.setOnClickListener(new ParaOprListener());
		etOwnNumView = (EditText) findViewById(R.id.editTextOwnNumber);

		btGetMainGprs = (Button) findViewById(R.id.buttonGetMainGprs);
		btGetMainGprs.setOnClickListener(new ParaOprListener());
		btSetMainGprs = (Button) findViewById(R.id.buttonSetMainGprs);
		btSetMainGprs.setOnClickListener(new ParaOprListener());
		etMainGprsView = (EditText) findViewById(R.id.editTextMainGprs);

		btGetMainApn = (Button) findViewById(R.id.buttonGetMainApn);
		btGetMainApn.setOnClickListener(new ParaOprListener());
		btSetMainApn = (Button) findViewById(R.id.buttonSetMainApn);
		btSetMainApn.setOnClickListener(new ParaOprListener());
		etMainApnView = (EditText) findViewById(R.id.editTextMainApn);

		btGetBackupGprs = (Button) findViewById(R.id.buttonGetBackupGprs);
		btGetBackupGprs.setOnClickListener(new ParaOprListener());
		btSetBackupGprs = (Button) findViewById(R.id.buttonSetBackupGprs);
		btSetBackupGprs.setOnClickListener(new ParaOprListener());
		etBackupGprsView = (EditText) findViewById(R.id.editTextBackupGprs);

		btGetBackupApn = (Button) findViewById(R.id.buttonGetBackupApn);
		btGetBackupApn.setOnClickListener(new ParaOprListener());
		btSetBackupApn = (Button) findViewById(R.id.buttonSetBackupApn);
		btSetBackupApn.setOnClickListener(new ParaOprListener());
		etBackupApnView = (EditText) findViewById(R.id.editTextBackupApn);

		btGetWorkMode = (Button) findViewById(R.id.buttonGetWorkMode);
		btGetWorkMode.setOnClickListener(new ParaOprListener());
		btSetWorkMode = (Button) findViewById(R.id.buttonSetWorkMode);
		btSetWorkMode.setOnClickListener(new ParaOprListener());
		spnWorkMode =(Spinner)findViewById(R.id.spinnerWorkMode);
		spnWorkMode.setPrompt("工作模式:");
		ArrayAdapter<String> adapterWrokMode=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strWorkMode);
		spnWorkMode.setAdapter(adapterWrokMode);
		spnWorkMode.setOnItemSelectedListener(new SpinnerOprListener());

		btGetFixReport = (Button) findViewById(R.id.buttonGetFixReport);
		btGetFixReport.setOnClickListener(new ParaOprListener());
		btSetFixReport = (Button) findViewById(R.id.buttonSetFixReport);
		btSetFixReport.setOnClickListener(new ParaOprListener());
		etFixReportView = (EditText) findViewById(R.id.editTextFixReport);

		btGetProvinceID = (Button) findViewById(R.id.buttonGetProvinceID);
		btGetProvinceID.setOnClickListener(new ParaOprListener());
		btSetProvinceID = (Button) findViewById(R.id.buttonSetProvinceID);
		btSetProvinceID.setOnClickListener(new ParaOprListener());
		etProvinceIDView = (EditText) findViewById(R.id.editTextProvinceID);
		
		btGetCityID = (Button) findViewById(R.id.buttonGetCityID);
		btGetCityID.setOnClickListener(new ParaOprListener());
		btSetCityID = (Button) findViewById(R.id.buttonSetCityID);
		btSetCityID.setOnClickListener(new ParaOprListener());
		etCityIDView = (EditText) findViewById(R.id.editTextCityID);
		
		btGetTerminalID = (Button) findViewById(R.id.buttonGetTerminalID);
		btGetTerminalID.setOnClickListener(new ParaOprListener());
		btSetTerminalID = (Button) findViewById(R.id.buttonSetTerminalID);
		btSetTerminalID.setOnClickListener(new ParaOprListener());
		etTerminalIDView = (EditText) findViewById(R.id.editTextTerminalID);
		
		btSetBps = (Button) findViewById(R.id.buttonSetBps);
		btSetBps.setOnClickListener(new ParaOprListener());
		spnSetBps =(Spinner)findViewById(R.id.spinnerSetBps);
		spnSetBps.setPrompt("波特率:");
		ArrayAdapter<String> adapterSetBps=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strSetBps);
		spnSetBps.setAdapter(adapterSetBps);
		spnSetBps.setOnItemSelectedListener(new SpinnerOprListener());

		btClear = (Button) findViewById(R.id.buttonClear);
		btClear.setOnClickListener(new ParaOprListener());
		btInit = (Button) findViewById(R.id.buttonInit);
		btInit.setOnClickListener(new ParaOprListener());
		
	}
	@Override
	protected  void onResume()
	{
		super.onResume();
		pGetParaHandler = new Handler(new ReadParaHandler()); 
		
		mViewPara=new ViewParaThread(); 
		mViewPara.start();
		mRecvData=new RecvDataThread(); 
		mRecvData.start();
	}
	@Override
	protected void onDestroy()
	{
	    Log.i("HandProtocolActivity", "onDestroy");
	    if(null==mViewPara)
	    {
	    }
	    else
	    {
		mViewPara.interrupt();
		mViewPara=null;
	    }
	    if(null==mRecvData)
	    {
	    }
	    else
	    {
		mRecvData.interrupt();
		mRecvData=null;
	    }
	    super.onDestroy();
	}//发送数据
	private void ClearPara()
	{
		etOwnNumView.setText(null);
		etMainApnView.setText(null);
		etBackupApnView.setText(null);
		etBackupGprsView.setText(null);
		etMainGprsView.setText(null);
		etFixReportView.setText(null);
		etProvinceIDView.setText(null);
		etCityIDView.setText(null);
		etTerminalIDView.setText(null);
		spnWorkMode.setSelection(0);
	}
	private int GetLengthExceptF(String i_strSrc)
	{
		int i=i_strSrc.length()-1;
		for(i=i_strSrc.length()-1;i>=0;i--)
		{
			if(i_strSrc.charAt(i)=='F')
			{
			}
			else
			{
				break;
			}
		}
		return i+1;
	}
	private String GetStringF(int i_iCount)
	{
		String strF="";
		if(i_iCount<=0)
		{
		}
		else
		{
			char cF[]=new char[i_iCount];
			int i;
			for(i=0;i<i_iCount;i++)
			{
				cF[i]='F';
			}
			strF=new String(cF);
		}
		return strF;
	}
	private boolean GetInputGprsPara(String i_strSrc,String []o_strDst)
	{
		boolean blRet=false;
		int iLocation;
		int iIpLen=0;
		int iPortLen=0;
		int iUsrNameLen=0;
		int iPasswordLen=0;
		int iOffset;
		String strIp="";
		String strPort="";
		String strUserName="";
		String strPassword="";
		iLocation=i_strSrc.indexOf(',');
		if((-1==iLocation)||(15<iLocation))
		{
			  Log.i("HandProtocolActivity", "strIpGetInputGprsPara"+iLocation);
		}
		else 
		{
			iIpLen=iLocation;
			iOffset=iIpLen+1;
			iLocation=i_strSrc.indexOf(',',iLocation+1);
			if((-1==iLocation)||((iOffset+5)<iLocation))
			{
				  Log.i("HandProtocolActivity", "strPortGetInputGprsPara"+iLocation);
			}
			else
			{
				iPortLen=iLocation-iOffset;
				iOffset+=iPortLen+1;
				iLocation=i_strSrc.indexOf(',',iLocation+1);
				if((-1==iLocation)||(iOffset+20<iLocation))
				{
				  	Log.i("HandProtocolActivity", "strUserNameGetInputGprsPara"+iLocation);
				}
				else
				{
					iUsrNameLen=iLocation-iOffset;
					iPasswordLen=i_strSrc.length()-iLocation-1;
					blRet=true;
				}
			}
		}
		if(false==blRet)
		{
		}
		else
		{
			if(iIpLen>0)
			strIp=new String(i_strSrc.toCharArray(),0,iIpLen);
			if(iPortLen>0)
			strPort=new String(i_strSrc.toCharArray(),iIpLen+1,iPortLen);
			if(iUsrNameLen>0)
			strUserName=new String(i_strSrc.toCharArray(),iIpLen+1+iPortLen+1,iUsrNameLen);
			if(iPasswordLen>0)
			strPassword=new String(i_strSrc.toCharArray(),iIpLen+1+iPortLen+1+iUsrNameLen+1,iPasswordLen);
			o_strDst[0]=strIp+GetStringF(15-iIpLen)+strPort+GetStringF(5-iPortLen)+strUserName+GetStringF(20-iUsrNameLen)+strPassword+GetStringF(20-iPasswordLen);
		}
		return blRet;
	}
	private class ReadParaHandler implements Handler.Callback
	{
		public boolean handleMessage(Message msg)
		{
			byte bHandleBuf[] =Arrays.copyOf((byte []) msg.obj,((byte[]) msg.obj).length);
			String strSrc=new String(bHandleBuf);
			String strSrcExceptSimCard=new String(strSrc.toCharArray(),1,strSrc.toCharArray().length-1);
			Log.i("ReadParaHandler", "handleMessage"+msg.what);
			switch(msg.what)
			{
				case HandProtocolInfo.HandProtocolSubCmdId.OWN_NUMBER:
				{
					int i=strSrcExceptSimCard.length()-1;
					for(i=strSrcExceptSimCard.length()-1;i>=0;i--)
					{
						if(strSrcExceptSimCard.charAt(i)>0x39)
						{
						}
						else
						{
							break;
						}
					}
					try{
						String strOwnNum=new String(strSrcExceptSimCard.toCharArray(),0,i+1);
						etOwnNumView.setText(strOwnNum);
					}
					catch (IndexOutOfBoundsException e)
					{
						Log.i("ReadParaHandler", "etOwnNumView err"+e);
					}
					break;
				}
				
				case HandProtocolInfo.HandProtocolSubCmdId.CENTRE_IP_PORT:
				{
					String strIp=new String(strSrcExceptSimCard.toCharArray(),0,15);
					String strPort=new String(strSrcExceptSimCard.toCharArray(),15,5);
					String strUsrName=new String(strSrcExceptSimCard.toCharArray(),20,20);
					String strPassword=new String(strSrcExceptSimCard.toCharArray(),40,20);
					
					strIp=new String(strIp.toCharArray(),0,GetLengthExceptF(strIp));
					strPort=new String(strPort.toCharArray(),0,GetLengthExceptF(strPort));
					strUsrName=new String(strUsrName.toCharArray(),0,GetLengthExceptF(strUsrName));
					strPassword=new String(strPassword.toCharArray(),0,GetLengthExceptF(strPassword));

					String strGprs=strIp+','+strPort+','+strUsrName+','+strPassword;
					if('0'==strSrc.charAt(0))
					{
						etMainGprsView.setText(strGprs);
					}
					else
					{
						etBackupGprsView.setText(strGprs);
					}

					break;
				}
				
				case HandProtocolInfo.HandProtocolSubCmdId.APN_NAME:
				{
					int i=strSrcExceptSimCard.length()-1;
					for(i=strSrcExceptSimCard.length()-1;i>=0;i--)
					{
						if(strSrcExceptSimCard.charAt(i)=='F')
						{
						}
						else
						{
							break;
						}
					}
					String strApn=new String(strSrcExceptSimCard.toCharArray(),0,i+1);
					if('0'==strSrc.charAt(0))
					{
						etMainApnView.setText(strApn);
					}
					else
					{
						etBackupApnView.setText(strApn);
					}
					break;
				}
				
				case HandProtocolInfo.HandProtocolSubCmdId.WORKMODE:
				{
					if(bHandleBuf[0]==2)//TCP
					{
						spnWorkMode.setSelection(0);
					}
					else if(bHandleBuf[0]==1)//UCP
					{
						spnWorkMode.setSelection(1);
					}
					else
					{
						Log.i("ReadParaHandler", "spnWorkMode err"+Arrays.toString(bHandleBuf));
					}
					break;
				}
				
				case HandProtocolInfo.HandProtocolSubCmdId.FIXED_NUM:
				{
					String strFixedNum=null;
					if(bHandleBuf[1]<=0)
					{
						strFixedNum=""+bHandleBuf[0];
					}
					else
					{
						strFixedNum=""+(bHandleBuf[1]*16*16+bHandleBuf[0]);
					}
					etFixReportView.setText(strFixedNum);
					break;
				}
				
				case HandProtocolInfo.HandProtocolSubCmdId.PROVINCE_ID:
				{
					String strFixedNum=null;
					if(bHandleBuf[1]<=0)
					{
						strFixedNum=""+bHandleBuf[0];
					}
					else
					{
						strFixedNum=""+(bHandleBuf[1]*16*16+bHandleBuf[0]);
					}
					etProvinceIDView.setText(strFixedNum);
					break;
				}
				
				case HandProtocolInfo.HandProtocolSubCmdId.CITY_ID:
				{		
					//Log.i("ReadParaHandler", "etCityIDView"+Arrays.toString(bHandleBuf));
					String strFixedNum=null;
					if(bHandleBuf[1]<=0)
					{
						strFixedNum=""+bHandleBuf[0];
					}
					else
					{
						strFixedNum=""+(bHandleBuf[1]*16*16+bHandleBuf[0]);
					}
					etCityIDView.setText(strFixedNum);
					break;
				}
				
				case HandProtocolInfo.HandProtocolSubCmdId.TERMINAL_ID:
				{
					etTerminalIDView.setText(strSrc);
					break;
				}
				default:
				{
					Log.i("ReadParaHandler", "handleMessage err"+msg.what);
					break;
				}
			}
			return  true;
		}
	}
	public interface IConstants 
	{
	    String IDLE = "IDLE";
	    String STRAT = "STRAT";
	    String RXING = "RXING";
	}
	public enum RecvDataState
	{
		IDLE,STRAT,RXING;
	}
	class ViewParaThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				try
				{
					ViewPara mViewPara=(ViewPara)GetParaQueue.ViewParaQueue.take();
					Message msgPara = new Message();
					msgPara.obj =mViewPara.GetDataBuf();
					msgPara.what = mViewPara.GetCmdId();
					//Log.i("ViewParaThread", "run"+Arrays.toString(mViewPara.GetDataBuf()));
					pGetParaHandler.sendMessage(msgPara);
					/*switch(mViewPara.GetCmdId())
					{
						case HandProtocolInfo.HandProtocolSubCmdId.OWN_NUMBER:
						{

							break;
						}
						default :
						{
							Log.i("ViewParaThread", "run err"+mViewPara.GetCmdId());
							break;
						}
					}*/		
				}
				catch(InterruptedException e)
				{
					Log.i("HandleProtocolActivity","takemViewPara err "+e);
				}
			}
		}
	}
	
	class RecvDataThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				try
				{
					byte [] pbViewData=(byte [])RecvDataQueue.ViewDataQueue.take();
					char cDataLen1=0;
					char cDataLen2=0;
					boolean blRet=false;
					byte [] pbAckDataBuf=null;
					int iAckDataBufLen[]=new int[1];
					if(pbViewData[0]!=HandProtocolInfo.RECV_FIRST_BYTE||pbViewData[1]!=HandProtocolInfo.RECV_SECOND_BYTE)
					{
					}
					else
					{
						mRecvState=RecvDataState.STRAT;
					}
					switch(mRecvState)
					{
						case STRAT:
						{
							cDataLen1=(char)(pbViewData[2]&0xff);
							cDataLen2=(char)(pbViewData[3]&0xff);
							s_cDataLen=(char)((cDataLen2<<8)|cDataLen1);
							s_pbGetDataBuf=new byte[s_cDataLen];
							System.arraycopy(pbViewData,0,s_pbGetDataBuf,0,pbViewData.length);
							s_iRecvDataLen=pbViewData.length;
							if(s_iRecvDataLen>=s_cDataLen)
							{
								blRet=true;
							}
							else
							{
								mRecvState=RecvDataState.RXING;
							}
							break;
						}
						case RXING:
						{
							System.arraycopy(pbViewData,0,s_pbGetDataBuf,s_iRecvDataLen,pbViewData.length);
							s_iRecvDataLen+=pbViewData.length;
							if(s_iRecvDataLen>=s_cDataLen)
							{
								mRecvState=RecvDataState.IDLE;
								blRet=true;
							}
							else
							{
							}
							break;
						}
						default :
						{
							Log.i("RecvDataThread", "RecvData err"+Arrays.toString(pbViewData)+mRecvState);
							break;
						}
					}
					if(false==blRet)
					{
					}
					else
					{
						//Log.i("RecvDataThread", "checkData"+Arrays.toString(s_pbGetDataBuf));
						HandProtocol mHandProtocol=new HandProtocol(s_pbGetDataBuf,(byte)1);
						if(false==mHandProtocol.checkData())
						{
							Log.i("RecvDataThread", "checkData err");
						}
						else
						{
							if(false==mHandProtocol.analyseData(pbAckDataBuf,iAckDataBufLen))
							{
								Log.i("RecvDataThread", "analyseData err " + Arrays.toString(s_pbGetDataBuf));
							}
							else
							{
							}
						}
						s_pbGetDataBuf=null;
						s_iRecvDataLen=0;
					}
				}
				catch(InterruptedException e)
				{
					Log.i("RecvDataThread","takeViewLog err "+e);
				}
			}
		}
	}
	/*****************************************************************************
	-Class			: ParaSet
	-Description	: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/23	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	class ParaSet
	{
		/*****************************************************************************
		-Fuction		: setOwnNum
		-Description	: setOwnNum
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setOwnNum()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String inputtext=etOwnNumView.getText().toString();
			byte bDatabuf[]=new byte[inputtext.getBytes().length+3];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.OWN_NUMBER;
			bDatabuf[1]=HandProtocolInfo.SET_PARA;
			bDatabuf[2]=HandProtocolInfo.SIM_CARD_0;
			System.arraycopy(inputtext.getBytes(),0,bDatabuf,3,inputtext.getBytes().length);
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			if (inputtext.length()>0) 
			{
				HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
				iSendDataLen[0]=0;
				try
				{
					mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);						
					mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
				}catch(Exception e)
				{
					Log.i("ParaSet", "setOwnNumpackData err"+e);
				}finally{
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
			}
		}

		/*****************************************************************************
		-Fuction		: setMainGprs
		-Description	: setMainGprs
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setMainGprs()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String strInputText=etMainGprsView.getText().toString();
			String [] SendText={"SendText"};
			if (true==GetInputGprsPara(strInputText,SendText)) 
			{
				byte bDatabuf[]=new byte[SendText[0].getBytes().length+3];
				bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.CENTRE_IP_PORT;
				bDatabuf[1]=HandProtocolInfo.SET_PARA;
				bDatabuf[2]=HandProtocolInfo.SIM_CARD_0;
				System.arraycopy(SendText[0].getBytes(),0,bDatabuf,3,SendText[0].getBytes().length);
				byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
				HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
				iSendDataLen[0]=0;
				try
				{
					mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);						
					mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
				}catch(Exception e)
				{
					Log.i("ParaSet", "setMainGprs err"+e);
				}finally{
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "发送格式错误!例:Ip,Port,UserName,Password", Toast.LENGTH_SHORT).show();
			}
		}

		/*****************************************************************************
		-Fuction		: setMainApn
		-Description	: setMainApn
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setMainApn()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String inputtext=etMainApnView.getText().toString();
			byte bDatabuf[]=new byte[inputtext.getBytes().length+3];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.APN_NAME;
			bDatabuf[1]=HandProtocolInfo.SET_PARA;
			bDatabuf[2]=HandProtocolInfo.SIM_CARD_0;
			System.arraycopy(inputtext.getBytes(),0,bDatabuf,3,inputtext.getBytes().length);
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			if (inputtext.length()>0) 
			{
				HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
				iSendDataLen[0]=0;
				try
				{
					mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);						
					mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
				}catch(Exception e)
				{
					Log.i("ParaSet", "setMainApn err"+e);
				}finally{
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
			}
		}
		
		/*****************************************************************************
		-Fuction		: setBackupGprs
		-Description	: setBackupGprs
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setBackupGprs()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String strInputText=etBackupGprsView.getText().toString();
			String [] SendText={"SendText"};
			if (true==GetInputGprsPara(strInputText,SendText)) 
			{
				byte bDatabuf[]=new byte[SendText[0].getBytes().length+3];
				bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.CENTRE_IP_PORT;
				bDatabuf[1]=HandProtocolInfo.SET_PARA;
				bDatabuf[2]=HandProtocolInfo.SIM_CARD_1;
				System.arraycopy(SendText[0].getBytes(),0,bDatabuf,3,SendText[0].getBytes().length);
				byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
				HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
				iSendDataLen[0]=0;
				try
				{
					mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);						
					mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
				}catch(Exception e)
				{
					Log.i("ParaSet", "setMainGprs err"+e);
				}finally{
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "发送格式错误!例:Ip,Port,UserName,Password", Toast.LENGTH_SHORT).show();
			}
		}

		/*****************************************************************************
		-Fuction		: setBackupApn
		-Description	: setBackupApn
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setBackupApn()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String inputtext=etBackupApnView.getText().toString();
			byte bDatabuf[]=new byte[inputtext.getBytes().length+3];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.APN_NAME;
			bDatabuf[1]=HandProtocolInfo.SET_PARA;
			bDatabuf[2]=HandProtocolInfo.SIM_CARD_1;
			System.arraycopy(inputtext.getBytes(),0,bDatabuf,3,inputtext.getBytes().length);
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			if (inputtext.length()>0) 
			{
				HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
				iSendDataLen[0]=0;
				try
				{
					mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);						
					mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
				}catch(Exception e)
				{
					Log.i("ParaSet", "setMainApn err"+e);
				}finally{
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
			}
		}

		/*****************************************************************************
		-Fuction		: setWorkMode
		-Description	: setWorkMode
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setWorkMode()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bWorkMode=0;
			byte bDatabuf[]=new byte[3];

			if(iWorkMode==0)
			{
				bWorkMode=(byte)2;//TCP
			}
			else
			{
				bWorkMode=(byte)1;//UDP
			}
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.WORKMODE;
			bDatabuf[1]=HandProtocolInfo.SET_PARA;
			bDatabuf[2]=bWorkMode;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);						
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}catch(Exception e)
			{
				Log.i("ParaSet", "setWorkMode err"+e);
			}finally{
			}
		}

		/*****************************************************************************
		-Fuction		: setBackupApn
		-Description	: setBackupApn
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setFixedReportTime()
		{
			byte bCmd=HandProtocolInfo.SEND_FUNCTION_STATE_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String inputText=etFixReportView.getText().toString();
			char cReportTime=0;
			byte bDatabuf[]=new byte[4];
			try
			{
				if (inputText.length()>0) 
				{
					cReportTime=(char)Integer.parseInt(inputText,10);
					bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.FIXED_NUM;
					bDatabuf[1]=HandProtocolInfo.SET_PARA;
					bDatabuf[2]=(byte)(cReportTime&0x00ff);
					bDatabuf[3]=(byte)(cReportTime>>8&0x00ff);
					byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
					HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
					iSendDataLen[0]=0;
					try
					{
						mHandProtocol.packData(bCmd, bDatabuf, bDatabuf.length, bSendDatabuf, iSendDataLen);
						mHandProtocol.sendData(bSendDatabuf, iSendDataLen[0]);
						//Log.i("ParaSet", "setFixedReportTime"+Arrays.toString(bSendDatabuf));
					}catch(Exception e)
					{
						Log.i("ParaSet", "setFixedReportTime err"+e);
					}finally{
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
				}
			}
			catch(NumberFormatException  e)
			{
				Log.i("ParaSet", "setFixedReportTime err"+e);
			}
		}

		/*****************************************************************************
		-Fuction		: setProvinceID
		-Description	: setProvinceID
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setProvinceID()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String inputText=etProvinceIDView.getText().toString();
			char cProvinceID=0;
			byte bDatabuf[]=new byte[4];
			try
			{
				if (inputText.length()>0) 
				{
					cProvinceID=(char)Integer.parseInt(inputText,10);
					bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.PROVINCE_ID;
					bDatabuf[1]=HandProtocolInfo.SET_PARA;
					bDatabuf[2]=(byte)(cProvinceID&0x00ff);
					bDatabuf[3]=(byte)(cProvinceID>>8&0x00ff);
					byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
					HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
					iSendDataLen[0]=0;
					try
					{
						mHandProtocol.packData(bCmd, bDatabuf, bDatabuf.length, bSendDatabuf, iSendDataLen);
						mHandProtocol.sendData(bSendDatabuf, iSendDataLen[0]);
						//Log.i("ParaSet", "setFixedReportTime"+Arrays.toString(bSendDatabuf));
					}catch(Exception e)
					{
						Log.i("ParaSet", "setProvinceID err"+e);
					}finally{
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
				}
			}
			catch(NumberFormatException  e)
			{
				Log.i("ParaSet", "setProvinceID err"+e);
			}
		}

		/*****************************************************************************
		-Fuction		: setCityID
		-Description	: setCityID
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setCityID()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String inputText=etCityIDView.getText().toString();
			char cCityID=0;
			byte bDatabuf[]=new byte[4];
			try
			{
				if (inputText.length()>0) 
				{
					cCityID=(char)Integer.parseInt(inputText,10);
					bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.CITY_ID;
					bDatabuf[1]=HandProtocolInfo.SET_PARA;
					bDatabuf[2]=(byte)(cCityID&0x00ff);
					bDatabuf[3]=(byte)(cCityID>>8&0x00ff);
					byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
					HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
					iSendDataLen[0]=0;
					try
					{
						mHandProtocol.packData(bCmd, bDatabuf, bDatabuf.length, bSendDatabuf, iSendDataLen);
						mHandProtocol.sendData(bSendDatabuf, iSendDataLen[0]);
						//Log.i("ParaSet", "setFixedReportTime"+Arrays.toString(bSendDatabuf));
					}catch(Exception e)
					{
						Log.i("ParaSet", "setCityID err"+e);
					}finally{
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
				}
			}
			catch(NumberFormatException  e)
			{
				Log.i("ParaSet", "setCityID err"+e);
			}
		}

		/*****************************************************************************
		-Fuction		: setTerminalID
		-Description	: setTerminalID
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void setTerminalID()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			String inputText=etTerminalIDView.getText().toString();
			byte bDatabuf[]=new byte[inputText.getBytes().length+2];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.TERMINAL_ID;
			bDatabuf[1]=HandProtocolInfo.SET_PARA;
			System.arraycopy(inputText.getBytes(),0,bDatabuf,2,inputText.getBytes().length);
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			if (inputText.length()>0) 
			{
				HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
				iSendDataLen[0]=0;
				try
				{
					mHandProtocol.packData(bCmd, bDatabuf, bDatabuf.length, bSendDatabuf, iSendDataLen);
					mHandProtocol.sendData(bSendDatabuf, iSendDataLen[0]);
					//Log.i("ParaSet", "setFixedReportTime"+Arrays.toString(bSendDatabuf));
				}catch(Exception e)
				{
					Log.i("ParaSet", "setTerminalID err"+e);
				}finally{
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
			}
		}

		/*****************************************************************************
		-Fuction		: FactoryReset
		-Description	: DefaultInit
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void FactoryReset()
		{
			byte bCmd=HandProtocolInfo.SEND_FUNCTION_STATE_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[2];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolFunSubCmdId.FACTORY_SET;
			bDatabuf[1]=HandProtocolInfo.SET_PARA;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd, bDatabuf, bDatabuf.length, bSendDatabuf, iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf, iSendDataLen[0]);
				//Log.i("ParaSet", "setFixedReportTime"+Arrays.toString(bSendDatabuf));
			}catch(Exception e)
			{
				Log.i("ParaSet", "FactoryReset err"+e);
			}finally{
			}
		}
		
	}
	/*****************************************************************************
	-Class			: ParaGet
	-Description	: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/23	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	class ParaGet
	{
		/*****************************************************************************
		-Fuction		: setOwnNum
		-Description	: setOwnNum
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getOwnNum()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[3];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.OWN_NUMBER;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			bDatabuf[2]=HandProtocolInfo.SIM_CARD_0;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getOwnNumpackData err"+e);
			}finally
			{
			}
		}
		/*****************************************************************************
		-Fuction		: getMainGprs
		-Description	: getMainGprs
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getMainGprs()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[3];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.CENTRE_IP_PORT;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			bDatabuf[2]=HandProtocolInfo.SIM_CARD_0;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getMainGprs err"+e);
			}finally
			{
			}
		}
		
		/*****************************************************************************
		-Fuction		: getMainApn
		-Description	: getMainApn
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getMainApn()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[3];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.APN_NAME;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			bDatabuf[2]=HandProtocolInfo.SIM_CARD_0;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getMainGprs err"+e);
			}finally
			{
			}
		}
		
		/*****************************************************************************
		-Fuction		: getBackupGprs
		-Description	: getBackupGprs
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getBackupGprs()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[3];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.CENTRE_IP_PORT;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			bDatabuf[2]=HandProtocolInfo.SIM_CARD_1;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getMainGprs err"+e);
			}finally
			{
			}
		}
		
		/*****************************************************************************
		-Fuction		: getBackupApn
		-Description	: getBackupApn
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getBackupApn()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[3];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.APN_NAME;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			bDatabuf[2]=HandProtocolInfo.SIM_CARD_1;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getMainGprs err"+e);
			}finally
			{
			}
		}
		
		
		/*****************************************************************************
		-Fuction		: getWorkMode
		-Description	: getWorkMode
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getWorkMode()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[2];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.WORKMODE;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getWorkMode err"+e);
			}finally
			{
			}
		}
		/*****************************************************************************
		-Fuction		: getBackupApn
		-Description	: getBackupApn
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getFixedReportTime()
		{
			byte bCmd=HandProtocolInfo.SEND_FUNCTION_STATE_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[2];
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.FIXED_NUM;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd, bDatabuf, bDatabuf.length, bSendDatabuf, iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf, iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getFixedReportTime err"+e);
			}finally
			{
			}
		}
			
		/*****************************************************************************
		-Fuction		: getProvinceID
		-Description	: getProvinceID
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getProvinceID()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[2];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.PROVINCE_ID;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getProvinceID err"+e);
			}finally
			{
			}
		}
				
		/*****************************************************************************
		-Fuction		: getCityID
		-Description	: getCityID
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getCityID()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[2];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.CITY_ID;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getCityID err"+e);
			}finally
			{
			}
		}
				
		/*****************************************************************************
		-Fuction		: getTerminalID
		-Description	: getTerminalID
		-Input			: 
		-Output 		: 
		-Return 		: 
		* Modify Date	  Version		 Author 		  Modification
		* -----------------------------------------------
		* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
		******************************************************************************/
		public  void getTerminalID()
		{
			byte bCmd=HandProtocolInfo.SEND_PARA_OPR_CMD;
			int iSendDataLen[]=new int[1];
			byte bDatabuf[]=new byte[2];
			
			bDatabuf[0]=HandProtocolInfo.HandProtocolSubCmdId.TERMINAL_ID;
			bDatabuf[1]=HandProtocolInfo.GET_PARA;
			byte bSendDatabuf[]=new byte[bDatabuf.length+HandProtocolInfo.BASE_LEN];
			HandProtocol mHandProtocol=new HandProtocol(bDatabuf,(byte)1);
			iSendDataLen[0]=0;
			try
			{
				mHandProtocol.packData(bCmd,bDatabuf,bDatabuf.length,bSendDatabuf,iSendDataLen);
				mHandProtocol.sendData(bSendDatabuf,iSendDataLen[0]);
			}
			catch(Exception e)
			{
				Log.i("ParaGet", "getTerminalID err"+e);
			}finally
			{
			}
		}


		
	}
}
class RecvDataQueue
{
	public static final int ARRAY_QUEUE_SIZE = 30;
	public static BlockingQueue<byte []> ViewDataQueue = new ArrayBlockingQueue<byte []>(ARRAY_QUEUE_SIZE);
}

