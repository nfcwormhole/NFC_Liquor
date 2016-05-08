/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : frag_contact.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : frag_contact
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class frag_contact extends Fragment{
	 private CNFCInterface ma;
		
	 public EditText v_name;
	 Button fatchContact;
		public EditText v_mail;
		public EditText v_number;
		public EditText v_add;
		protected static final int PICK_CONTACT= 0;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_contact, container, false);
        ma=(CNFCInterface)getActivity().getApplication();
        fatchContact= (Button) v.findViewById(R.id.button2);
        v_name = (EditText) v.findViewById(R.id.editText4);
        v_number = (EditText) v.findViewById(R.id.editText5);
        v_mail = (EditText) v.findViewById(R.id.editText6);
        v_add = (EditText) v.findViewById(R.id.editText7);
        
        
        
        fatchContact.setOnClickListener(new View.OnClickListener() {
			   
		    
			public void onClick(View v) {



				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
		            
		            
		            
			}
		});
      
      

        return v;
    }

    public static frag_contact newInstance(String text) {

    	frag_contact f = new frag_contact();
    	 Bundle b = new Bundle();
         

         f.setArguments(b);

        return f;
    }
    
    
    
    
    @Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  switch (reqCode) {
	    case (PICK_CONTACT) :
	      if (resultCode == Activity.RESULT_OK) {
	        Uri contactData = data.getData();
	        Cursor c =   getActivity().getContentResolver().query(contactData, null, null, null, null);
	        
	        
	        if (c.moveToFirst()) {
	          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	       
	          v_name.setText(name);
	        
	          String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)); 
	          
	          String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
	         
	          if ((Integer.parseInt(hasPhone)==1 )){ 
	          
	          Cursor phones =  getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
	          null, 
	          ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, 
	          null, null); 
	          while (phones.moveToNext()) { 
	        String  phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
	   
	        v_number.setText(phoneNumber);
	        
	        Cursor cursorPhone =  getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
	                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
	 
	                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
	 
	                new String[]{contactId},
	                null);
	 
	        if (cursorPhone.moveToFirst()) {
	        String	Email= cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
	        
	     v_mail.setText(Email);
	        
	        }
	        
	        Cursor cursorPhone1 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
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
	  }
    }

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/