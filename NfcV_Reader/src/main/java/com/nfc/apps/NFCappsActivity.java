// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.

package com.nfc.apps;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.pm.PackageManager;
//import android.util.Log;

public class NFCappsActivity extends Activity 
{
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	
	Button btnwww;
	
	private ImageView imgScan;
	private int drawableImageID;
	private Timer rollImage;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
     // Check for available NFC Adapter
    	PackageManager pm = getPackageManager();
    	if(!pm.hasSystemFeature(PackageManager.FEATURE_NFC))
    	{
    		setContentView(R.layout.main_nfc_hs);	        
	        
	        imgScan = (ImageView) findViewById(R.id.imgViewScanHS);
	        btnwww= (Button) this.findViewById(R.id.btnwwwHS);
	        drawableImageID = R.drawable.main_nfc_hs_1;
	        rollImage = new Timer();
	        rollImage.schedule(new TimerTask() 
	        {
				@Override
				public void run() 
				{
					TimerMethod_nfc_not_available();
				}
			}, 0, 1000);
			
	        this.btnwww.setOnClickListener(new View.OnClickListener() {			
				public void onClick(View v) {			
					
					String url = "http://www.st.com/memories";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			});
    	}
    	else
    	{
    		
	        mAdapter = NfcAdapter.getDefaultAdapter(this);
	        if (mAdapter.isEnabled())
	        {
		        setContentView(R.layout.main);
		        
		        mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		        mFilters = new IntentFilter[] {ndef,};
		        mTechLists = new String[][] { new String[] { android.nfc.tech.NfcV.class.getName() } };
		        
		        imgScan = (ImageView) findViewById(R.id.imgViewScan);
		        btnwww= (Button) this.findViewById(R.id.btnwww);
		        drawableImageID = R.drawable.wait1;
		        rollImage = new Timer();
		        rollImage.schedule(new TimerTask() 
		        {
					@Override
					public void run() 
					{
						TimerMethod_nfc();
					}
				}, 0, 500);
				
		        this.btnwww.setOnClickListener(new View.OnClickListener() {			
					public void onClick(View v) {			
						
						String url = "http://www.st.com/memories";
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						startActivity(i);
					}
				});
    		}
	        else
	        {
	        	setContentView(R.layout.main_nfc_hs);		        		        	        	
		        
		        imgScan = (ImageView) findViewById(R.id.imgViewScanHS);
		        btnwww= (Button) this.findViewById(R.id.btnwwwHS);
		        drawableImageID = R.drawable.main_nfc_hs_1;
		        rollImage = new Timer();
		        rollImage.schedule(new TimerTask() 
		        {
					@Override
					public void run() 
					{
						TimerMethod_nfc_disabled();
					}
				}, 0, 1000);
				
		        this.btnwww.setOnClickListener(new View.OnClickListener() {			
					public void onClick(View v) {			
						
						String url = "http://www.st.com/memories";
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						startActivity(i);
					}
				});
	        }
    	}
    }
    
