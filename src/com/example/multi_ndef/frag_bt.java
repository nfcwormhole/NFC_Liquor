/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : frag_bt.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : frag_bt
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
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class frag_bt extends Fragment  implements OnItemSelectedListener{
	
	String[] mac_addBT;
	String BT_Device_name="";
	int index_bt_item=0;
	String Mac_address="";
	List<String> SpinnerArrayBT_Device_name = new ArrayList<String>();
	List<String> SpinnerArrayBT_devices_mac = new ArrayList<String>();
	Spinner btSpinner;
	 private CNFCInterface ma;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_bt, container, false);

        btSpinner= (Spinner) v.findViewById(R.id.spinnerbt);
	    btSpinner.setOnItemSelectedListener(this);
	    ma=(CNFCInterface)getActivity().getApplication();
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
				
				Toast toast = Toast.makeText(getActivity().getApplicationContext(),
						"Bluetooth Code Error", Toast.LENGTH_SHORT);
				toast.show();
				
				int a1=10;
			}
   
     ArrayAdapter<String>btArrayAdapter= new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, SpinnerArrayBT_Device_name);	
	 btArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 btSpinner.setAdapter(btArrayAdapter);
			
        
        

        return v;
    }

    public static frag_bt newInstance(String text) {

    	frag_bt f = new frag_bt();
       

        return f;
    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		  try{
              BT_Device_name=btSpinner.getSelectedItem().toString();
              
              index_bt_item=btSpinner.getSelectedItemPosition();
              Mac_address=SpinnerArrayBT_devices_mac.get(index_bt_item);
              
              ma.setBT_Device_name(BT_Device_name);
              ma.setindex_bt_item(index_bt_item);
              ma.setMac_address(Mac_address);
              
              
              }
              catch(Exception e)
              {
           	   Toast toast = Toast.makeText(getActivity().getApplicationContext(),
      					"Spinner BT Error"+e.toString(), Toast.LENGTH_SHORT);
      			toast.show();
              }
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}

/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/


