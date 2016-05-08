/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : BasicWriteSR.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : BasicWriteSR
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
import java.util.Set;

//import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
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

public class BasicWriteSR extends Activity implements OnClickListener {
	

	
        String network_name;
        String[] mac_add;
        String password;
		private NfcAdapter mAdapter;
		private boolean mInWriteMode;
		private Button mWriteTagButton;
		private TextView mTextView1;
		private TextView mTextView2;
		
		private EditText mEditText1;
	
		private Spinner mSpinner;
		int pass_length;
		int net_length;
		Handler updateBarHandler;

		
		private byte[] dataToWrite = new byte[200];
	
		boolean status= false;
				
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
		        
	        // grab our NFC Adapter
	        mAdapter = NfcAdapter.getDefaultAdapter(this);
	   
	        // button that starts the tag-write procedure
	        mWriteTagButton = (Button)findViewById(R.id.button1);
	        mWriteTagButton.setOnClickListener(this);
	        
	        // TextView that we'll use to output messages to screen
	        mTextView1 = (TextView)findViewById(R.id.textView1);
	        mTextView2 = (TextView)findViewById(R.id.textView2);
	        mEditText1 = (EditText)findViewById(R.id.editText1);
	        mSpinner = (Spinner)findViewById(R.id.spinner);
	        
	        updateBarHandler = new Handler();
	       
	       
	        mSpinner.setOnItemSelectedListener(new OnItemSelected());
			WifiManager wifi;
			try {
				wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

				// Get WiFi status
				WifiInfo info = wifi.getConnectionInfo();
				// List available networks
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
				List<String> SpinnerArray = new ArrayList<String>();
				// SpinnerArray.addAll(set);
				for (int i1 = 1; i1 < length; i1++) {
					SpinnerArray.add(uniques[i1]);
				}

				// Create an ArrayAdapter using the string array and a default
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
	  }   
		
		public class OnItemSelected implements OnItemSelectedListener {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				network_name = mSpinner.getSelectedItem().toString();

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
			
					mac_add = bssid.split(delimiter);
					/* print substrings */

					net_length = network_name.length();

					password = mEditText1.getText().toString();
					pass_length = password.length();
					
					Toast toast = Toast.makeText(getApplicationContext(),
							"Keep tag near the phone", Toast.LENGTH_SHORT);
					toast.show();
					
					enableWriteMode();
					
				}
			});

		}
		

		@Override
		protected void onPause() {
			super.onPause();
			disableWriteMode();
		}
		
		/**
		 * Called when our blank tag is scanned executing the PendingIntent
		 */
	
		@Override
	    public void onNewIntent(Intent intent) {
			if(mInWriteMode) {
				mInWriteMode = false;
				
				// write to newly scanned tag
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				writeTag(tag);
			}
	    }
		
		/**
		 * Force this Activity to get NFC events first
		 */
	
		
		private void enableWriteMode() {
			mInWriteMode = true;
			
			// set up a PendingIntent to open the app when a tag is scanned
	        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
	            new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
	        IntentFilter[] filters = new IntentFilter[] { tagDetected };
	        
			mAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
		}
		
		//@SuppressLint("NewApi")
		private void disableWriteMode() {
			mAdapter.disableForegroundDispatch(this);
		}
		
		
		/**
		 * Format a tag and write our NDEF message
		 */
	
		private boolean writeTag(Tag tag) {
			// record to launch Play Store if app is not installed
		NdefRecord appRecord = NdefRecord.createApplicationRecord("com.example.wifisr_lr");
			
		byte[] mimeBytes = MimeType.AppName.getBytes(Charset.forName("US-ASCII"));
			
			// Here data is written
			byte[] payload = data(); // payload in hex
		
	        NdefRecord cardRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, 
	        										new byte[0], payload);
	        NdefMessage message = new NdefMessage(new NdefRecord[] { cardRecord, appRecord});
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
					displayMessage("Tag written successfully.");
					
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
		
	
		
	
		
		private void displayMessage(String message) {
			
			status =true;
			Toast toast = Toast.makeText(getApplicationContext(),
					message, Toast.LENGTH_SHORT);
			toast.show();
		}



		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		private String[] conversion(String toconvert) {
			byte[] hex = new byte[50];
			hex = toconvert.getBytes();
			String[] hexArray = new String[hex.length];
			for (int index = 0; index < hex.length; index++) {
				hexArray[index] = Integer.toHexString(hex[index]);
			}

			return hexArray;

		}
		
		public byte[] data()
		{
			
			
			dataToWrite[0] = (byte) 0x10;
			dataToWrite[1] = (byte) 0x4A;
			dataToWrite[2] = (byte) 0x00;
			dataToWrite[3] = (byte) 0x01;
			dataToWrite[4] = (byte) 0x10;
			dataToWrite[5] = (byte) 0x10;
			dataToWrite[6] = (byte) 0x0E;
			
			int cred_length= 2+2+1+2+2+net_length+2+2+2+2+2+2+2+2+pass_length+2+2+6+2+2+3+1+1+1;
			
			
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
		
		
}		
	
	


/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/