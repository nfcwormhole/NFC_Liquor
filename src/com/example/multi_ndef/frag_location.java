/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : frag_location.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : frag_location
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

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class frag_location extends Fragment implements OnClickListener{
	
	double latitudevalue,longitudevalue;
	EditText latitude,longitude;
	GPSTracker gps;
	Button location;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_location, container, false);

        	location=(Button)v.findViewById(R.id.location);
        	location.setOnClickListener(this);
			latitude=(EditText)v.findViewById(R.id.editlatitude);
			longitude=(EditText)v.findViewById(R.id.editlongitude);
			
      
      

        return v;
    }

    public static frag_location newInstance(String text) {

    	frag_location f = new frag_location();
       
    	 Bundle b = new Bundle();
         

         f.setArguments(b);
        return f;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		 if(((Button)v).getId()==location.getId())
			{
				
				
				gps = new GPSTracker(getActivity());                 // check if GPS enabled  
				if(gps.canGetLocation()){    
					latitudevalue= gps.getLatitude();        
					longitudevalue = gps.getLongitude();   
					latitude.setText(String.valueOf(latitudevalue));
					longitude.setText(String.valueOf(longitudevalue));
					// \n is for new line                 
					Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitudevalue + "\nLong: " + longitudevalue, Toast.LENGTH_LONG).show();     
					}else{      
						// can't get location           
						// GPS or Network is not enabled     
						// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();             
				}
			
			}
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/