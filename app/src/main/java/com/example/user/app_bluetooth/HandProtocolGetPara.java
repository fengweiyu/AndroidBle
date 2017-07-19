/*****************************************************************************
* Copyright (C) 2017-2020 China Aerospace Telecommunications Ltd.  All rights reserved.
------------------------------------------------------------------------------
* File Module		: 	HandProtocolGetPara.java
* Description		: 	HandProtocolGetPara operation center
* Created			: 	2017.06.22.
* Author			: 	Yu Weifeng
* Function List 		: 	
* Last Modified 	: 	
* History			: 	
******************************************************************************/

package com.example.user.app_bluetooth;

import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*****************************************************************************
-Class			: HandProtocolGetPara
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
public class HandProtocolGetPara 
{
	private byte bSubCmd=0;

	public byte getSubCmd()
	{
		return bSubCmd;
	}
	public void setSubCmd(byte i_bCmd)
	{
		bSubCmd=i_bCmd;
	}
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		Log.i("HandProtocolGetPara", "getPara null");
		return false;
	}

}
class GetParaQueue
{
	public static final int OBJ_QUEUE_SIZE = 30;
	public static BlockingQueue<Object> ViewParaQueue = new ArrayBlockingQueue<Object>(OBJ_QUEUE_SIZE);
}
class ViewPara
{
	private byte bCmdId;
	private byte[] bDataBuf;
	ViewPara(byte i_bCmdId,byte[] i_bDataBuf)
	{
		bCmdId=i_bCmdId;
		bDataBuf=Arrays.copyOf(i_bDataBuf,i_bDataBuf.length);
	}
	public byte GetCmdId()
	{
		return bCmdId;
	}
	public byte[] GetDataBuf()
	{
		return bDataBuf;
	}
}
/*****************************************************************************
-Class			: GetOwnNumber
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetOwnNumber extends HandProtocolGetPara
{
	GetOwnNumber()
	{
		super.setSubCmd(HandProtocolInfo.HandProtocolSubCmdId.OWN_NUMBER);
	}
	/*****************************************************************************
	-Fuction		: getPara
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		boolean blRet;
		//Log.i("GetOwnNumber","getPara"+Arrays.toString(i_pbDataBuf)+i_pbDataBuf.length);
		ViewPara mViewPara=new ViewPara(HandProtocolInfo.HandProtocolSubCmdId.OWN_NUMBER,i_pbDataBuf);
		try
		{
			GetParaQueue.ViewParaQueue.put(mViewPara);
		}catch(InterruptedException e)
		{
			Log.i("GetOwnNumber","Exception:"+e);
		}
		blRet=true;
		return blRet;
	}
}
/*****************************************************************************
-Class			: GetMainGprs
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetMainGprs extends HandProtocolGetPara
{
	GetMainGprs()
	{
		super.setSubCmd(HandProtocolInfo.HandProtocolSubCmdId.CENTRE_IP_PORT);
	}
	/*****************************************************************************
	-Fuction		: getPara
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		boolean blRet;
		//Log.i("GetOwnNumber","getPara"+Arrays.toString(i_pbDataBuf)+i_pbDataBuf.length);
		ViewPara mViewPara=new ViewPara(HandProtocolInfo.HandProtocolSubCmdId.CENTRE_IP_PORT,i_pbDataBuf);
		try
		{
			GetParaQueue.ViewParaQueue.put(mViewPara);
		}catch(InterruptedException e)
		{
			Log.i("GetOwnNumber","Exception:"+e);
		}
		blRet=true;
		return blRet;
	}
}
/*****************************************************************************
-Class			: GetMainApn
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetMainApn extends HandProtocolGetPara
{
	GetMainApn()
	{
		super.setSubCmd(HandProtocolInfo.HandProtocolSubCmdId.APN_NAME);
	}
	/*****************************************************************************
	-Fuction		: getPara
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		boolean blRet;
		//Log.i("GetOwnNumber","getPara"+Arrays.toString(i_pbDataBuf)+i_pbDataBuf.length);
		ViewPara mViewPara=new ViewPara(HandProtocolInfo.HandProtocolSubCmdId.APN_NAME,i_pbDataBuf);
		try
		{
			GetParaQueue.ViewParaQueue.put(mViewPara);
		}catch(InterruptedException e)
		{
			Log.i("GetOwnNumber","Exception:"+e);
		}
		blRet=true;
		return blRet;
	}
}
/*****************************************************************************
-Class			: GetWorkMode
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetWorkMode extends HandProtocolGetPara
{
	GetWorkMode()
	{
		super.setSubCmd(HandProtocolInfo.HandProtocolSubCmdId.WORKMODE);
	}
	/*****************************************************************************
	-Fuction		: getPara
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		boolean blRet;
		//Log.i("GetOwnNumber","getPara"+Arrays.toString(i_pbDataBuf)+i_pbDataBuf.length);
		ViewPara mViewPara=new ViewPara(HandProtocolInfo.HandProtocolSubCmdId.WORKMODE,i_pbDataBuf);
		try
		{
			GetParaQueue.ViewParaQueue.put(mViewPara);
		}catch(InterruptedException e)
		{
			Log.i("GetWorkMode","Exception:"+e);
		}
		blRet=true;
		return blRet;
	}
}

/*****************************************************************************
-Class			: GetMainApn
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetFixedReportTime extends HandProtocolGetPara
{
	GetFixedReportTime(){super.setSubCmd(HandProtocolInfo.HandProtocolSubCmdId.FIXED_NUM);}
	/*****************************************************************************
	-Fuction		: getPara
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		boolean blRet;
		//Log.i("GetOwnNumber","getPara"+Arrays.toString(i_pbDataBuf)+i_pbDataBuf.length);
		ViewPara mViewPara=new ViewPara(HandProtocolInfo.HandProtocolSubCmdId.FIXED_NUM,i_pbDataBuf);
		try
		{
			GetParaQueue.ViewParaQueue.put(mViewPara);
		}catch(InterruptedException e)
		{
			Log.i("GetOwnNumber","Exception:"+e);
		}
		blRet=true;
		return blRet;
	}
}

/*****************************************************************************
-Class			: GetMainApn
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetProvinceID extends HandProtocolGetPara
{
	GetProvinceID(){super.setSubCmd(HandProtocolInfo.HandProtocolSubCmdId.PROVINCE_ID);}
	/*****************************************************************************
	-Fuction		: getPara
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		boolean blRet;
		//Log.i("GetOwnNumber","getPara"+Arrays.toString(i_pbDataBuf)+i_pbDataBuf.length);
		ViewPara mViewPara=new ViewPara(HandProtocolInfo.HandProtocolSubCmdId.PROVINCE_ID,i_pbDataBuf);
		try
		{
			GetParaQueue.ViewParaQueue.put(mViewPara);
		}catch(InterruptedException e)
		{
			Log.i("GetProvinceID","Exception:"+e);
		}
		blRet=true;
		return blRet;
	}
}

/*****************************************************************************
-Class			: GetMainApn
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetCityID extends HandProtocolGetPara
{
	GetCityID(){super.setSubCmd(HandProtocolInfo.HandProtocolSubCmdId.CITY_ID);}
	/*****************************************************************************
	-Fuction		: getPara
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		boolean blRet;
		//Log.i("GetOwnNumber","getPara"+Arrays.toString(i_pbDataBuf)+i_pbDataBuf.length);
		ViewPara mViewPara=new ViewPara(HandProtocolInfo.HandProtocolSubCmdId.CITY_ID,i_pbDataBuf);
		try
		{
			GetParaQueue.ViewParaQueue.put(mViewPara);
		}catch(InterruptedException e)
		{
			Log.i("GetCityID","Exception:"+e);
		}
		blRet=true;
		return blRet;
	}
}

/*****************************************************************************
-Class			: GetMainApn
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
class GetTerminalID extends HandProtocolGetPara
{
	GetTerminalID(){super.setSubCmd(HandProtocolInfo.HandProtocolSubCmdId.TERMINAL_ID);}
	/*****************************************************************************
	-Fuction		: getPara
	-Description	: Override
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public boolean getPara(byte[] i_pbDataBuf,int i_iLen)
	{
		boolean blRet;
		//Log.i("GetOwnNumber","getPara"+Arrays.toString(i_pbDataBuf)+i_pbDataBuf.length);
		ViewPara mViewPara=new ViewPara(HandProtocolInfo.HandProtocolSubCmdId.TERMINAL_ID,i_pbDataBuf);
		try
		{
			GetParaQueue.ViewParaQueue.put(mViewPara);
		}catch(InterruptedException e)
		{
			Log.i("GetTerminalID","Exception:"+e);
		}
		blRet=true;
		return blRet;
	}
}

