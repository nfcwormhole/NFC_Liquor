/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : Mail.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : Mail
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

import java.nio.charset.Charset;
import java.util.Scanner;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Mail extends Activity implements OnClickListener{
	
	Button send;
	byte[] mailInByte; 
	String mailData;
	String[] torecipients,ccrecipients,bccrecipients;
	Scanner _scanner;
	String item,contacts;
	TextView to,cc,bcc,subject,body;
	String Sto="",Scc="",Sbcc="",Ssubject="",Sbody="";
	 private CNFCInterface ma; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail);
		
		
		send=(Button)findViewById(R.id.sendMail);
		to=(TextView)findViewById(R.id.labelTo);
		cc=(TextView)findViewById(R.id.labelCc);
		bcc=(TextView)findViewById(R.id.labelBcc);
		subject=(TextView)findViewById(R.id.labelSubject);
		body=(TextView)findViewById(R.id.labelBody);
		send.setOnClickListener(Mail.this);
		
		ma = (CNFCInterface)getApplication();
		
		
		mailData=new String(ma.getMailData(),Charset.forName("US-ASCII"));
		item=mailData;
		 //_string2parse = smsData.toString();
			if (!mailData.isEmpty())
			{
				
				parse();
			}
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mail, menu);
		return true;
	}

	

	@Override
	public void onClick(View v) {
	
	if(((Button)v).getId()==send.getId())
	{
		sendMail();
	}
	}
	
	
	
	protected void sendMail() {
	      Log.i("Send Mail", "");
	      
	      
	      Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	      String aEmailList[] = torecipients;
	      String aEmailCCList[] = ccrecipients;
	      String aEmailBCCList[] = bccrecipients;
	      if(aEmailList!=null)
	      {
	      emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
	      }
	      if(aEmailCCList!=null)
	      {
	      emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
	      }
	      if(aEmailBCCList!=null)
	      {
	      emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
	      }
	      if(!Ssubject.equals(""))
	      {
	      emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,Ssubject);
	      }
	      emailIntent.setType("text/plain");
	      if(!Sbody.equals(""))
	      {
	      emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,Sbody);
	      }
	     /* startActivity(emailIntent);
	      //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	      finish();
*/
	      
	      

	    
	      try {
	         startActivity(emailIntent);
	         finish();
	         Log.i("Finished sending Mail...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(Mail.this, 
	         "Mail faild, please try again later.", Toast.LENGTH_SHORT).show();
	      }
	   }
	
	
	public int parse()
	{
		
		String property[] = new String[2];
		
		int pos=item.indexOf('?'); 
		if(pos!=-1)
		{
		property[0]=item.substring(0, pos);
		property[1]=item.substring(pos+1);
		
		String toData =  property[0].substring(1);
		if(!toData.isEmpty())
		{
		String mailDetails[] =  property[1].split("&");
		
		
		torecipients=toData.split(",");
			
			Sto=appendStringFromArrayString(torecipients);
			
			
			
			for(int i=0;i<mailDetails.length;i++)
			{
				String field[]=mailDetails[i].split("=");
				if(!field[1].isEmpty())
				{
				
				if(field[0].equals("subject"))
				{
					Ssubject=field[1];
				}
				else if(field[0].equals("body"))
				{
					Sbody=field[1];
				}
				else if(field[0].equals("cc"))
				{
					
					ccrecipients=field[1].split(",");
					Scc=appendStringFromArrayString(ccrecipients);
					
				}
				else if(field[0].equals("bcc"))
				{
					
					bccrecipients=field[1].split(",");
					Sbcc=appendStringFromArrayString(bccrecipients);
					
				}
				}
				
			}
			
			
			cc.setText(Scc);
			bcc.setText(Sbcc);
			subject.setText(Ssubject);
			body.setText(Sbody);
			to.setText(Sto);
		}
		}
		else if(item.length()>1)
		{
			String toData =  item.substring(1);
			pos=toData.indexOf('&');
			String data=toData.substring(pos+1);
			toData=toData.substring(0, pos);
			
			
			String mailDetails[] =  data.split("&");
			
			if(!toData.isEmpty())
			{
				torecipients=toData.split(",");
				
				Sto=appendStringFromArrayString(torecipients);
				
				to.setText(Sto);
				
				for(int i=0;i<mailDetails.length;i++)
				{
					String field[]=mailDetails[i].split("=");
					if(!field[1].isEmpty())
					{
					
					if(field[0].equals("subject"))
					{
						Ssubject=field[1];
					}
					else if(field[0].equals("body"))
					{
						Sbody=field[1];
					}
					else if(field[0].equals("cc"))
					{
						
						ccrecipients=field[1].split(",");
						Scc=appendStringFromArrayString(ccrecipients);
						
					}
					else if(field[0].equals("bcc"))
					{
						
						bccrecipients=field[1].split(",");
						Sbcc=appendStringFromArrayString(bccrecipients);
						
					}
					}
					
				}
				cc.setText(Scc);
				bcc.setText(Sbcc);
				subject.setText(Ssubject);
				body.setText(Sbody);
			}
		}
		
		
		return 0;
	}
	
	
	
	
	public String appendStringFromArrayString(String [] arrayString)
	{
		StringBuffer result = new StringBuffer();
		for (int i = 0; i<arrayString.length;i++)
		{
			result.append(arrayString[i]);
			result.append("\n");
		}
		return result.toString();
	}

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/