    @Override
    protected void onNewIntent(Intent intent) 
    {
    	// TODO Auto-generated method stub
    	super.onNewIntent(intent);
    	String action = intent.getAction();
    	if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))    	
    	{
	    	Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	
	    	DataDevice dataDevice = (DataDevice)getApplication();
	    	dataDevice.setCurrentTag(tagFromIntent);

			byte[] GetSystemInfoAnswer = NFCCommand.SendGetSystemInfoCommandCustom(tagFromIntent,(DataDevice)getApplication());

			if(DecodeGetSystemInfoResponse(GetSystemInfoAnswer))
	    	{
	    		Intent intentScan = new Intent(this, Scan.class);
	        	startActivity(intentScan);
	    	}
			else
			{
				return;
			}
    	}
    }
    
    @Override
    protected void onResume() 
    {
    	// TODO Auto-generated method stub
    	super.onResume();
    	//Used for DEBUG : Log.v("NFCappsActivity.java", "ON RESUME NFC APPS ACTIVITY");
    	mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    	mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }
        
    @Override
    protected void onPause() 
    {
    	// TODO Auto-generated method stub
    	//Used for DEBUG : Log.v("NFCappsActivity.java", "ON PAUSE NFC APPS ACTIVITY");
    	super.onPause();
    	mAdapter.disableForegroundDispatch(this);
    	return;
    }
        
    private void TimerMethod_nfc()
	{
		//This method is called directly by the timer
		//and runs in the same thread as the timer.

		//We call the method that will work with the UI
		//through the runOnUiThread method.
		this.runOnUiThread(Timer_Tick_nfc);
	}
    
    private Runnable Timer_Tick_nfc = new Runnable() {
		@Override
		public void run() {
			if (drawableImageID==R.drawable.wait1)
			{
				drawableImageID = R.drawable.wait2;
			} else if (drawableImageID==R.drawable.wait2)
			{			
				drawableImageID = R.drawable.wait3;
			} else if (drawableImageID==R.drawable.wait3)
			{				
				drawableImageID = R.drawable.wait4;
			} else if (drawableImageID==R.drawable.wait4)
			{
				drawableImageID = R.drawable.wait1;				
			}			
			imgScan.setImageDrawable(getResources().getDrawable(drawableImageID));
		}
	};
	
	private void TimerMethod_nfc_not_available()
	{
		//This method is called directly by the timer
		//and runs in the same thread as the timer.

		//We call the method that will work with the UI
		//through the runOnUiThread method.
		this.runOnUiThread(Timer_Tick_nfc_not_available);
	}
    
    private Runnable Timer_Tick_nfc_not_available = new Runnable() {
		@Override
		public void run() {
			if (drawableImageID==R.drawable.main_nfc_hs_1)
			{
				drawableImageID = R.drawable.main_nfc_not_available_2;
			} else if (drawableImageID==R.drawable.main_nfc_not_available_2)
			{			
				drawableImageID = R.drawable.main_nfc_hs_3;
			} else if (drawableImageID==R.drawable.main_nfc_hs_3)
			{				
				drawableImageID = R.drawable.main_nfc_not_available_4;
			} else if (drawableImageID==R.drawable.main_nfc_not_available_4)
			{
				drawableImageID = R.drawable.main_nfc_hs_1;				
			}			
			imgScan.setImageDrawable(getResources().getDrawable(drawableImageID));
		}
	};
	
	private void TimerMethod_nfc_disabled()
	{
		//This method is called directly by the timer
		//and runs in the same thread as the timer.

		//We call the method that will work with the UI
		//through the runOnUiThread method.
		this.runOnUiThread(Timer_Tick_nfc_disabled);
	}
    
    private Runnable Timer_Tick_nfc_disabled = new Runnable() {
		@Override
		public void run() {
			if (drawableImageID==R.drawable.main_nfc_hs_1)
			{
				drawableImageID = R.drawable.main_nfc_disabled_2;
			} else if (drawableImageID==R.drawable.main_nfc_disabled_2)
			{			
				drawableImageID = R.drawable.main_nfc_hs_3;
			} else if (drawableImageID==R.drawable.main_nfc_hs_3)
			{				
				drawableImageID = R.drawable.main_nfc_disabled_4;
			} else if (drawableImageID==R.drawable.main_nfc_disabled_4)
			{
				drawableImageID = R.drawable.main_nfc_hs_1;				
			}			
			imgScan.setImageDrawable(getResources().getDrawable(drawableImageID));
		}
	};

	

	
	//***********************************************************************/
	 //* the function Decode the tag answer for the GetSystemInfo command
	 //* the function fills the values (dsfid / afi / memory size / icRef /..) 
	 //* in the myApplication class. return true if everything is ok.
	 //***********************************************************************/
	 public boolean DecodeGetSystemInfoResponse (byte[] GetSystemInfoResponse)
	 {
		 DataDevice ma = (DataDevice)getApplication();
		 //if the tag has returned a good response
		 if(GetSystemInfoResponse[0] == (byte) 0x00 && GetSystemInfoResponse.length >= 12)
		 { 
			 //DataDevice ma = (DataDevice)getApplication();
			 String uidToString = "";
			 byte[] uid = new byte[8];
			 // change uid format from byteArray to a String
			 for (int i = 1; i <= 8; i++) 
			 {
				 uid[i - 1] = GetSystemInfoResponse[10 - i];
				 uidToString += Helper.ConvertHexByteToString(uid[i - 1]);
			 }			 

			 //***** TECHNO ******
			 ma.setUid(uidToString);
			 if(uid[0] == (byte) 0xE0)
			 		 ma.setTechno("ISO 15693");
			 else if (uid[0] == (byte) 0xD0)
			 	 ma.setTechno("ISO 14443");
			 else
			 	 ma.setTechno("Unknown techno");			 
			 			
			 //***** MANUFACTURER ****
			 if(uid[1]== (byte) 0x02)
			 	 ma.setManufacturer("STMicroelectronics");
			 else if(uid[1]== (byte) 0x04)
			 	 ma.setManufacturer("NXP");
			 else if(uid[1]== (byte) 0x07)
			 	 ma.setManufacturer("Texas Instruments");
			 else if (uid[1] == (byte) 0x01) //MOTOROLA (updated 20140228)
				 ma.setManufacturer("Motorola");
			 else if (uid[1] == (byte) 0x03) //HITASHI (updated 20140228)
				 ma.setManufacturer("Hitachi");
			 else if (uid[1] == (byte) 0x04) //NXP SEMICONDUCTORS
				 ma.setManufacturer("NXP");
			 else if (uid[1] == (byte) 0x05) //INFINEON TECHNOLOGIES (updated 20140228)
				 ma.setManufacturer("Infineon");
			 else if (uid[1] == (byte) 0x06) //CYLINC (updated 20140228)
				 ma.setManufacturer("Cylinc");
			 else if (uid[1] == (byte) 0x07) //TEXAS INSTRUMENTS TAG-IT
				 ma.setManufacturer("Texas Instruments");
			 else if (uid[1] == (byte) 0x08) //FUJITSU LIMITED (updated 20140228)
				 ma.setManufacturer("Fujitsu");
			 else if (uid[1] == (byte) 0x09) //MATSUSHITA ELECTRIC INDUSTRIAL (updated 20140228)
				 ma.setManufacturer("Matsushita");
			 else if (uid[1] == (byte) 0x0A) //NEC (updated 20140228)
				 ma.setManufacturer("NEC");
			 else if (uid[1] == (byte) 0x0B) //OKI ELECTRIC (updated 20140228)
				 ma.setManufacturer("Oki");
			 else if (uid[1] == (byte) 0x0C) //TOSHIBA (updated 20140228)
				 ma.setManufacturer("Toshiba");
			 else if (uid[1] == (byte) 0x0D) //MITSUBISHI ELECTRIC (updated 20140228)
				 ma.setManufacturer("Mitsubishi");
			 else if (uid[1] == (byte) 0x0E) //SAMSUNG ELECTRONICS (updated 20140228)
				 ma.setManufacturer("Samsung");
			 else if (uid[1] == (byte) 0x0F) //HUYNDAI ELECTRONICS (updated 20140228)
				 ma.setManufacturer("Hyundai");
			 else if (uid[1] == (byte) 0x10) //LG SEMICONDUCTORS (updated 20140228)
				 ma.setManufacturer("LG");	 
			 else
			 	 ma.setManufacturer("Unknown manufacturer");						 			
			 
			 if(uid[1]== (byte) 0x02)
			 {
					 //**** PRODUCT NAME *****
					 if(uid[2] >= (byte) 0x04 && uid[2] <= (byte) 0x07)
					 {
					 	 ma.setProductName("LRI512");
					 	 ma.setMultipleReadSupported(false);
					 	 ma.setMemoryExceed2048bytesSize(false);
					 }
					 else if(uid[2] >= (byte) 0x14 && uid[2] <= (byte) 0x17)
					 {
					 	 ma.setProductName("LRI64");
					 	 ma.setMultipleReadSupported(false);
					 	 ma.setMemoryExceed2048bytesSize(false);
					 }
					 else if(uid[2] >= (byte) 0x20 && uid[2] <= (byte) 0x23)
					 {
					 	 ma.setProductName("LRI2K");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(false);
					 }
					 else if(uid[2] >= (byte) 0x28 && uid[2] <= (byte) 0x2B)
					 {
					 	 ma.setProductName("LRIS2K");
					 	 ma.setMultipleReadSupported(false);	
					 	 ma.setMemoryExceed2048bytesSize(false);
					 }
					 else if(uid[2] >= (byte) 0x2C && uid[2] <= (byte) 0x2F)
					 {
					 	 ma.setProductName("M24LR64");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 }
					 else if(uid[2] >= (byte) 0x40 && uid[2] <= (byte) 0x43)
					 {
					 	 ma.setProductName("LRI1K");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(false);
					 }
					 else if(uid[2] >= (byte) 0x44 && uid[2] <= (byte) 0x47)
					 {
					 	 ma.setProductName("LRIS64K");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 }
					 else if(uid[2] >= (byte) 0x48 && uid[2] <= (byte) 0x4B)
					 {
					 	 ma.setProductName("M24LR01E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(false);
					 }
					 else if(uid[2] >= (byte) 0x4C && uid[2] <= (byte) 0x4F)
					 {
					 	 ma.setProductName("M24LR16E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 	 if(ma.isBasedOnTwoBytesAddress() == false)
						 	return false;
					 }
					 else if(uid[2] >= (byte) 0x50 && uid[2] <= (byte) 0x53)
					 {
					 	 ma.setProductName("M24LR02E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(false);
					 }
					 else if(uid[2] >= (byte) 0x54 && uid[2] <= (byte) 0x57)
					 {
					 	 ma.setProductName("M24LR32E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 	 if(ma.isBasedOnTwoBytesAddress() == false)
						 	return false;
					 }
					 else if(uid[2] >= (byte) 0x58 && uid[2] <= (byte) 0x5B)
					 {
						 ma.setProductName("M24LR04E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 }
					 else if(uid[2] >= (byte) 0x5C && uid[2] <= (byte) 0x5F)
					 {
					 	 ma.setProductName("M24LR64E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 	 if(ma.isBasedOnTwoBytesAddress() == false)
						 	return false;
					 }
					 else if(uid[2] >= (byte) 0x60 && uid[2] <= (byte) 0x63)
					 {
					 	 ma.setProductName("M24LR08E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 }
					 else if(uid[2] >= (byte) 0x64 && uid[2] <= (byte) 0x67)
					 {
					 	 ma.setProductName("M24LR128E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 	 if(ma.isBasedOnTwoBytesAddress() == false)
						 	return false;
					 }
					 else if(uid[2] >= (byte) 0x6C && uid[2] <= (byte) 0x6F)
					 {
					 	 ma.setProductName("M24LR256E");
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 	 if(ma.isBasedOnTwoBytesAddress() == false)
						 	return false;
					 }
					 else if(uid[2] >= (byte) 0xF8 && uid[2] <= (byte) 0xFB)
					 {
					 	 ma.setProductName("detected product");
					 	 ma.setBasedOnTwoBytesAddress(true);
					 	 ma.setMultipleReadSupported(true);
					 	 ma.setMemoryExceed2048bytesSize(true);
					 }	 
					 else
					 {
					 	 ma.setProductName("Unknown product");
					 	 ma.setBasedOnTwoBytesAddress(false);
					 	 ma.setMultipleReadSupported(false);
					 	 ma.setMemoryExceed2048bytesSize(false);
					 }
					 
					 //*** DSFID ***
					 ma.setDsfid(Helper.ConvertHexByteToString(GetSystemInfoResponse[10]));
					 
					//*** AFI ***
					 ma.setAfi(Helper.ConvertHexByteToString(GetSystemInfoResponse[11]));			 
					 
					//*** MEMORY SIZE ***
					 if(ma.isBasedOnTwoBytesAddress())
					 {
						 String temp = new String();
						 temp += Helper.ConvertHexByteToString(GetSystemInfoResponse[13]);
						 temp += Helper.ConvertHexByteToString(GetSystemInfoResponse[12]);
						 ma.setMemorySize(temp);
					 }
					 else 
						 ma.setMemorySize(Helper.ConvertHexByteToString(GetSystemInfoResponse[12]));
					 
					//*** BLOCK SIZE ***
					 if(ma.isBasedOnTwoBytesAddress())
						 ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
					 else
						 ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[13]));
		
					//*** IC REFERENCE ***
					 if(ma.isBasedOnTwoBytesAddress())
						 ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[15]));
					 else
						 ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
				 }
				 else
				 {
					 ma.setProductName("Unknown product");
				 	 ma.setBasedOnTwoBytesAddress(false);
				 	 ma.setMultipleReadSupported(false);
				 	 ma.setMemoryExceed2048bytesSize(false);
					 //ma.setAfi("00 ");
					 ma.setAfi(Helper.ConvertHexByteToString(GetSystemInfoResponse[11]));				//changed 22-10-2014	
					 //ma.setDsfid("00 ");
					 ma.setDsfid(Helper.ConvertHexByteToString(GetSystemInfoResponse[10]));				//changed 22-10-2014
					 //ma.setMemorySize("FF ");
					 ma.setMemorySize(Helper.ConvertHexByteToString(GetSystemInfoResponse[12]));		//changed 22-10-2014
					 //ma.setBlockSize("03 ");
					 ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[13]));			//changed 22-10-2014
					 //ma.setIcReference("00 ");
					 ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));		//changed 22-10-2014
				 }

			 return true;
		 }
		 
		 // in case of Inventory OK and Get System Info HS
		 else if (ma.getTechno() == "ISO 15693")
		 {
			 ma.setProductName("Unknown product");
		 	 ma.setBasedOnTwoBytesAddress(false);
		 	 ma.setMultipleReadSupported(false);
		 	 ma.setMemoryExceed2048bytesSize(false);
			 ma.setAfi("00 ");
			 ma.setDsfid("00 ");
			 ma.setMemorySize("3F ");				//changed 22-10-2014
			 ma.setBlockSize("03 ");
			 ma.setIcReference("00 ");
			 return true;
		 }
		 
		//if the tag has returned an error code 
		 else
			 return false;
		 
	 }	
		 
}