/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : bt.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : bt
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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;



import com.example.multi_ndef.wifi.ReadDataFromWifi;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




public class bt extends Activity {
	
	 private CNFCInterface ma; 
	 // Intent request codes
	    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	    private static final int REQUEST_ENABLE_BT = 3;
		public byte[] message =null;
		String mac="";
		 byte[] address;
		 Button connect;
		   public static BluetoothService mBluetoothService = null;
		    private BluetoothAdapter mBluetoothAdapter = null;
		    TextView name,status ;
		    // Message types sent from the BluetoothChatService Handler
		    public static final int MESSAGE_STATE_CHANGE = 1;
		    public static final int MESSAGE_READ = 2;
		    public static final int MESSAGE_WRITE = 3;
		    public static final int MESSAGE_DEVICE_NAME = 4;
		    public static final int MESSAGE_TOAST = 5;
		    private static final boolean D = true;
		    private static final String TAG = "BT_Light";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_bt);
		ma = (CNFCInterface)getApplication();
		message =	ma.getBTData();
		connect = (Button) findViewById(R.id.button1);
		name= (TextView) findViewById(R.id.textView3);
		status= (TextView) findViewById(R.id.textView4);
		mBluetoothService = new BluetoothService(this, mHandler);
		ByteBuffer bt = ByteBuffer.wrap(message);
	
	
	
	 mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
     // Register for broadcasts when a device is discovered
     IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
     this.registerReceiver(mReceiver, filter);
     
     BluetoothAdapter mBluetoothAdapter1 = BluetoothAdapter.getDefaultAdapter(); 
	  mBluetoothAdapter1.enable(); 
	  if (mBluetoothAdapter1.isDiscovering()) {
	      mBluetoothAdapter1.enable(); 
	  } 
	  

     // Register for broadcasts when discovery has finished
     filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
     this.registerReceiver(mReceiver, filter);

	parseBtOob(bt);
	
	
	  connect.setOnClickListener(new View.OnClickListener() {
		   
		    
			public void onClick(View v) {

				connectDevice();

			}
		});
	  
	}
	
	
	private NDEFDeviceClassCode _deviceClassCode;
	private byte [] _macAddr;
	private byte [] _deviceClass;
	private String _deviceName;
	private byte _UUIDClassList;
	private byte [] _UUIDClassBuff;
	private byte [] _payload;
	
	
	enum NDEFDeviceClassCode {
		NDEF_RTD_CLASS_PRINTER,
		NDEF_RTD_CLASS_CAMERA,
		NDEF_RTD_CLASS_SMARTPHONE,
		NDEF_RTD_CLASS_HEADSET,
		NDEF_RTD_CLASS_UNDEF,
	}
	
	enum NDEFSimplifiedMessageType {
		NDEF_SIMPLE_MSG_TYPE_EMPTY, // Must always be present at the beginning of the list
		NDEF_SIMPLE_MSG_TYPE_TEXT,
		NDEF_SIMPLE_MSG_TYPE_VCARD,
		NDEF_SIMPLE_MSG_TYPE_URI,
		NDEF_SIMPLE_MSG_TYPE_BTHANDOVER,
		NDEF_SIMPLE_MSG_TYPE_WIFIHANDOVER,
		NDEF_SIMPLE_MSG_TYPE_EXT_M24SRDISCOCTRL,

	}
    @Override
    protected void onDestroy() 
    {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
  

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }
    private final Handler mHandler = new Handler() 
    {
		String []strNetworkInformation=null;
        @Override
        public void handleMessage(Message msg) 
        {
            switch (msg.what) 
            {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) 
                {
                case BluetoothService.STATE_CONNECTED:
           
                	Toast.makeText(getApplicationContext(), "State Connected", Toast.LENGTH_SHORT).show();
                	status.setText("Connected");
                    break;
                case BluetoothService.STATE_CONNECTING:
                    /*mTitle.setText(R.string.title_connecting);*/
                	 Toast.makeText(getApplicationContext(), "State Connecting", Toast.LENGTH_SHORT).show();
                	 status.setText("Connecting");
                	 
                	 break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                    /*mTitle.setText(R.string.title_not_connected);*/
                	 Toast.makeText(getApplicationContext(), "Already Connected", Toast.LENGTH_SHORT).show();
                	 status.setText("Already Connected");
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
              
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
         
                String readMessage = new String(readBuf, 0, msg.arg1);
                       
                break;
            case MESSAGE_DEVICE_NAME:
              
                break;
            case MESSAGE_TOAST:
            
                break;
            }
        }
    };
    @Override
   	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
   	{
   		// TODO Auto-generated method stub
   		/*super.onActivityResult(requestCode, resultCode, data);*/
   		
   		if(D) Log.d(TAG, "onActivityResult " + resultCode);
           switch (requestCode) 
           {
           case REQUEST_CONNECT_DEVICE_SECURE:
               // When DeviceListActivity returns with a device to connect
               if (resultCode == Activity.RESULT_OK) 
               {
               }
               break;
           case REQUEST_CONNECT_DEVICE_INSECURE:
               // When DeviceListActivity returns with a device to connect
               if (resultCode == Activity.RESULT_OK) 
               {
                   //connectDevice(data, false);
               }
               break;
           case REQUEST_ENABLE_BT:
               // When the request to enable Bluetooth returns
               if (resultCode == Activity.RESULT_OK) 
               {
                   // Bluetooth is now enabled, so set up a chat session
                 //  setupChat();
               } 
               else 
               {
                   // User did not enable Bluetooth or an error occured
             
                   finish();
               }
           }
   	}
	private String lower_to_upper(String lower_case)
    {
	    	String test1= lower_case.substring(0,1);
	    	String test2= lower_case.substring(1,2);
	    	if(test1.equals("a")==true)
	    		 //lower_case="A"+test2;
	    		test1="A";
	    	if(test1.equals("b")==true)
	   		 //lower_case="B"+test2;
	    		test1="B";
	    	if(test1.equals("c")==true)
	   		 //lower_case="C"+test2;
	    		test1="C";
	    	if(test1.equals("d")==true)
	   		 //lower_case="D"+test2;
	    		test1="D";
	    	if(test1.equals("e")==true)
	   		 //lower_case="E"+test2;
	    		test1="E";
	    	if(test1.equals("f")==true)
	      		 //lower_case="F"+test2;
	    		test1="F";
	    	
	    	if(test2.equals("a")==true)
	   		 //lower_case="A"+test2;
	   		test2="A";
		   	if(test2.equals("b")==true)
		  		 //lower_case="B"+test2;
		   		test2="B";
		   	if(test2.equals("c")==true)
		  		 //lower_case="C"+test2;
		   		test2="C";
		   	if(test2.equals("d")==true)
		  		 //lower_case="D"+test2;
		   		test2="D";
		   	if(test2.equals("e")==true)
		  		 //lower_case="E"+test2;
		   		test2="E";
		   	if(test2.equals("f")==true)
		     		 //lower_case="F"+test2;
		   		test2="F";
	   	
	   	  //Combine
	   	   lower_case=test1+test2;
	    	 return lower_case;
    }
	

	  
	 private void parseBtOob(ByteBuffer payload) {

	        try {
	            payload.position(1);
	             address = new byte[6];
	            payload.get(address);
	            
	            String hexString,device_address="",final_adrr="";   
	       	 //String device_address="";
       		 for(int i=0;i<=5;i++)
       		 {
       		 int value=0;
       	     value=(int)address[i];
       	     if(value<0)
       	    	 value=256-(value*(-1));
       		  
       		 hexString=Helper.ConvertIntToHexFormatString(value);
       		 hexString=hexString.substring(2, 4);
       		 //Check to convert lowercase to uppercase
       		 final_adrr=lower_to_upper(hexString);
       		if(i!=5)
       	     final_adrr=final_adrr+":";
       		 device_address=device_address+final_adrr;
       		 
       		 
       		 }
    
       		  mac=device_address;
       		 
	            String test="";
	            test=mac;
	            // ByteBuffer.order(LITTLE_ENDIAN) doesn't work for
	            // ByteBuffer.get(byte[]), so manually swap order
	            for (int i = 0; i < 3; i++) {
	                byte temp = address[i];
	                address[i] = address[5 - i];
	                address[5 - i] = temp;
	            }
	            
	            
	            String address =  mac;
	            // Get the BLuetoothDevice object
	            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
	            String device_name=device.getName();
	            name.setText(device_name);
	            
	            //_macAddr=address.clone();
	            _deviceName = null;
	            while (payload.remaining() > 0) {
	                byte[] nameBytes;
	                
	                int len = payload.get();
	                int type = payload.get();
	                switch (type) {
	                    case 0x08:  // short local name
	                        nameBytes = new byte[len - 1];
	                        payload.get(nameBytes);
	                        _deviceName = new String(nameBytes, Charset.forName("UTF-8"));
	                        break;
	                    case 0x09:  // long local name
	                        if (_deviceName != null) break;  // prefer short name
	                        nameBytes = new byte[len - 1];
	                        payload.get(nameBytes);
	                        _deviceName = new String(nameBytes, Charset.forName("UTF-8"));
	                        break;
	                    case 0x0D: //Class of device - 3 Bytes with Service Class / Major Device Class / Minor Device Class
	                    	_deviceClass = new byte[3];
	                    	payload.get(_deviceClass);
	                    	break;
	                    case 0x02: //16-bit un-complete Service Class UUID list
	                    	_UUIDClassList = (byte) 0x02;
	                    	_UUIDClassBuff = new byte[len - 1];
	                        payload.get(_UUIDClassBuff);
	                    	break;
	                    case 0x03: //16-bit complete Service Class UUID list
	                    	_UUIDClassList = (byte) 0x03;
	                    	_UUIDClassBuff = new byte[len - 1];
	                        payload.get(_UUIDClassBuff);
	                    	break;
	                    case 0x04: //32-bit un-complete Service Class UUID list
	                    	_UUIDClassList = (byte) 0x04;
	                    	_UUIDClassBuff = new byte[len - 1];
	                        payload.get(_UUIDClassBuff);
	                    	break;
	                    case 0x05: //32-bit complete Service Class UUID list
	                    	_UUIDClassList = (byte) 0x05;
	                    	_UUIDClassBuff = new byte[len - 1];
	                        payload.get(_UUIDClassBuff);
	                    	break;
	                    case 0x06: //128-bit un-complete Service Class UUID list
	                    	_UUIDClassList = (byte) 0x06;
	                    	_UUIDClassBuff = new byte[len - 1];
	                        payload.get(_UUIDClassBuff);
	                    	break;
	                    case 0x07: //128-bit complete Service Class UUID list
	                    	_UUIDClassList = (byte) 0x07;
	                    	_UUIDClassBuff = new byte[len - 1];
	                        payload.get(_UUIDClassBuff);
	                    	break;
	                    default:
	                        payload.position(payload.position() + len - 1);
	                        break;
	                }
	            }
	        } catch (IllegalArgumentException e) {
	      
	        } catch (BufferUnderflowException e) {
	            
	        }
	        
	          
	        
	        
	    }
	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() 
	    {
	        @Override
	        public void onReceive(Context context, Intent intent) 
	        {
	            String action = intent.getAction();

	           // When discovery finds a device
	            if (BluetoothDevice.ACTION_FOUND.equals(action)) 
	            {
	                // Get the BluetoothDevice object from the Intent
	                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                // If it's already paired, skip it, because it's been listed already
	                if (device.getBondState() != BluetoothDevice.BOND_BONDED) 
	                {
	                   // mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	                }
	            // When discovery is finished, change the Activity title
	            } 
	            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) 
	            {
	                setProgressBarIndeterminateVisibility(false);
	        
	            }
	        }
	    };
	 
	   private void connectDevice() 
       {
		   String address =  mac;
		   BluetoothDevice localBluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(address);
		   mBluetoothService.connect(localBluetoothDevice, true);
		
       }      
  }
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/

