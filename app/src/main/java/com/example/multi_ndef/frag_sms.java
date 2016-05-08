/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : frag_sms.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : frag_sms
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class frag_sms extends Fragment implements OnClickListener{
	Button fetchContact,addMore;
	EditText numbers;
	TextView Recipients;
	String SMSpayload="",MultiContacts="",recipients="",Telepayload="";
	int noOfContacts=1;
	protected static final int PICK_NUMBER=1;
	 private CNFCInterface ma;
		
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_sms, container, false);


		  fetchContact=(Button)v.findViewById(R.id.fetchContact);
		  numbers=(EditText)v.findViewById(R.id.editcontact);
		  Recipients=(TextView)v.findViewById(R.id.Recipients);
		  addMore=(Button)v.findViewById(R.id.addRec);
		  addMore.setOnClickListener(this);
		  ma=(CNFCInterface)getActivity().getApplication();
	        
		  
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
					ma.setMultiContacts(MultiContacts);
					ma.setnoOfContacts(noOfContacts);
					
				}
				else
				{
					MultiContacts+=";"+numbers.getText().toString();
					noOfContacts++;
					numbers.setText("");
					recipients=MultiContacts;
					Recipients.setText(recipients);
					ma.setMultiContacts(MultiContacts);
					ma.setnoOfContacts(noOfContacts);
					
				}
				}

				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_NUMBER);
		             
			}
		});
      

        return v;
    }

    public static frag_sms newInstance(String text) {

    	frag_sms f = new frag_sms();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		 if(((Button)v).getId()==addMore.getId())
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
					ma.setMultiContacts(MultiContacts);
					ma.setnoOfContacts(noOfContacts);
				}
				else
				{
					MultiContacts+=";"+numbers.getText().toString();
					noOfContacts++;
					numbers.setText("");
					recipients=MultiContacts;
					Recipients.setText(recipients);
					ma.setMultiContacts(MultiContacts);
					ma.setnoOfContacts(noOfContacts);
					
				}
				}
			}
		
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/
