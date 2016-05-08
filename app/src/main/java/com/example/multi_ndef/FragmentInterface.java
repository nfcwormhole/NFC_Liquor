/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : FragmentInterface.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : FragmentInterface
********************************************************************************
* THE PRESENT SOFTWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS
* WITH PRODUCT CODING INFORMATION TO SAVE TIME.
* AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY DIRECT,
* INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS ARISING FROM THE
* CONTENT OF THE SOFTWARE AND/OR THE USE MADE BY CUSTOMERS OF THE CODING
* INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.
*
* THIS SOURCE CODE IS PROTECTED BY A LICENSE AGREEMENT.
* FOR MORE INFORMATION PLEASE READ CAREFULLY THE LICENSE AGREEMENT FILE LOCATED
* IN THE ROOT DIRECTORY OF THIS SOFTWARE PACKAGE.
*******************************************************************************/
package com.example.multi_ndef;

import android.app.Application;

public class FragmentInterface extends Application {
	
	private String phone;
	private String item;
	private String network,password;
	
	
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public void setItem(String item)
	{
		this.item=item;
	}
	
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getItem()
	{
		return item;
	}
	
	public void setWifiNetwork(String network)
	{
		this.network=network;
	}

	
	public String getWifiNetwork()
	{
		return network;
	}
	
	public void setWifiPassword(String password)
	{
		this.password=password;
	}

	
	public String getWifiPassword()
	{
		return password;
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/