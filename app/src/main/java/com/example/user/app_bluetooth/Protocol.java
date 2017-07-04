/*****************************************************************************
* Copyright (C) 2017-2020 China Aerospace Telecommunications Ltd.  All rights reserved.
------------------------------------------------------------------------------
* File Module		: 	Protocol.java
* Description		: 	Protocol operation center
* Created			: 	2017.06.22.
* Author			: 	Yu Weifeng
* Function List 		: 	
* Last Modified 	: 	
* History			: 	
******************************************************************************/

package com.example.user.app_bluetooth;



/*****************************************************************************
-Class			: Protocol
-Description	: 
* Modify Date	  Version		 Author 		  Modification
* -----------------------------------------------
* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
******************************************************************************/
public abstract class Protocol 
{
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
	public abstract boolean checkData();
	/*****************************************************************************
	-Fuction		: analyseData
	-Description	: analyseData
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public abstract boolean analyseData(byte[] o_pbDataBuf,int[] o_iLen);
	/*****************************************************************************
	-Fuction		: packData
	-Description	: packData
	-Input			: 
	-Output 		: 
	-Return 		: 
	* Modify Date	  Version		 Author 		  Modification
	* -----------------------------------------------
	* 2017/06/22	  V1.0.0		 Yu Weifeng 	  Created
	******************************************************************************/
	public abstract void packData(byte i_bCmdID,byte[] i_pbDataBuf,int i_iLen,byte[] o_pbDataBuf,int[] o_iLen)throws Exception;
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
	public abstract void sendData(byte[] i_pbDataBuf,int i_iLen);
}
