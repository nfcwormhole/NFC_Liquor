/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : CardActivity.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : CardActivity
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

import java.io.UnsupportedEncodingException;
import java.util.List;


//import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 


public class CardActivity extends Activity implements OnClickListener{
	
	boolean flag_paired = false;
	boolean flag = false;
	boolean connected= false;
	boolean test;
	int a=0;
	private TextView mUriView;
	private TextView mTextView1;
	ProgressDialog theReadProgressBar;
    Button buttonSTLink;
	static public boolean m_bConnectionCheck = true;
	public byte[] message =null;
	String ssid_info="";
	ReadDataFromWifi theObject;
	String ssid_test;
	String exp;
	String  networkname;
	TextView SSID;
	CNFCInterface dataDevice = (CNFCInterface) getApplication();
    Button connect;
	//@SuppressLint("NewApi")
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_activity);
		
		
		
      
        mTextView1 = (TextView)findViewById(R.id.text_view);
        mUriView = (TextView)findViewById(R.id.textView3);
        
        SSID = (TextView) findViewById(R.id.textView6);
        connect = (Button) findViewById(R.id.button1);
     connect.setOnClickListener(this);
        // see if app was started from a tag
        
        
        Intent intent = getIntent();
        if(intent.getType() != null && intent.getType().equals(MimeType.AppName)) {
        	Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            NdefRecord cardRecord = msg.getRecords()[0];
            NdefRecord cardRecord2 = msg.getRecords()[1];
            NdefRecord cardRecord3 = msg.getRecords()[2];
            
            String rec2 =  new String(cardRecord2.getPayload());
            String rec3 = new String (cardRecord3.getPayload());
          
            String t = rec2.substring(3);
            String u = "http://www."+ rec3.substring(1);
          
            mTextView1.setText(t);
            mUriView.setText(u);
            
            message = cardRecord.getPayload();
         
            int namelength = message[17];
			byte[] name = new byte[namelength];
			System.arraycopy(message,18,name, 0, namelength);
			
			try {
			  networkname = new String(name, "US-ASCII");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SSID.setText(networkname);
    		
			 connect.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
				
						 theObject = new ReadDataFromWifi();
							theObject.execute();
						
						
					}
				});
            
     }
    }

	
void saveWepConfig(String networkname, String password) {
		
	
		WifiConfiguration conf = new WifiConfiguration();
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration i : list) {
			if (i.SSID != null && i.SSID.equals("\"" + networkname + "\"")) {

				wifiManager.disconnect();
				wifiManager.removeNetwork(i.networkId);

				break;
			}
		}

		conf.SSID = "\"" + networkname + "\"";
		
		//conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	
		conf.preSharedKey = "\"" + password + "\"";
		
		wifiManager.addNetwork(conf);

		List<WifiConfiguration> list1 = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration i : list1) {
			if (i.SSID != null && i.SSID.equals("\"" + networkname + "\"")) {
				wifiManager.disconnect();
				
				wifiManager.enableNetwork(i.networkId, true);

				break;
			}
		}

	}




public class ReadDataFromWifi extends AsyncTask<Void, Void, Void> {
	


	@Override
	protected void onPreExecute() {
	   
		theReadProgressBar = ProgressDialog.show(CardActivity.this,
				"Configuring Wi Fi Connection",
				"Now you can remove the phone from tag.", false);

	}
	

	@Override
	protected Void doInBackground(Void... params) {
		try {

			
    
	            
	            int namelength = message[17];
				byte[] name = new byte[namelength];
				System.arraycopy(message,18,name, 0, namelength);
				
				String  networkname = new String(name, "US-ASCII");
				
				int passlength = message[17+namelength+16];
				byte[] pass = new byte [passlength];
				
				System.arraycopy(message,17+namelength+16+1,pass, 0, passlength);
				
				String  password = new String(pass, "US-ASCII");
				
				WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	    		boolean check;
	    		check= wifiManager.isWifiEnabled();
	    		
	    		WifiInfo wifiinfo=wifiManager.getConnectionInfo();
	    		
	    		ssid_info=wifiinfo.getSSID();
	    		String ankit =ssid_info;
	    		
	    		if(ssid_info!=null && !ssid_info.isEmpty())
	    		{
		    		if(ssid_info.charAt(0)=='"')
		    		{
				    ssid_test=ssid_info.substring(1,ssid_info.length()-1);
				    
		    		}
		    		else
		    		{
		    			ssid_test= ssid_info;
		    		}
	    		
	    		}
	    		else 
	    		{
	    			
	    			ssid_test=null;
	    		}
	    		//connected / disconnected 
	    		
	    		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        		 test =networkname.equals(ssid_test);
	    	 	if (mWifi.isConnected()) 
	    		{
	    		 
	    			if (test)
						
					{
						connected= true;
						flag = true;
						
					}
	    			
	    			else
	    			{
	    				saveWepConfig(networkname,password);
	    				
	    				
						Thread.sleep(4000);
	    				if (isOnline())
	    				{
	    				flag = true ;
	    				}
    				
	    			}
	    			
	    		}
	    		
	    		else if (!mWifi.isConnected())
	    			
	    		{
	    			
	    			if(!check)
		    		{
		    		wifiManager.setWifiEnabled(true);
		    	
		    	    Thread.sleep(3000);
		    		}
					
					saveWepConfig(networkname,password);
					Thread.sleep(4000);
    				if (isOnline())
    				{
    				flag = true ;
    				}
	    			
	    		}
	     		
		}
	            	

	  
		catch (Exception e) {
			exp = e.toString();

		}

		return null;
	}
		
		
		
		@Override
		protected void onPostExecute(Void result) {

			if(connected==true && flag==false){
				
				Toast toast = Toast.makeText(getApplicationContext(),
						"Disonnected", Toast.LENGTH_SHORT);
				toast.show();
		
				theReadProgressBar.dismiss();
				}
			
			if(connected==false&&flag==false){
			
				Toast toast = Toast.makeText(getApplicationContext(),
						"Not Connected", Toast.LENGTH_SHORT);
				toast.show();
			theReadProgressBar.dismiss();
			}
			if (flag==true){
				
				Toast toast = Toast.makeText(getApplicationContext(),
						"Connected", Toast.LENGTH_SHORT);
				toast.show();
		
				theReadProgressBar.dismiss();
			}

		}
   }


public boolean isOnline() {

	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo ni = cm.getActiveNetworkInfo();
	NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	boolean result = false;
	if (ni != null) {
		if (mWifi.isConnected()) {
			result = true;
		}
	}

	return result;

}


@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
}
	
}


/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/

	
