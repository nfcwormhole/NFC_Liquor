/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : SMS.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : SMS
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

public class SMS extends Activity  implements OnClickListener {
	Button send;
	TextView contact,smsBody;
	 private CNFCInterface ma; 
	
		byte[] smsInByte; 
		String smsData;
		Scanner _scanner;
		String item,contacts="";
		
		String smsbody="";
		String recData="";
		

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		
		send=(Button)findViewById(R.id.send);
		contact=(TextView)findViewById(R.id.show_rec);
		smsBody=(TextView)findViewById(R.id.show_msg);
		send.setOnClickListener(SMS.this);
		
		
		ma = (CNFCInterface)getApplication();
		
		
		smsData=new String(ma.getSMSData(),Charset.forName("US-ASCII"));
		item=smsData;
		
			if (!smsData.isEmpty())
			{
				
				parse();
			}
	}

	
	
	@Override
	public void onClick(View v) {
	
	if(((Button)v).getId()==send.getId())
	{
		sendSMS();
	}
	}
	
	
	protected void sendSMS() {
	      Log.i("Send SMS", "");

	      Intent smsIntent = new Intent(Intent.ACTION_VIEW);
	      smsIntent.setData(Uri.parse("smsto:"));
	      smsIntent.setType("vnd.android-dir/mms-sms");

	     smsIntent.putExtra("address"  , recData);
	      smsIntent.putExtra("sms_body"  , smsbody);
	      try {
	         startActivity(smsIntent);
	         finish();
	         Log.i("Finished sending SMS...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(SMS.this, 
	         "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
	      }
	   }
	
	
	
	public int parse()
	{
		
			String property[] =  new String[2];
			int pos=item.indexOf('?');
			
			property[0]=item.substring(0, pos);
			property[1]=item.substring(pos+1);
			
			String ContactData[] =  property[0].split(":");
			if(ContactData.length!=1)
			{
			contacts=ContactData[1];
			String SmsData[] =  property[1].split("=");
			if(SmsData.length!=1)
			{
			smsbody=SmsData[1];
			
			
			smsBody.setText(smsbody);
			
			}
		
			recData=ContactData[1];
				String[] Contacts=ContactData[1].split(";");
				contacts=appendStringFromArrayString(Contacts);
			
				contact.setText(contacts);
			
			}
			
			
			
		
		return 0;
	}
	
	
	public String appendStringFromArrayString(String [] arrayString)
	{
		StringBuffer result = new StringBuffer();
		for (int i = 0; i<arrayString.length;i++)
		{
			result.append(arrayString[i]);
			result.append("   ");
		}
		return result.toString();
	}
	

}

/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/