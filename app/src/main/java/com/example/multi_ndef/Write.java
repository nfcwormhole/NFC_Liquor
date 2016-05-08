/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : Write.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : Write
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale; 
import java.util.Set;
import java.util.TimerTask;









//import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Write extends Activity implements OnClickListener {
	

	
        String network_name;
        String[] mac_add;
        String password;
        
		private NfcAdapter mAdapter;
		private boolean mInWriteMode;
		private Button mWriteTagButton;
		private TextView mTextView1;
		private TextView mTextView2;
		
		private EditText mEditText1,telephone;
		private EditText mUri;
		private EditText mTxt;
		String SMSpayload="",MultiContacts="",recipients="",Telepayload="";
		int noOfContacts=1;
		public EditText v_name;
		public EditText v_mail;
		public EditText v_number;
		public EditText v_add;
		EditText to,cc,bcc,subject,body;
		NdefRecord textRecord ;
		
		public Button fatchContact,addMore,fetchContact,fetchto,fetchcc,fetchbcc,location;
		String Locationpayload="",Mailpayload="",Sto="",Scc="",Sbcc="",Ssubject="";
		private Spinner mSpinner;
		int pass_length;
		int net_length;
		Handler updateBarHandler;
		private Spinner btSpinner;
		EditText numbers,message;
		String[] mac_addBT;
		String BT_Device_name="";
		int index_bt_item=0;
		String Mac_address="";
		Locale ENGLISH;
		boolean encodeInUtf8;
		NdefRecord uriRecord;
		TextView Recipients;
		private byte[] dataToWrite = new byte[200];
		private byte[] dataBT = new byte[200];
		List<String> SpinnerArrayBT_Device_name = new ArrayList<String>();
		List<String> SpinnerArrayBT_devices_mac = new ArrayList<String>();
		boolean status= false;
		String Control;
		double latitudevalue,longitudevalue;
		EditText latitude,longitude;
		GPSTracker gps;
		
		protected static final int PICK_CONTACT= 0,PICK_NUMBER=1,PICK_MAIL=2;
		 private CNFCInterface ma;
		
		//@SuppressLint("NewApi")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	      //  setContentView(R.layout.write_sr);
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        ma = (CNFCInterface)getApplication();
	        // grab our NFC Adapter
	        mAdapter = NfcAdapter.getDefaultAdapter(this);
	        
	        fatchContact= (Button) findViewById(R.id.button2);
	        
	        // button that starts the tag-write procedure
	        mWriteTagButton = (Button)findViewById(R.id.button1);
	        //mWriteTagButton.setOnClickListener(this);
	        
	        mUri = (EditText)findViewById(R.id.editText3);
	        mTxt = (EditText)findViewById(R.id.editText2);
	        // TextView that we'll use to output messages to screen
	        mTextView1 = (TextView)findViewById(R.id.textView1);
	        mTextView2 = (TextView)findViewById(R.id.textView2);
         
	        mEditText1 = (EditText)findViewById(R.id.editText1);
	        
	        v_name = (EditText) findViewById(R.id.editText4);
	        v_number = (EditText) findViewById(R.id.editText5);
	        v_mail = (EditText) findViewById(R.id.editText6);
	        v_add = (EditText) findViewById(R.id.editText7);
	        
	        
	        mSpinner = (Spinner)findViewById(R.id.spinner);
	        
	        btSpinner= (Spinner) findViewById(R.id.spinnerbt);
	        
	        addMore=(Button)findViewById(R.id.addRec);
			addMore.setOnClickListener(this);
			
			numbers=(EditText)findViewById(R.id.editcontact);
			Recipients=(TextView)findViewById(R.id.Recipients);
		
			message=(EditText)findViewById(R.id.editmessage);
			
			
			
			body=(EditText)findViewById(R.id.editBody);
			to=(EditText)findViewById(R.id.editTo);
			cc=(EditText)findViewById(R.id.editCc);
			bcc=(EditText)findViewById(R.id.editBcc);
			subject=(EditText)findViewById(R.id.editSubject);
			
			
			 fetchto=(Button)findViewById(R.id.addto);
			  fetchto.setOnClickListener(this);
				   
			  fetchcc=(Button)findViewById(R.id.addcc);
			  fetchcc.setOnClickListener(this);
			  
			  fetchbcc=(Button)findViewById(R.id.addbcc);
			  fetchbcc.setOnClickListener(this);
			  
			  
			  telephone = (EditText)findViewById(R.id.editTelephone);
			  
			  location=(Button)findViewById(R.id.location);
			  location.setOnClickListener(this);
				latitude=(EditText)findViewById(R.id.editlatitude);
				longitude=(EditText)findViewById(R.id.editlongitude);
				
	        
	  	  fatchContact.setOnClickListener(new View.OnClickListener() {
			   
			    
				public void onClick(View v) {



					Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
					startActivityForResult(intent, PICK_CONTACT);
			            
			            
			            
				}
			});
	        
	    	WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			boolean a;
			a= wifiManager.isWifiEnabled();
			if(!a)
			{
			wifiManager.setWifiEnabled(true);
			}
			
	        
	        updateBarHandler = new Handler();
	       
	       
	        mSpinner.setOnItemSelectedListener(new OnItemSelected());
	        btSpinner.setOnItemSelectedListener(new OnItemSelected());
			WifiManager wifi;
			try {
				wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

				// Get WiFi status
				WifiInfo info = wifi.getConnectionInfo();
				// List available networks
				// List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
				wifi.startScan();
				List<ScanResult> results;
				results = wifi.getScanResults();
				int array_length=results.size();
				int i = 0;
				String[] values = new String[array_length];
				for (ScanResult result : results) {
					values[i] = result.SSID;
					i = i + 1;
				}

				
				
				Set<String> set = new HashSet<String>();
				Collections.addAll(set, values);
				String[] uniques = set.toArray(new String[0]);
				int length = uniques.length;

				// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				// android.R.layout.simple_list_item_1, uniques);
				List<String> SpinnerArray = new ArrayList<String>();
				// SpinnerArray.addAll(set);
				for (int i1 = 1; i1 < length; i1++) {
					SpinnerArray.add(uniques[i1]);
				}

				// Create an ArrayAdapter using the string array and a default
				// spinner
				// layout
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, SpinnerArray);
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// Apply the adapter to the spinner
				mSpinner.setAdapter(adapter);

				initListener();

			} catch (Exception e) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Turn on the wifi", Toast.LENGTH_SHORT);
				toast.show();

			}
			
			//btSpinner.setOnClickListener(clicked());
			

			
