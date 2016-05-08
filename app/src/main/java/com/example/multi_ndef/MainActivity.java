/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : MainActivity.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : MainActivity
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

import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button write;
	Button read;
	 public byte Text []= null;
	 public byte Uri []= null;
	 public byte Contact []= null;
	 public byte WiFi []= null;
	 public byte BT []= null;
	// ReadDataFromDualEEPROM theObject;
	 ProgressDialog theReadProgressBar;
	 
	 private CNFCInterface ma = (CNFCInterface)getApplication();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	 write = (Button)findViewById(R.id.button2); 
		  
		  write.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					//Log.i("BTN", "NDEF FORMAT");
					try
					{
					Intent i = new Intent(getApplicationContext() ,Frag_Write.class);
					startActivity(i);
					}
					catch(Exception e)
					{
						Toast toast = Toast.makeText(getApplicationContext(),
								e.toString(), Toast.LENGTH_LONG);
						toast.show();
		
					}
				}
			});
		  
	
		  
 read = (Button)findViewById(R.id.button1); 
		  
		  read.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					//Log.i("BTN", "NDEF FORMAT");
								
					Toast toast = Toast.makeText(getApplicationContext(),
							"Please bring the tag close to phone", Toast.LENGTH_LONG);
					toast.show();
	
					
				}
			});
		  
		//Enable bluetooth
		  BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
		  mBluetoothAdapter.enable(); 
		  if (mBluetoothAdapter.isDiscovering()) {
		      mBluetoothAdapter.enable(); 
		  } 
		  
		  
			WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			boolean a;
			a= wifiManager.isWifiEnabled();
			if(!a)
			{
			wifiManager.setWifiEnabled(true);
			}
		  
	}



}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/