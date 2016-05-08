/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : VCardHandler.java
* Author           : Systems Lab
* Version          : 1.0
* Date             : 1st Sep, 2014
* Description      : VCardHandler
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefRecord;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class VcardHandler {    
	


    private Uri uriContact;
    private String contactID;     // contacts unique I
    
    
    public VcardHandler mVcardHandler;
	
	public String _Number;
	public String _Name;
	public String _NickName;
	public String _FormatedName;
	
	public String _Email;
	public String _SPAddr;    
	public String _Vcard;
	
    NdefRecord nfcRecord = null;
    static final String TAG = "VCARD - TEST";

	public VcardHandler()
	{
		_Number = null;
		_Name = null;
		_NickName = null;
	 	_FormatedName = null;
		_Email = null;
		_SPAddr = null;    
		_Vcard = null;
	}
	
	public String getNumber ()
	{
		return _Number;
	}
	
	public String getName()
	{
		return _Name;
	}
	
	public String getFormatedName()
	{
		return _FormatedName;
	}
	
	public String getNickName()
	{
		return _NickName;
	}
	
	public String getEmail()
	{
		return _Email;
	}
	
	public String getSPAddr()
	{
		return _SPAddr;
	}

	
	public String getVcard()
	{
		return _Vcard;
	}
	
	public void setNumber( String aNumber) { _Number= new String(aNumber);}
	public void setName(String aName) { _Name = new String(aName);}
	public void setFormatedName(String aFormatedName) { _FormatedName = new String(aFormatedName);}
	public void setNickName(String aNickName) { _NickName = new String(aNickName);}
	public void setEmail(String aEmail) {_Email = new String(aEmail);}
	public void setSPAddr(String aSPAddr) {_SPAddr = new String(aSPAddr);}
	public void setVcard(String vcardString) {_Vcard = new String(vcardString);}
     
}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/