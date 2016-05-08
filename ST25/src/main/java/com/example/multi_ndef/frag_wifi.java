/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : frag_wifi.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : frag_wifi
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.multi_ndef.Write.OnItemSelected;



import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class frag_wifi extends Fragment implements OnItemSelectedListener{
	
	 String network_name;
     String[] mac_add;
     String password;
     
     Spinner mSpinner;
     EditText mEditText1;
     byte[] dataToWrite;
     
 	int pass_length;
	int net_length;
	 private CNFCInterface fr;
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.frag_wifi, container, false);

	        
	        mSpinner = (Spinner)v.findViewById(R.id.spinner);
	        mSpinner.setOnItemSelectedListener(this);
	      
	        mEditText1 = (EditText)v.findViewById(R.id.editText1);
	        fr=(CNFCInterface)getActivity().getApplication();
	        
	        WifiManager wifi;
			try {
				wifi = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);

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

				for (int i1 = 1; i1 < length; i1++) {
					SpinnerArray.add(uniques[i1]);
				}

				// Create an ArrayAdapter using the string array and a default
			
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
						android.R.layout.simple_spinner_item, SpinnerArray);
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// Apply the adapter to the spinner
				mSpinner.setAdapter(adapter);

				

			} catch (Exception e) {
				Toast toast = Toast.makeText(getActivity().getApplicationContext(),
						"Turn on the wifi", Toast.LENGTH_SHORT);
				toast.show();

			}
			
			
	        return v;
	    }

	    public static frag_wifi newInstance(String text) {

	    	frag_wifi f = new frag_wifi();
	    	 Bundle b = new Bundle();
	         

	         f.setArguments(b);

	        return f;
	    }
	   
	    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
		
				try{
				network_name = mSpinner.getSelectedItem().toString();
				password = mEditText1.getText().toString();
				fr.setWifiNetwork(network_name);
				fr.setWifiPassword(password);
				
				}
				catch(Exception e)
				{
					Toast toast = Toast.makeText(getActivity().getApplicationContext(),
							"Unable to populate available Wi-Fi networks. Please press back button and again press write", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
					toast.show();
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		
}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/

	
