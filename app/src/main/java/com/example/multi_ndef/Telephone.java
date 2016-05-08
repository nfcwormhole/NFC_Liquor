/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : Telephone.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : Telephone
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

public class Telephone extends Activity  implements OnClickListener{
	Button call;
	byte[] contactInByte; 
	String contactData;
	Scanner _scanner;
	Uri uri;
	String item,contacts;
	
	TextView contact;
	 private CNFCInterface ma; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telephone);

		call=(Button)findViewById(R.id.call);
		contact=(TextView)findViewById(R.id.ph_number);
		
		call.setOnClickListener(Telephone.this);
		ma = (CNFCInterface)getApplication();
		
		
		
		contactData=new String(ma.getTelephoneData(),Charset.forName("US-ASCII"));
		
		item=contactData;
		 //_string2parse = smsData.toString();
			if (!contactData.isEmpty())
			{
				
				parse();
			}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.telephone, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(((Button)v).getId()==call.getId())
		{
			createCall();
		}
	}
	
	

	protected void createCall() {
	      Log.i("Making a call", "");

	      Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"+contacts));
			
	      
	      try {
	    	  startActivity(callIntent);
	         finish();
	         Log.i("Call connected...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(Telephone.this, 
	         "call failed, please try again later.", Toast.LENGTH_SHORT).show();
	      }
	   }
	
	public int parse()
	{
			String ContactData[] =  item.split(":");
		
			contacts=item.substring(1);
		
			contact.setText(contacts);
		
		return 0;
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/