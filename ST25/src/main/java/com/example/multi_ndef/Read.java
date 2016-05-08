/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : Read.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : Read
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


import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import android.net.Uri;


public class Read extends Activity 
{
	
	 public byte Text []= null;
	 public byte UriValue []= null;
	 public byte Contact []= null;
	 public byte WiFi []= null;
	 public byte BT []= null;
	 public byte Sms[]=null;
	 public byte Mail[]=null;
	 public byte Telephone[]=null;
	 public byte Location[]=null;
	 private CNFCInterface ma;
	 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read);
		ma = (CNFCInterface)getApplication();
		
		Button b_text = (Button) findViewById(R.id.button1);
		Button b_uri = (Button) findViewById(R.id.button2);
		Button b_contact = (Button) findViewById(R.id.button3);
		Button b_wifi = (Button) findViewById(R.id.button4);
		Button b_bt  = (Button) findViewById(R.id.button5);
		Button b_sms  = (Button) findViewById(R.id.btnsms);
		Button b_mail  = (Button) findViewById(R.id.btnmail);
		Button b_telephone  = (Button) findViewById(R.id.btntelephone);
		Button b_location  = (Button) findViewById(R.id.btnlocation);
		
		
	
       Intent intent = getIntent();
        if(intent.getType() != null && intent.getType().equals(MimeType.AppName)) {
        	Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            NdefRecord cardRecord  = msg.getRecords()[0];
            NdefRecord cardRecord2 = msg.getRecords()[1];
            NdefRecord cardRecord3 = msg.getRecords()[2];
            NdefRecord cardRecord4 = msg.getRecords()[3];
            NdefRecord cardRecord5 = msg.getRecords()[4];
            NdefRecord cardRecord6 = msg.getRecords()[5];
            NdefRecord cardRecord7 = msg.getRecords()[6];
            NdefRecord cardRecord8 = msg.getRecords()[7];
            NdefRecord cardRecord9 = msg.getRecords()[8];
            try
            {
            	String str=(new String(cardRecord8.getPayload(),Charset.forName("US-ASCII")));
            	Uri uri=Uri.parse(str);
            	str=uri.getScheme();
            	
            	
            	
           ma.setTelephoneUri(uri );
        }
		catch(Exception e)
		{
			 Toast.makeText(Read.this, 
			         "uri format not supported", Toast.LENGTH_SHORT).show();
		}
            WiFi =  cardRecord.getPayload();
            Text =  cardRecord2.getPayload();
            UriValue =  cardRecord3.getPayload();
            
            
        	
         //   Contact =  cardRecord5.getPayload();
         
           BT =  cardRecord4.getPayload();
           
           Contact = cardRecord5.getPayload();
           
           Sms = cardRecord6.getPayload();
           
           Mail = cardRecord7.getPayload();
           
           Telephone = cardRecord8.getPayload();
           
           Location = cardRecord9.getPayload();
            
            
           Toast toast = Toast.makeText(getApplicationContext(),
					"Sucessfully read multiple NDEF records", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
			toast.show();
           
            
            try 
            {
            ma.setTextData(Text);
            
            }
            
            catch (Exception e)
            {
            	
            	String a = e.toString();
            	String b;
            	
            	int f= 9;
            	
            	
            	int aa=0;
            	int c= f+aa;
            	
            }
            ma.setUriData(UriValue);
    
            ma.setWiFiData(WiFi);
            ma.setBTData(BT);
            ma.setContactData(Contact);
            ma.setSMSData(Sms);
            ma.setMailData(Mail);
            ma.setTelephoneData(Telephone);
            ma.setLocationData(Location);
            
            
            
            
            String msg1 =  new String(ma.getTextData());
            
            int a;
            
  
		b_text.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,text.class);
				startActivity(i);
			}
		});
		
		
		b_uri.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,uri.class);
				startActivity(i);
			}
		});
		
		
	
		
		b_wifi.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,wifi.class);
				startActivity(i);
			}
		});
		
		
		b_bt.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,bt.class);
				startActivity(i);
			}
		});
		
		
		
		b_contact.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,contact.class);
				startActivity(i);
			}
		});
		
		
		b_sms.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,SMS.class);
				startActivity(i);
			}
		});
		
		
		b_mail.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,Mail.class);
				startActivity(i);
			}
		});
		
		
		b_telephone.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,Telephone.class);
				startActivity(i);
			}
		});
		
		b_location.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Log.i("BTN", "NDEF FORMAT");
				Intent i = new Intent(Read.this ,Location.class);
				startActivity(i);
			}
		});
		
	}

}
}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/