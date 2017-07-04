/*****************************************************************************
* Copyright (C) 2017-2020 China Aerospace Telecommunications Ltd.  All rights reserved.
------------------------------------------------------------------------------
* File Module		: 	HandProtocol.java
* Description		: 	HandProtocol operation center
* Created			: 	2017.06.22.
* Author			: 	Yu Weifeng
* Function List 		: 	
* Last Modified 	: 	
* History			: 	
******************************************************************************/

package com.example.user.app_bluetooth;


import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/*****************************************************************************
-Class			: Protocol
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
public class HandProtocol extends Protocol
{
 	private byte[] pbDataBuf=null;
	private int iDataLen;
	private byte bProtocolUser;
	private List listHandleMenu;
	/*****************************************************************************
	-Fuction		: HandProtocol
	-Description	: construct
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public HandProtocol(byte[] i_pbDataBuf,byte i_bProtocolUser)
	{
		iDataLen 		= i_pbDataBuf.length;
		pbDataBuf 	= Arrays.copyOf(i_pbDataBuf, iDataLen);
		bProtocolUser 	= i_bProtocolUser;
		//construct handleMenu
		listHandleMenu = Collections.synchronizedList(new LinkedList());
		GetParameter mGetPara=new GetParameter();
		listHandleMenu.add(mGetPara);
	}
	/*****************************************************************************
	-Fuction		: checkData
	-Description	: checkData
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public  boolean checkData()
	{
		boolean blRet=false;
		char cCrc1;
		char cCrc2;
		char cCrcCheck;
		char cMakeCrcCheck;
		if(pbDataBuf[0]!=HandProtocolInfo.RECV_FIRST_BYTE||pbDataBuf[1]!=HandProtocolInfo.RECV_SECOND_BYTE||
		    pbDataBuf[iDataLen-2]!=HandProtocolInfo.SECOND_LAST_BYTE|| pbDataBuf[iDataLen-1]!=HandProtocolInfo.LAST_BYTE)
		{
			blRet=false;
			Log.i("HandProtocol", "CheckData err:"+Arrays.toString(pbDataBuf));
		}
		else
		{
			cCrc1=(char)((pbDataBuf[iDataLen-3])&0xff);
			cCrc2=(char)((pbDataBuf[iDataLen-4])&0xff);
			cCrcCheck=(char)((cCrc1<<8)|cCrc2);
			cMakeCrcCheck=(char)Crc.MakeCrc(pbDataBuf,iDataLen-4);
			if(cCrcCheck!=cMakeCrcCheck)
			{
				blRet=false;
				Log.i("HandProtocol", "CrcCheck err"+Arrays.toString(pbDataBuf)+cCrcCheck+cMakeCrcCheck);
			}
			else
			{
				blRet=true;

			}
		}
		return blRet; 
	}
	/*****************************************************************************
	-Fuction		: analyseData
	-Description	: analyseData
	-Input			: 
	-Output 		: o_pbDataBuf o_iLen
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public  boolean analyseData(byte[] o_pbDataBuf,int[] o_iLen)
	{
		boolean blRet=false;
		int i;
		byte bCmd;
		byte pbBuf[] =new byte[iDataLen-HandProtocolInfo.BASE_LEN];
		HandProtocolHandleMenu mHandle;
		
		bCmd=pbDataBuf[HandProtocolInfo.CMD_POS];
		System.arraycopy(pbDataBuf,(HandProtocolInfo.CMD_POS+1),pbBuf,0,(iDataLen-HandProtocolInfo.BASE_LEN));
		for(i=0;i<listHandleMenu.size();i++)
		{
			mHandle=(HandProtocolHandleMenu)listHandleMenu.get(i);
			
			//Log.i("HandProtocol", "analyseData" +Arrays.toString(pbBuf)+iDataLen);
			if(bCmd==mHandle.getCmd())
			{
				blRet=mHandle.handleProtocol(pbBuf,(iDataLen-HandProtocolInfo.BASE_LEN),o_pbDataBuf,o_iLen);
			}
			else
			{
			}
		}
		return blRet; 
	}
	/*****************************************************************************
	-Fuction		: packData
	-Description	: packData
	-Input			: i_pbDataBuf i_iLen
	-Output 		: o_pbDataBuf o_iLen
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public  void packData(byte i_bCmdID,byte[] i_pbDataBuf,int i_iLen,byte[] o_pbDataBuf,int[] o_iLen)throws Exception
	{
		char cCrc;
		if((null==o_pbDataBuf)||(null==o_iLen)||(Array.getLength(o_pbDataBuf)!=i_iLen+HandProtocolInfo.BASE_LEN))
		{
			Log.i("HandProtocol", "PackData err"+Array.getLength(o_pbDataBuf));
			throw new Exception("PackData err");
		}
		else
		{
			o_pbDataBuf[0]=HandProtocolInfo.SEND_FIRST_BYTE;
			o_pbDataBuf[1]=HandProtocolInfo.SEND_SECOND_BYTE;

			o_pbDataBuf[4]=i_bCmdID;
			System.arraycopy(i_pbDataBuf,0,o_pbDataBuf,5,i_iLen);
			o_iLen[0]=5+i_iLen;
			
			o_iLen[0]++;//crc
			o_iLen[0]++;//crc
			o_pbDataBuf[o_iLen[0]]=HandProtocolInfo.SECOND_LAST_BYTE;
			o_iLen[0]++;
			o_pbDataBuf[o_iLen[0]]=HandProtocolInfo.LAST_BYTE;
			o_iLen[0]++;
			
			o_pbDataBuf[2]=(byte)(o_iLen[0]&0x000000ff);
			o_pbDataBuf[3]=(byte)(o_iLen[0]>>8&0x000000ff);

			
			cCrc=(char)Crc.MakeCrc(o_pbDataBuf,o_iLen[0]-4);
			o_pbDataBuf[o_iLen[0]-4]=(byte)(cCrc&0x00ff);
			o_pbDataBuf[o_iLen[0]-3]=(byte)(cCrc>>8&0x00ff);	
		}
	}
	/*****************************************************************************
	-Fuction		: sendData
	-Description	: sendData
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public  void sendData(byte[] i_pbDataBuf,int i_iLen)
	{
		byte bSendDataBuf[]=null;
		if(null==BluetoothMsg.BleRemoteDevice)
		{
			Log.i("HandProtocol", "sendData err");
		}
		else
		{
			bSendDataBuf=Arrays.copyOf(i_pbDataBuf,i_iLen);
			BluetoothMsg.BleRemoteDevice.write(bSendDataBuf);
		}
	}
}
/*****************************************************************************
-Class			: HandProtoclInfo
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class HandProtocolInfo
{
	public static final byte BASE_LEN=9;	/*9=2+2+1+2+2*/
	public static final byte CMD_POS=4;	/*9=2+2+1+2+2*/

	public static final byte READ_PARA_OPR_CMD					=(byte)0x0b;
	public static final byte READ_FUNCTION_STATE_OPR_CMD		=(byte)0x0a;
	public static final byte SEND_PARA_OPR_CMD					=(byte)0x07;
	public static final byte SEND_FUNCTION_STATE_OPR_CMD		=(byte)0x0a;
	public static final byte GET_PARA=0;
	public static final byte SET_PARA=1;
	public static final byte SIM_CARD_0='0';
	public static final byte SIM_CARD_1='1';
	class HandProtocolSubCmdId
	{
		public static final byte OWN_NUMBER					=(byte)0x01;		//��������//
		public static final byte PASSWORD_SET				=(byte)0x02;		//��������//
		public static final byte CENTRE_IP_PORT				=(byte)0x03;		//����IP��ַ�Ͷ˿ں�//
		public static final byte APN_NAME						=(byte)0x04;		//APN//
		public static final byte WORKMODE					=(byte)0x05;		//����ģʽ//
		public static final byte CONTROL_NUM0				=(byte)0x06;		//�������ĺ���0//
		public static final byte SMS_CENTER_NUM0			=(byte)0x07;		//�������ĺ���0//
		public static final byte LISTEN_NUM0 					=(byte)0x08;		//�������ĺ���0//
		public static final byte  FIXED_NUM					=(byte)0x09;		//�̶��ɲ���绰����//
		public static final byte  MAX_SPEED					=(byte)0x0a;		//����ٶ�ֵ//
		public static final byte  LOW_VOLTAGE 				=(byte)0x0b;		//�͵�ѹ��׼ֵ//
		public static final byte  ALARM_INTERVAL				=(byte)0x0c;		//�����ϴ�ʱ����//
		public static final byte  GPRS_TIMEOUT				=(byte)0x0d;		//GPRS���Ӽ���ʱ��//
		
		public static final byte  GPI_STATE						=(byte)0x0f;		//�����״̬//
		public static final byte  SPEAKING_MODE				=(byte)0x10;		//ͨ��ģʽ//
		public static final byte  SAVEING_POWER_MODE		=(byte)0x11;		//ʡ��ģʽ//
		public static final byte  CONTROL_NUM1				=(byte)0x16;		//�������ĺ���1//
		public static final byte  SMS_CENTER_NUM1			=(byte)0x17;		//�������ĺ���1//
		public static final byte  LISTEN_NUM1 					=(byte)0x18;		//�������ĺ���1//
		public static final byte  SOS_NUM 						=(byte)0x19;		//SOS����//
		public static final byte  SERIAL_NUM					=(byte)0x20;		//�������к�//
		public static final byte  TRANSFER_INTERVAL			=(byte)0x4e;		//����OBD����͸���ϴ����//
		public static final byte  OEMID						=(byte)0x50; 	 	// �豸���̴���
		public static final byte  LICENSE_PLATE				=(byte)0x46;		// ���ƺ���
		public static final byte  LICENSE_PLATE_COLOR 		=(byte)0x52;  		// ������ɫ
 
	}

	
	public static final byte RECV_FIRST_BYTE 		='@';
	public static final byte RECV_SECOND_BYTE 	='@';
	public static final byte SECOND_LAST_BYTE	 ='\r';
	public static final byte LAST_BYTE				 ='\n';

	public static final byte SEND_FIRST_BYTE		 ='$';
	public static final byte SEND_SECOND_BYTE	 ='$';
}
/*****************************************************************************
-Class			: HandProtocolHandMenu
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class HandProtocolHandleMenu
{
	private byte bCmd=0;

	public byte getCmd()
	{
		return bCmd;
	}
	public void setCmd(byte i_bCmd)
	{
		bCmd=i_bCmd;
	}
	public boolean handleProtocol(byte[] i_pbDataBuf,int i_iLen,byte[] o_pbDataBuf,int[] o_iLen)
	{
		Log.i("HandProtocolHandleMenu","handleProtocol null");
		return false;
	}
}
/*****************************************************************************
-Class			: ParameterOperation
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetParameter extends HandProtocolHandleMenu
{
	private List listGetParamMenu;
	GetParameter()
	{
		super.setCmd(HandProtocolInfo.READ_PARA_OPR_CMD);
		//construct GetParamMenu
		listGetParamMenu = Collections.synchronizedList(new LinkedList());
		GetOwnNumber mGetOwnNumber=new GetOwnNumber();
		listGetParamMenu.add(mGetOwnNumber);
		
		GetMainGprs mGetMainGprs=new GetMainGprs();
		listGetParamMenu.add(mGetMainGprs);

		GetMainApn mGetMainApn=new GetMainApn();
		listGetParamMenu.add(mGetMainApn);
	}
	/*****************************************************************************
	-Fuction		: handleProtocol
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean handleProtocol(byte[] i_pbDataBuf,int i_iLen,byte[] o_pbDataBuf,int[] o_iLen)
	{
		byte bSubCmdId=i_pbDataBuf[0];
		boolean blRet=false;
		int i;
		HandProtocolGetPara mGetParam;
		byte[] pbBuf=new byte[i_iLen-1];//Uncontain SubCmd
		System.arraycopy(i_pbDataBuf,1,pbBuf,0,i_iLen-1);
		for(i=0;i<listGetParamMenu.size();i++)
		{
			mGetParam=(HandProtocolGetPara)listGetParamMenu.get(i);
			
			//Log.i("GetParameter", "handleProtocol" +bSubCmdId+Arrays.toString(i_pbDataBuf)+mGetParam.getSubCmd());
			if(bSubCmdId==mGetParam.getSubCmd())
			{
				blRet=mGetParam.getPara(pbBuf,i_iLen-1);
			}
			else
			{
			}
		}
		return blRet; 
	}
}
/*****************************************************************************
-Class			: GetFunctionState
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetFunctionState extends HandProtocolHandleMenu
{
	GetFunctionState()
	{
		super.setCmd(HandProtocolInfo.READ_FUNCTION_STATE_OPR_CMD);
	}
	/*****************************************************************************
	-Fuction		: handleProtocol
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean handleProtocol(byte[] i_pbDataBuf,int i_iLen,byte[] o_pbDataBuf,int[] o_iLen)
	{
		byte bSubCmdId=i_pbDataBuf[0];
		boolean blRet=false;

		
		return blRet;
	}
}

