/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : text.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : text
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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;



public class text extends Activity {

	 private CNFCInterface ma; 
	 
	 TextView t;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_text);
		
		ma = (CNFCInterface)getApplication();
		   t= (TextView) findViewById(R.id.textView2);
		
		   String msg =  new String(ma.getTextData());
		
	       t.setText(msg.substring(3));
		
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
	
		return true;
	}
	
	
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
			}
		
	
	@Override
    protected void onPause() {
		super.onPause();    
    }
	
}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/