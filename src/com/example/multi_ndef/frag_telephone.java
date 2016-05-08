/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : frag_telephone.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : frag_telephone
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
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class frag_telephone  extends Fragment{
	Button fetchContact;
	EditText numbers;
	
	protected static final int PICK_NUMBER=1;
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_telephone, container, false);
        fetchContact=(Button)v.findViewById(R.id.fetchTeleContact);
		  numbers=(EditText)v.findViewById(R.id.editTelephone);
		 
	        
		  
		fetchContact.setOnClickListener(new View.OnClickListener() {
			   
			
			public void onClick(View v) {
				
				

				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_NUMBER);
		            
		            
		            
			}
		});
    
      
      

        return v;
    }

    public static frag_telephone newInstance(String text) {

    	frag_telephone f = new frag_telephone();
    	 Bundle b = new Bundle();
         

         f.setArguments(b);

        return f;
    }
    
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		  super.onActivityResult(reqCode, resultCode, data);

		  switch (reqCode) {
		   case PICK_NUMBER:
		    	 if (resultCode == Activity.RESULT_OK) {
			    	  try
			    	  {
			        Uri contactData = data.getData();
			        Cursor c =  getActivity().getContentResolver().query(contactData, null, null, null, null);
			        
			        
			        if (c.moveToFirst()) {
			          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			          String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)); 
			          
			          String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
			         
			          if ((Integer.parseInt(hasPhone)==1 )){ 
			          
			          Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
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
				    
		  }
		}
	
	
	
	
	private void displayMessage(String message) {
		
	
		Toast toast = Toast.makeText(getActivity().getApplicationContext(),
				message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
		toast.show();
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/