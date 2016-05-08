/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : Location.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : Location
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

import java.nio.charset.Charset;
import java.util.Scanner;


import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Location extends Activity implements OnClickListener{
	
	Button map;
	byte[] locationInByte; 
	String locationData;
	Scanner _scanner;
	Uri uri;
	String item,contacts;
	 private CNFCInterface ma; 
	TextView latitude,longitude;
	String latitudeValue,longitudeValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		map=(Button)findViewById(R.id.map);
		latitude=(TextView)findViewById(R.id.labellatitude);
		longitude=(TextView)findViewById(R.id.labellongitude);
		
		map.setOnClickListener(Location.this);
		Intent intent = getIntent();
		
		ma = (CNFCInterface)getApplication();
		locationData=new String(ma.getLocationData(),Charset.forName("US-ASCII"));
		
		item=locationData;
		 //_string2parse = smsData.toString();
			if (!locationData.isEmpty())
			{
				
				parse();
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
	
	if(((Button)v).getId()==map.getId())
	{
		showMap();
	}
	}
	
	
	protected void showMap() {
	      Log.i("creating connection", "");

	      Intent mapIntent = new Intent();
			mapIntent.setData(Uri.parse("geo:"+latitudeValue+","+longitudeValue));
			mapIntent.setAction(Intent.ACTION_VIEW);
			
	      
	      try {
	    	  startActivity(mapIntent);
	         finish();
	         Log.i("Map shown...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(Location.this, 
	         "connection failed, please try again later.", Toast.LENGTH_SHORT).show();
	      }
	   }
	
	
	
	public int parse()
	{
				
			
			String locationdata[] =  item.split(":");
			String locations[]=new String[2];

				locations=locationdata[1].split(",");
	
				if(locations.length!=0)
				{
				latitudeValue=locations[0];
				longitudeValue=locations[1];
		
		latitude.setText(locations[0]);
		longitude.setText(locations[1]);
			}
		return 0;
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/