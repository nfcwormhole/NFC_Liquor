/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : frag_url.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : frag_url
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
import android.view.ViewGroup;

public class frag_url extends Fragment{
	
	
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.frag_url, container, false);

	      
	      

	        return v;
	    }

	    public static frag_url newInstance(String text) {

	    	frag_url f = new frag_url();
	       
	    	 Bundle b = new Bundle();
	         

	         f.setArguments(b);
	        return f;
	    }

	}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/