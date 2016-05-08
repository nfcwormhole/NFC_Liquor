/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : frag_mail.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : frag_mail
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class frag_mail extends Fragment implements OnClickListener{
	public Button fetchto,fetchcc,fetchbcc;
	String Locationpayload="",Mailpayload="",Sto="",Scc="",Sbcc="",Ssubject="";
	EditText to,cc,bcc,subject,body;
	protected static final int PICK_MAIL=2;
	String Control;
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_mail, container, false);


		body=(EditText)v.findViewById(R.id.editBody);
		to=(EditText)v.findViewById(R.id.editTo);
		cc=(EditText)v.findViewById(R.id.editCc);
		bcc=(EditText)v.findViewById(R.id.editBcc);
		subject=(EditText)v.findViewById(R.id.editSubject);
		
		
		  fetchto=(Button)v.findViewById(R.id.addto);
		  fetchto.setOnClickListener(this);
			   
		  fetchcc=(Button)v.findViewById(R.id.addcc);
		  fetchcc.setOnClickListener(this);
		  
		  fetchbcc=(Button)v.findViewById(R.id.addbcc);
		  fetchbcc.setOnClickListener(this);
		  
      

        return v;
    }

    public static frag_mail newInstance(String text) {

    	frag_mail f = new frag_mail();
    	 Bundle b = new Bundle();
        

         f.setArguments(b);

        return f;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(((Button)v).getId()==fetchto.getId())
		{
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, PICK_MAIL);
	        Control="To";    
	            
	            
		}
		
		else if(((Button)v).getId()==fetchcc.getId())
		{
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, PICK_MAIL);
			Control="Cc";
	            
	            
	            
		}
		
		else if(((Button)v).getId()==fetchbcc.getId())
		{
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, PICK_MAIL);
			Control="Bcc";    
	            
	            
		}
		
	}
	
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  switch (reqCode) {
	    case PICK_MAIL:
	    	if (resultCode == Activity.RESULT_OK) {
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
		   
		        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
		                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
		 
		                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
		 
		                new String[]{contactId},
		                null);
		 
		        if (cursorPhone.moveToFirst()) {
		        String	Email= cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
		        if(Control.equals("To"))
		        {
		        	String tempTo=to.getText().toString();
		        	if(!tempTo.isEmpty())
		        	{
		        		to.setText(tempTo+","+Email);
		        	}
		        	else
		        		to.setText(Email);
		        }
		        else if(Control.equals("Cc"))
		        {
		        	String tempCc=cc.getText().toString();
		        	if(!tempCc.isEmpty())
		        	{
		        		cc.setText(tempCc+","+Email);
		        	}
		        	else
		        		cc.setText(Email);
		        }
		        else if(Control.equals("Bcc"))
		        {
		        	String tempBcc=bcc.getText().toString();
		        	if(!tempBcc.isEmpty())
		        	{
		        		bcc.setText(tempBcc+","+Email);
		        	}
		        	else
		        		bcc.setText(Email);
		        }
		       
		        }
		          }
		          }
		        }
		      }
	    	break;
	    
	  }
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/
