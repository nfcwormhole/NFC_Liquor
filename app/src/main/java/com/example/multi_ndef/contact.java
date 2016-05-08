/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : contact.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : contact
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.example.multi_ndef.wifi.ReadDataFromWifi;


import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class contact extends Activity{
	
	 private CNFCInterface ma ;
	 
		
		
		 String _string2parse;
		 Scanner _scanner;
		 String V_data;
		 
		 String name;
		 String address;
		 String mail;
		 String number;
		 
		 TextView v_name;
		 TextView v_number;
		 TextView v_mail;
		 TextView v_address;
		 Button add;
		 
		
		
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_contact);
		
	
		v_name = (TextView) findViewById(R.id.textView3);
		v_number = (TextView) findViewById(R.id.textView5);
		v_mail = (TextView) findViewById(R.id.textView7);
		v_address = (TextView) findViewById(R.id.textView9);
		
		add= (Button) findViewById(R.id.button1);
	    ma = (CNFCInterface)getApplication();
		//constructor

		 V_data =  new String(ma.getContactData());
		

		 _string2parse = V_data.toString();
			if (!_string2parse.isEmpty())
			{
				_scanner = new Scanner(_string2parse);
			}
		 
	  int c =	parse ();
	  add.setOnClickListener(new View.OnClickListener() {
		   
		    
			public void onClick(View v) {

				 addContactv2(name,number,mail,address);

			}
		});
		
	}
	
	public void addContactv2(String name, String phone, String mail,String address)
	{
		
	
		 String DisplayName = name;
		 String MobileNumber = phone;
	
		 String emailID = mail;
		 
		 String Address = address;
	
		 ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

		 ops.add(ContentProviderOperation.newInsert(
		 ContactsContract.RawContacts.CONTENT_URI)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		     .build());

		 //------------------------------------------------------ Names
		 if (DisplayName != null) {
		     ops.add(ContentProviderOperation.newInsert(
		     ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		         .withValue(
		     ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
		     DisplayName).build());
		 }

		 //------------------------------------------------------ Mobile Number                     
		 if (MobileNumber != null) {
		     ops.add(ContentProviderOperation.
		     newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		         .build());
		 }



		 //------------------------------------------------------ Email
		 if (emailID != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
		         .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
		         .build());
		 }

		 //------------------------------------------------------ Address
		 if (/*!Address.equals("") ||*/ Address!=null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.StructuredPostal.DATA,Address)
		         .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
		        .build());
		 }

		 // Asking the Contact provider to create a new contact                 
		 try {
		     getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			 Toast toast = Toast.makeText(getApplicationContext(),
						"Added"+" "+name+" "+"to Phonebook",  Toast.LENGTH_LONG);
				toast.show();
		     
		 } catch (Exception e) {
		     e.printStackTrace();
		     Toast.makeText(getApplicationContext() ,"Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		 } 
		
	}
	
	// Unused code Depreciated 

	@SuppressWarnings("deprecation")
	private void addContact(String name, String phone) {
		 ContentValues values = new ContentValues();
		 values.put(People.NUMBER, phone);
		 values.put(People.TYPE, Phone.TYPE_CUSTOM);
		 values.put(People.LABEL, name);
		 values.put(People.NAME, name);
		
		 Uri dataUri = getContentResolver().insert(People.CONTENT_URI, values);
		 Uri updateUri = Uri.withAppendedPath(dataUri, People.Phones.CONTENT_DIRECTORY);
		 values.clear();
		 values.put(People.Phones.TYPE, People.TYPE_MOBILE);
		 values.put(People.NUMBER, phone);
		 updateUri = getContentResolver().insert(updateUri, values);
		 
	
		 Toast toast = Toast.makeText(getApplicationContext(),
					"Added"+" "+name+" "+"to Phonebook",  Toast.LENGTH_LONG);
			toast.show();	
	
	}
	
	
	public String appendStringFromArrayString(String [] arrayString)
	{
		StringBuffer result = new StringBuffer();
		for (int i = 0; i<arrayString.length;i++)
		{
			result.append(arrayString[i]);
			result.append(" ");
		}
		return result.toString();
	}
	
	
	public int parse()
	{
		_scanner.useDelimiter("\n");
		while (_scanner.hasNext())
		{
			String item = _scanner.next();
			//Log.d("Parser",item);
			String property[] =  item.split(":");
			if (property.length<2)
			{
				continue;
			}
			String propertyName[] =  property[0].split(";");
			String propertyValue[] =  property[1].split(";");
			if (propertyName[0].equals("BEGIN"))
			{
				Log.d("Parser","BEGIN Flag detected!");
			}
			else if (propertyName[0].equals("END"))
			{
				Log.d("Parser","END Flag detected!");
			}
			else if (propertyName[0].equals("VERSION"))
			{
				Log.d("Parser","VERSION Flag detected!");
				Log.d("Parser","Current Version is  " + propertyValue[0]);
			}
			else if (propertyName[0].equals("N"))
			{
				Log.d("Parser","NAME Flag detected!");
				Log.d("Parser","Current NAME is  " + Arrays.toString(propertyValue));
				name = appendStringFromArrayString(propertyValue);
			 	
			v_name.setText(name);
				
			}
			
			else if (propertyName[0].equals("EMAIL"))
			{
				Log.d("Parser","EMAIL Flag detected!");
				Log.d("Parser","Current EMAIL is" + Arrays.toString(propertyValue));
			  mail = appendStringFromArrayString(propertyValue);
			  v_mail.setText(mail);
			 
			}
			
			else if (propertyName[0].equals("TEL"))
			{
				Log.d("Parser","TEL Flag detected!");
				Log.d("Parser","Current TEL is" + Arrays.toString(propertyValue));
				number = appendStringFromArrayString(propertyValue);
				
				v_number.setText(number);
				
				
			}
			else if (propertyName[0].equals("ADR"))
			{
				Log.d("Parser","ADR Flag detected!");
				Log.d("Parser","Current ADR is" + Arrays.toString(propertyValue));
				address =appendStringFromArrayString(propertyValue);
				
				v_address.setText(address);
			}
			else
			{
				Log.d("Parser","Token "+propertyName[0]+" not yet handled!");	
			}
			
		}
		return 0;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
	
		return true;
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/