//ArrayAdapter<String> btArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
		  //  android.R.layout.simple_list_item_1);
			
			
			
			try{
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			
			Set<BluetoothDevice> pairedDevices= bluetoothAdapter.getBondedDevices();
			
			if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				String deviceBTName = device.getName();
				String address=device.getAddress();
				//String deviceBTMajorClass = getBTMajorDeviceClass(device.getBluetoothClass().getMajorDeviceClass());
				SpinnerArrayBT_Device_name.add(deviceBTName);
				SpinnerArrayBT_devices_mac.add(address);
			}
			}
			}
			catch(Exception e)
			//SpinnerArrayBT.a
			{
				
				Toast toast = Toast.makeText(getApplicationContext(),
						"Bluetooth Code Error", Toast.LENGTH_SHORT);
				toast.show();
				
				int a1=10;
			}
    //ArrayAdapter<String>btArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);		
     ArrayAdapter<String>btArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpinnerArrayBT_Device_name);	
				
			
   btArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
	
	
		
		
		btSpinner.setAdapter(btArrayAdapter);
			
		
		
		
		
		
		
		  fetchContact=(Button)findViewById(R.id.fetchContact);
		fetchContact.setOnClickListener(new View.OnClickListener() {
			   
		    
			public void onClick(View v) {
				
				if(!numbers.getText().toString().isEmpty())
				{
				if(noOfContacts==1)
				{
					MultiContacts=numbers.getText().toString();
					noOfContacts++;
					numbers.setText("");
					recipients=MultiContacts;
					Recipients.setText(recipients);
				}
				else
				{
					MultiContacts+=";"+numbers.getText().toString();
					noOfContacts++;
					numbers.setText("");
					recipients=MultiContacts;
					Recipients.setText(recipients);
					
				}
				}



				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_NUMBER);
		            
		            
		            
			}
		});
        
	
}
			
			
	  
		
		
		@Override
		public void onActivityResult(int reqCode, int resultCode, Intent data) {
		  super.onActivityResult(reqCode, resultCode, data);

		  switch (reqCode) {
		    case (PICK_CONTACT) :
		      if (resultCode == Activity.RESULT_OK) {
		        Uri contactData = data.getData();
		        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
		        
		        
		        if (c.moveToFirst()) {
		          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		       
		          v_name.setText(name);
		          
		          //   String mail = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
		          String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)); 
		          
		          String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
		         
		          if ((Integer.parseInt(hasPhone)==1 )){ 
		          
		          Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
		          null, 
		          ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, 
		          null, null); 
		          while (phones.moveToNext()) { 
		        String  phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
		   
		        v_number.setText(phoneNumber);
		        
		        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
		                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
		 
		                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
		 
		                new String[]{contactId},
		                null);
		 
		        if (cursorPhone.moveToFirst()) {
		        String	Email= cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
		        
		     v_mail.setText(Email);
		        
		        }
		        
		        Cursor cursorPhone1 = getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
		                new String[]{ContactsContract.CommonDataKinds.StructuredPostal.DATA},
		 
		                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
		 
		                new String[]{contactId},
		                null);
		 
		        if (cursorPhone1.moveToFirst()) {
		        String	address= cursorPhone1.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));
		        
		        v_add.setText(address);
		        
		        }   
		         
		        
		          // TODO Whatever you want to do with the selected contact name.
		         
		        }
		      }
		          
		          
		      break;
		  }
		      }
		    
		    case PICK_NUMBER:
		    	 if (resultCode == Activity.RESULT_OK) {
			    	  try
			    	  {
			        Uri contactData = data.getData();
			        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
			        
			        
			        if (c.moveToFirst()) {
			          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			       
			         /* v_name.setText(name);*/
			          
			          //   String mail = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
			          String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)); 
			          
			          String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
			         
			          if ((Integer.parseInt(hasPhone)==1 )){ 
			          
			          Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
			          null, 
			          ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, 
			          null, null); 
			          while (phones.moveToNext()) { 
			        String  phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
			   
			        numbers.setText(phoneNumber);
			          }
			          }
			        }
			      }
			    	  catch(Exception e)
			    	  {
			    		  displayMessage(e.toString());
			    	  }
			      }
		    	 break;
		    	 
		    case PICK_MAIL:
		    	if (resultCode == Activity.RESULT_OK) {
			        Uri contactData = data.getData();
			        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
			        
			        
			        if (c.moveToFirst()) {
			          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			       
			         // v_name.setText(name);
			          
			          //   String mail = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
			          String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)); 
			          
			          String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
			         
			          if ((Integer.parseInt(hasPhone)==1 )){ 
			          
			          Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
			          null, 
			          ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, 
			          null, null); 
			          while (phones.moveToNext()) { 
			        String  phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
			   
			        //v_number.setText(phoneNumber);
			        
			        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
			                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
			 
			                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
			 
			                new String[]{contactId},
			                null);
			 
			        if (cursorPhone.moveToFirst()) {
			        String	Email= cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
			        if(Control.equals("To"))
			        {
			        	String tempTo=to.getText().toString();
			        	if(!tempTo.isEmpty())
			        	{
			        		to.setText(tempTo+","+Email);
			        	}
			        	else
			        		to.setText(Email);
			        }
			        else if(Control.equals("Cc"))
			        {
			        	String tempCc=cc.getText().toString();
			        	if(!tempCc.isEmpty())
			        	{
			        		cc.setText(tempCc+","+Email);
			        	}
			        	else
			        		cc.setText(Email);
			        }
			        else if(Control.equals("Bcc"))
			        {
			        	String tempBcc=bcc.getText().toString();
			        	if(!tempBcc.isEmpty())
			        	{
			        		bcc.setText(tempBcc+","+Email);
			        	}
			        	else
			        		bcc.setText(Email);
			        }
			     //v_mail.setText(Email);
			        
			        }
			          }
			          }
			        }
			      }
		    	break;
		    
		  }
		}
		
		
		
		
		private OnClickListener clicked() {
			// TODO Auto-generated method stub
			return null;
		}
        
       
			


		public class OnItemSelected implements OnItemSelectedListener {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*
				 * Toast.makeText(arg0.getContext(), "The country is
				 * "+arg0.getItemAtPosition(arg2).toString(),
				 * Toast.LENGTH_SHORT).show();
				 */

				// Drug Name
				try{
				network_name = mSpinner.getSelectedItem().toString();
				}
				catch(Exception e)
				{
					Toast toast = Toast.makeText(getApplicationContext(),
							"Unable to populate available Wi-Fi networks. Please press back button and again press write", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
					toast.show();
				}
               try{
               BT_Device_name=btSpinner.getSelectedItem().toString();
               
               index_bt_item=btSpinner.getSelectedItemPosition();
               Mac_address=SpinnerArrayBT_devices_mac.get(index_bt_item);
               }
               catch(Exception e)
               {
            	   Toast toast = Toast.makeText(getApplicationContext(),
       					"Spinner BT Error"+e.toString(), Toast.LENGTH_SHORT);
       			toast.show();
               }
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}
		
		
		
		
		private void initListener() {

			mWriteTagButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					network_name = mSpinner.getSelectedItem().toString();
					WifiManager wifi;

					String t;
					wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					List<ScanResult> results;
					results = wifi.getScanResults();
					String bssid = null;
					for (ScanResult result : results) {

						if ((network_name).equals(result.SSID)) {
							bssid = result.BSSID;
							break;
						}

					}

					String delimiter = ":";
					/*
					 * given string will be split by the argument delimiter
					 * provided.
					 */
					mac_add = bssid.split(delimiter);
					/* print substrings */

					net_length = network_name.length();

					password = mEditText1.getText().toString();
					pass_length = password.length();
					
					Toast toast = Toast.makeText(getApplicationContext(),
							"Keep tag near the phone", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
					toast.show();
					
					
					
					
					getSMS();
					getMail();
					getLocation();
					getTelephone();
					
					
					enableWriteMode();
				
					
				}

			
			});

		
		}
		
		
		@Override
		protected void onPause() {
			super.onPause();
			disableWriteMode();
		}
	
	   public  boolean varifyTag() {
			boolean test = false;
			
			
	 byte[] NdefSelectAppliFrame = new byte[] { (byte) 0x00, (byte) 0xA4,
					 (byte) 0x04, (byte) 0x00, (byte) 0x07,
					 (byte) 0xD2, (byte) 0x76, (byte) 0x00, (byte) 0x00,
					 (byte) 0x85, (byte) 0x01, (byte) 0x01};       
					
	 byte[] CCSelect = new byte[] {(byte) 0x00, (byte) 0xA4,(byte) 0x00, (byte) 0x0C, 
			 (byte) 0x02,(byte) 0xE1, (byte) 0x03 };
	 
      byte[] CCReadLength = new byte [] {(byte) 0x00, (byte) 0xB0,
		 (byte) 0x00, (byte) 0x00, (byte) 0x0F};
	 
	 
	 byte[] ndefSelectcmd = new byte [] {  (byte) 0x00, (byte) 0xA4,
				(byte) 0x00, (byte) 0x0C, (byte) 0x02,
				(byte) 0x00, (byte) 0x01 // Ndef File ID to select
				};
	 
	 
//	 int a = 491;
	 
	 byte[] ndefreadlengthcmd = new byte []{
			 (byte) 0x00, (byte) 0xB0,
			 (byte) 0x00, (byte) 0x00, (byte) 0x6E
				};
	 
	 byte[] readBinary = new byte [] {(byte) 0x00, (byte) 0xB0,
				(byte) 0x00, (byte) 0x00, (byte) 0x0F};
	 
	 
	 byte[] SYSSelect = new byte[] {(byte) 0x00, (byte) 0xA4,
			 (byte) 0x00, (byte) 0x0C, (byte) 0x02,
			 (byte) 0xE1, (byte) 0x01 };
	 
	 
	 
	 
	 byte[] response1 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
	 byte[] response2 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
	 byte[] response3 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
	 byte[] response4 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
	 byte[] response5 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
	 byte[] response6 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
	 byte[] response7 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
	 byte[] response8 = new byte[] { (byte) 0x01, (byte)0x00, (byte) 0x00 };
	 
	 try 
	 {
		 
		 
		 IsoDep isoDepCurrentTag= (IsoDep) IsoDep.get(ma.getCurrentTag());
		 
		 NfcA nfcaTag = (NfcA) NfcA.get(ma.getCurrentTag());
		 nfcaTag.close();
		 boolean a ;
		 a= isoDepCurrentTag.isConnected();
		 byte[] at=new byte [10];
		 //at=isoDepCurrentTag.getAtqa();
		 isoDepCurrentTag.connect();
	     int time;
	     time=isoDepCurrentTag.getTimeout();
	    
	
		 response1 = isoDepCurrentTag.transceive(NdefSelectAppliFrame);
		 response2 = isoDepCurrentTag.transceive(CCSelect);
		 response3 = isoDepCurrentTag.transceive(CCReadLength);
		 response4 = isoDepCurrentTag.transceive(SYSSelect);
		 response5 = isoDepCurrentTag.transceive(readBinary);
		 int ad = 10;
		 isoDepCurrentTag.close();
		 
	 }
		
	 
	 catch (Exception e)
	 {
		 
		 String exp = e.toString();
		 Toast toast = Toast.makeText(getApplicationContext(),
					"Tag not found in range. Press back button and re click Write button", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
			toast.show();
		 
		 
	 }
	    
		if (response5[8]==(byte)0x02)
		{
		   
			test = true;
		}
		
		else 
		{
			
			test = false;
			
		}
			
			return test;
		}
		

        
		/**
		 * Called when our blank tag is scanned executing the PendingIntent
		 */
		//@SuppressLint("InlinedApi")
		@Override
	    public void onNewIntent(Intent intent) {
			try {
			
			if(mInWriteMode) {
				mInWriteMode = false;
				
				// write to newly scanned tag
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);     

				ma.setCurrentTag(tagFromIntent);

	boolean cardCheck = false; 
				
				cardCheck= varifyTag();
				
				
				if (cardCheck)
				{
					
				writeTag(tag);
			
				}
				
				else if (!cardCheck)
					
				{
					
					Toast toast = Toast.makeText(getApplicationContext(),
							"Verification failed. Please use STMicroelectronics SR Tag to continue.", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
					toast.show();
					
					
				}
	}
			
			}
			
			catch (Exception e)
			{
				//Toast toast = Toast.makeText(getApplicationContext(),"Error function OnNewIntent"+e.toString() , Toast.LENGTH_SHORT);

				//toast.show();
				
			}
			
	    }
		
		/**
		 * Force this Activity to get NFC events first
		 */
	
		//@SuppressLint("NewApi")
		private void enableWriteMode() {
			mInWriteMode = true;
			
			
			try {
			// set up a PendingIntent to open the app when a tag is scanned
	        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
	            new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
	        IntentFilter[] filters = new IntentFilter[] { tagDetected };
	        
			mAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
			}
			
			catch(Exception e)
			{
				
				Toast toast = Toast.makeText(getApplicationContext(),
						"Problem capturing tag" + e.toString(), Toast.LENGTH_SHORT);
				toast.show();
				
			}
		}
		
		//@SuppressLint("NewApi")
		private void disableWriteMode() {
			mAdapter.disableForegroundDispatch(this);
		}
		
		
		/**
		 * Format a tag and write our NDEF message
		 */
		//@SuppressLint("NewApi")
		private boolean writeTag(Tag tag) {
			// record to launch Play Store if app is not installed
	
			
			NdefRecord appRecord = NdefRecord.createApplicationRecord("com.example.multi_ndef");
		
			
		try {	
		ENGLISH=Locale.ENGLISH ;
		encodeInUtf8 = true ;
		}
		
		catch (Exception e)
		{
			
			Toast toast = Toast.makeText(getApplicationContext(),
					"Locale Error "+ e.toString(), Toast.LENGTH_SHORT);
			toast.show();
		}
		
		try {
			
			
			
		textRecord = createTextRecord(getText(),ENGLISH, encodeInUtf8);
		
		
		
		}
		
		catch(Exception e)
		
		{
			
			Toast toast = Toast.makeText(getApplicationContext(),
					"Text Conversion error "+ e.toString(), Toast.LENGTH_SHORT);
			toast.show();
			
		}
		
		try {
		
	    uriRecord = NdefRecord.createUri(getUri());
		}
		
		
		catch (Exception e )
		{
			
			Toast toast = Toast.makeText(getApplicationContext(),
					"Uri Conversion error "+ e.toString(), Toast.LENGTH_SHORT);
			toast.show();
		}
		
		
		
		
		byte[] mimeBytes = MimeType.AppName.getBytes(Charset.forName("US-ASCII"));
		
		
		byte[] mimeBytesBT = MimeType.AppNameBT.getBytes(Charset.forName("US-ASCII"));
		
		
		byte [] v_data = VCard();
		
			// Here data is written
			byte[] payload = data(); // payload in hex
			
			
			//Comments by neha - 3 Jul 2014
			byte[] payloadBT = btData();
			
			  NdefRecord VcardRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/x-vcard".getBytes(), new byte[0], v_data);
			  
			  
			
			 
	       NdefRecord BTcardRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytesBT, 
	        										new byte[0], payloadBT);
			 
			 
	        NdefRecord cardRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, 
	        										new byte[0], payload);
	        
	        
	        NdefRecord SMSRecord = NdefRecord.createUri(SMSpayload); 
	        
	        NdefRecord MailRecord = NdefRecord.createUri(Mailpayload);
	        
	        NdefRecord TeleRecord = NdefRecord.createUri(Telepayload);
	        
	        NdefRecord LocationRecord = NdefRecord.createUri(Locationpayload);
	        
	        
	        NdefMessage message = new NdefMessage(new NdefRecord[] { cardRecord,textRecord,uriRecord,BTcardRecord,VcardRecord,SMSRecord,MailRecord,TeleRecord,LocationRecord,appRecord});
	      //  ringProgressDialog.dismiss();
	    
	        
	        
			try {
				// see if tag is already NDEF formatted
				Ndef ndef = Ndef.get(tag);
				if (ndef != null) {
					ndef.connect();

					if (!ndef.isWritable()) {
						displayMessage("Read-only tag.");
					
						return false;
					}
					
					// work out how much space we need for the data
					int size = message.toByteArray().length;
					if (ndef.getMaxSize() < size) {
						displayMessage("Tag doesn't have enough free space.");
					
						return false;
					}

					ndef.writeNdefMessage(message);
					displayMessage("Multiple NDEF Records written to tag successfully.");
					
					return true;
				} else {
					// attempt to format tag
					NdefFormatable format = NdefFormatable.get(tag);
					if (format != null) {
						try {
							format.connect();
							format.format(message);
							displayMessage("Tag written successfully!\nClose this app and scan tag.");
						
							return true;
						} catch (IOException e) {
							displayMessage("Unable to format tag to NDEF.");
						
							return false;
						}
					} else {
						displayMessage("Tag doesn't appear to support NDEF format.");
					
						return false;
					}
				}
			} catch (Exception e) {
				displayMessage("Failed to write tag");
				
			}

	        return false;
	    }
		
	
		
		public NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
		    byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
		    Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
		    byte[] textBytes = payload.getBytes(utfEncoding);
		    int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		    char status = (char) (utfBit + langBytes.length);
		    byte[] data = new byte[1 + langBytes.length + textBytes.length];
		    data[0] = (byte) status;
		    System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		    System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
		    NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
		    NdefRecord.RTD_TEXT, new byte[0], data);
		    return record;
		}
		
		
		private void displayMessage(String message) {
			
			status =true;
			Toast toast = Toast.makeText(getApplicationContext(),
					message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
			toast.show();
		}



		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(((Button)v).getId()==fetchto.getId())
			{
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_MAIL);
		        Control="To";    
		            
		            
			}
			
			else if(((Button)v).getId()==fetchcc.getId())
			{
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_MAIL);
				Control="Cc";
		            
		            
		            
			}
			
			else if(((Button)v).getId()==fetchbcc.getId())
			{
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_MAIL);
				Control="Bcc";    
		            
		            
			}
			
			
			else if(((Button)v).getId()==addMore.getId())
			{
				if(!numbers.getText().toString().isEmpty())
				{
				if(noOfContacts==1)
				{
					MultiContacts=numbers.getText().toString();
					noOfContacts++;
					numbers.setText("");
					recipients=MultiContacts;
					Recipients.setText(recipients);
				}
				else
				{
					MultiContacts+=";"+numbers.getText().toString();
					noOfContacts++;
					numbers.setText("");
					recipients=MultiContacts;
					Recipients.setText(recipients);
					
				}
				}
			}
			else if(((Button)v).getId()==location.getId())
			{
				
				
				gps = new GPSTracker(Write.this);                 // check if GPS enabled  
				if(gps.canGetLocation()){    
					latitudevalue= gps.getLatitude();        
					longitudevalue = gps.getLongitude();   
					latitude.setText(String.valueOf(latitudevalue));
					longitude.setText(String.valueOf(longitudevalue));
					// \n is for new line                 
					Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitudevalue + "\nLong: " + longitudevalue, Toast.LENGTH_LONG).show();     
					}else{      
						// can't get location           
						// GPS or Network is not enabled     
						// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();             
				}
			
			}
			
		}
		private String[] conversion(String toconvert) {
			byte[] hex = new byte[50];
			hex = toconvert.getBytes();
			String[] hexArray = new String[hex.length];
			for (int index = 0; index < hex.length; index++) {
				hexArray[index] = Integer.toHexString(hex[index]);

				// ri=Helper.ConvertStringToHexByte(hexArray[index]);
			}

			return hexArray;

		}
		
	
		public byte [] btData()
		
		{
			//Comments by neha 3 jul2014
			//int length = 17 + name_length;
				
			try {
			
			int btnamelen = BT_Device_name.length();
		     
			//6 bytes of MAC Address
			
			dataBT[0] = (byte) btnamelen;
			
			String value1=Mac_address.substring(0,2);
			dataBT[1] = (byte) Helper
					.ConvertStringToHexByte(value1);
			String value2=Mac_address.substring(3,5);
			dataBT[2] = (byte) Helper
					.ConvertStringToHexByte(value2);
			String value3=Mac_address.substring(6,8);
			dataBT[3] = (byte) Helper
					.ConvertStringToHexByte(value3);
			String value4=Mac_address.substring(9,11);
			dataBT[4] = (byte) Helper
					.ConvertStringToHexByte(value4);
			String value5=Mac_address.substring(12,14);
			dataBT[5] = (byte) Helper
					.ConvertStringToHexByte(value5);
			String value6=Mac_address.substring(15,17);
			dataBT[6] = (byte) Helper
					.ConvertStringToHexByte(value6);
			
			dataBT[7] = (byte) 0x04;
			dataBT[8] = (byte) 0x0D;
		    dataBT[9] = (byte) 0x40;
		    dataBT[10] = (byte)0x25 ;
		    dataBT[11] = (byte)0x00 ;
		    dataBT[12] = (byte)0x03;
		    dataBT[13] = (byte)0x03;
		    dataBT[14] = (byte)0x24;
		    dataBT[15] = (byte)0x11;
		    dataBT[16] = (byte)0x12;
		    dataBT[17] = (byte)0x09;
		    
		    
		    String[] bt_name = conversion(BT_Device_name);

			int start = 18;
			for (int j = 0; j < btnamelen; j++) {
				dataToWrite[start] = (byte) Helper
						.ConvertStringToHexByte(bt_name[j]);
				start++;
			}
		    
			int sizeBT;
			sizeBT=start+btnamelen;
			
	byte[] CardDataBT= new byte[sizeBT];
			
			
			for(int j=0;j<sizeBT;j++)
			{
				
				
				CardDataBT[j]=dataBT[j];
				
				
			}
			
			
			
			
			return CardDataBT;
			
			
			}
			
			
			catch(Exception e)
			{
				
				Toast toast = Toast.makeText(getApplicationContext(),
						"BT Ndef Message making error" + e.toString(), Toast.LENGTH_SHORT);
				toast.show();
				return null;
				
			}
		    
		  //  return null;
			
		}
		
		
		
		public byte[] data()
		{
		
			
			try {
			
			dataToWrite[0] = (byte) 0x10;
			dataToWrite[1] = (byte) 0x4A;
			dataToWrite[2] = (byte) 0x00;
			dataToWrite[3] = (byte) 0x01;
			dataToWrite[4] = (byte) 0x10;
			dataToWrite[5] = (byte) 0x10;
			dataToWrite[6] = (byte) 0x0E;
			
			int cred_length= 2+2+1+2+2+net_length+2+2+2+2+2+2+2+2+pass_length+2+2+6+2+2+3+1+1+1;
			
		//	dataToWrite[7] = (byte) 0x00;
			
			String len = Integer.toHexString(cred_length);
			dataToWrite[8] = (byte) Helper.ConvertStringToHexByte(len);
			
			dataToWrite[9] = (byte) 0x10;
			dataToWrite[10] = (byte) 0x26;
			dataToWrite[11] = (byte) 0x00;
			dataToWrite[12] = (byte) 0x01;
			dataToWrite[13] = (byte) 0x01;
			dataToWrite[14] = (byte) 0x10;
			dataToWrite[15] = (byte) 0x45;
			
			String ssid_len;

			String[] ssid = conversion(network_name);

			if (net_length < 16) {
				ssid_len = "0" + Integer.toHexString(net_length);
			} else {
				ssid_len = Integer.toHexString(net_length);
			}
			dataToWrite[17] = (byte) Helper
					.ConvertStringToHexByte(ssid_len);

			// byte s=ConvertintToHexByte(ssid_len);

			int start = 18;
			for (int j = 0; j < net_length; j++) {
				dataToWrite[start] = (byte) Helper
						.ConvertStringToHexByte(ssid[j]);
				start++;
			}
			
			
			dataToWrite[start]   = (byte) 0x10;
			dataToWrite[start+1] = (byte) 0x03;
			dataToWrite[start+2] = (byte) 0x00;
			dataToWrite[start+3] = (byte) 0x02;
			dataToWrite[start+4] = (byte) 0x00;
			dataToWrite[start+5] = (byte) 0x20;
			dataToWrite[start+6] = (byte) 0x10;
			dataToWrite[start+7] = (byte) 0x0F;
			dataToWrite[start+8] = (byte) 0x00;
			dataToWrite[start+9] = (byte) 0x02;
			dataToWrite[start+10] = (byte) 0x00;
			dataToWrite[start+11] = (byte) 0x08;
			dataToWrite[start+12] = (byte) 0x10;
			dataToWrite[start+13] = (byte) 0x27;
			
			// key length

			String pass_len;
			String[] passwd = conversion(password);
			if (pass_length < 16) {
				pass_len = "0" + Integer.toHexString(pass_length);
			} else {
				pass_len = Integer.toHexString(pass_length);
			}
			dataToWrite[start + 15] = (byte) Helper
					.ConvertStringToHexByte(pass_len);

			// key
			int keystart = start + 16;
			for (int j = 0; j < pass_length; j++) {
				dataToWrite[keystart] = (byte) Helper
						.ConvertStringToHexByte(passwd[j]);
				keystart++;
			}
			
			dataToWrite[keystart] = (byte) 0x10;
			dataToWrite[keystart + 1] = (byte) 0x20;
			dataToWrite[keystart + 2] = (byte) 0x00;
			dataToWrite[keystart + 3] = (byte) 0x06;
			
			
			// MAC Address
			dataToWrite[keystart + 4] = (byte) Helper
					.ConvertStringToHexByte(mac_add[0]);
			dataToWrite[keystart + 5] = (byte) Helper
					.ConvertStringToHexByte(mac_add[1]);
			dataToWrite[keystart + 6] = (byte) Helper
					.ConvertStringToHexByte(mac_add[2]);
			dataToWrite[keystart + 7] = (byte) Helper
					.ConvertStringToHexByte(mac_add[3]);
			dataToWrite[keystart + 8] = (byte) Helper
					.ConvertStringToHexByte(mac_add[4]);
			dataToWrite[keystart + 9] = (byte) Helper
					.ConvertStringToHexByte(mac_add[5]);
			
			dataToWrite[keystart + 10] = (byte) 0x10;
			dataToWrite[keystart + 11] = (byte) 0x49;
			dataToWrite[keystart + 12] = (byte) 0x00;
			dataToWrite[keystart + 13] = (byte) 0x06;
			dataToWrite[keystart + 14] = (byte) 0x00;
			dataToWrite[keystart + 15] = (byte) 0x37;
			dataToWrite[keystart + 16] = (byte) 0x2A;
			dataToWrite[keystart + 17] = (byte) 0x02;
			dataToWrite[keystart + 18] = (byte) 0x01;
			dataToWrite[keystart + 19] = (byte) 0x01;
			dataToWrite[keystart + 20] = (byte) 0x10;
			dataToWrite[keystart + 21] = (byte) 0x49;
			dataToWrite[keystart + 22] = (byte) 0x00;
			dataToWrite[keystart + 23] = (byte) 0x06;
			dataToWrite[keystart + 24] = (byte) 0x00;
			dataToWrite[keystart + 25] = (byte) 0x37;
			dataToWrite[keystart + 26] = (byte) 0x2A;
			dataToWrite[keystart + 27] = (byte) 0x00;
			dataToWrite[keystart + 28] = (byte) 0x01;
			dataToWrite[keystart + 29] = (byte) 0x20;
			
			int size;
			size= keystart+30;
			
			byte[] CardData= new byte[size];
			
			
			for(int j=0;j<size;j++)
			{
				
				
				CardData[j]=dataToWrite[j];
				
				
			}
			
			return CardData;
			
			}
			
			catch (Exception e)
			
			{
				Toast toast = Toast.makeText(getApplicationContext(),
						"Wi-Fi Ndef Message making error" + e.toString(), Toast.LENGTH_SHORT);
				toast.show();
				return null;
				
			}
			
			
		
			
			
		}
		
		
		private void getSMS()
		{
			String Snumber;
			if(noOfContacts==1)
			{
					Snumber=numbers.getText().toString();
			}
			else
			{
				if(!numbers.getText().toString().isEmpty())
				{
					Snumber=MultiContacts+";"+numbers.getText().toString();
				}
				else
				{
					Snumber=MultiContacts;
				}
					
			}
			String Smessage=message.getText().toString();
			SMSpayload="sms:"+Snumber+"?body="+Smessage;
			
		}
		
		private String getUri()
		{
			
			String u = mUri.getText().toString();
			return u;
			
		}
		
		private void getTelephone()
		{
			String Snumber=telephone.getText().toString();
			
			
			Telepayload="tel:"+Snumber;
		}
		
		
		private void getLocation()
		{

			String Slatitude=latitude.getText().toString();
			String Slongitude=longitude.getText().toString();
		
		
		    Locationpayload="geo:"+Slatitude+","+Slongitude;
		}
		private void getMail()
		{
			Sto=to.getText().toString();
			Scc=cc.getText().toString();
			Sbcc=bcc.getText().toString();
			Ssubject=subject.getText().toString();
			String Smessage=body.getText().toString();
			Mailpayload="mailto:"+Sto;
			if(!Ssubject.isEmpty())
			{
				Mailpayload+="?subject="+Ssubject;
			}
			if(!Scc.isEmpty())
			{
				Mailpayload+="&cc="+Scc;
			}
			if(!Sbcc.isEmpty())
			{
				Mailpayload+="&bcc="+Sbcc;
			}
			if(!Smessage.isEmpty())
			{
				Mailpayload+="&body="+Smessage;
			}
		}
		
		
		private String getText()
		{
			String a= mTxt.getText().toString();
			return a;	
			
		}
		
		
		 public byte [] VCard()
		    {
		    	
		    	try {
		    			
		    	String vcardString = "";
		    	String _Vcard ="BEGIN:VCARD\nVERSION:2.1\n";
		    	
		    		vcardString =  vcardString + "N:"+v_name.getText().toString()+"\n";
		
		    	
		    		vcardString =  vcardString + "EMAIL:"+v_mail.getText().toString()+"\n";;
		    	
		    	
		    	
		    		vcardString =  vcardString + "ADR:"+v_add.getText().toString()+"\n";
		    	
		    		vcardString = vcardString + "TEL:"+v_number.getText().toString()+"\n";
		    	
		    	
		    	vcardString = _Vcard + vcardString + "END:VCARD";
		    	
		    
		    	
		    	
			byte[] card_payload = vcardString.getBytes(Charset.forName("US-ASCII"));
			
			return card_payload;
		    	}
		    	
		    	
		    	catch (Exception e)
		    	{
		    		Toast toast = Toast.makeText(getApplicationContext(),
							"vCard formation error "+ e.toString(), Toast.LENGTH_SHORT);
					toast.show();
		    		
					return null;
		    	}
		    	
			
		    }
		
}		
	
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/	
