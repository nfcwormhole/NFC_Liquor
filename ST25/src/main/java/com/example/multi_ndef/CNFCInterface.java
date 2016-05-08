/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************

* File Name        : CNFCInterface.java
* Author           : IMS Systems Lab & Technical Marketing 
* Version          : 1.0
* Date             : 8th June, 2013
* Description      : CNFCInterface
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


import android.app.Application;
import android.net.Uri;
import android.nfc.Tag;

public class CNFCInterface extends Application 
{
	private Tag currentTag;
	private byte[] arrayEEPROMData ;
	
	private String EngineeringInfo;
	private String EngineeringInfo2;
	private String uid;
	private String techno;
	private String manufacturer;
	private String productName;
	private String dsfid;
	private String afi;
	private String memorySize;
	private String blockSize;
	private String icReference;
	private boolean basedOnTwoBytesAddress;
	private boolean readOperation;
	private String bDeviceAddress;
	private String bLocalName;
	private byte[] arrayEnergyData;
	private boolean activityVisible;
	
	private byte[] TextData ;
	private byte[] UriData ;
	private byte[] ContactData ;
	private byte[] WiFiData ;
	private byte[] BTData ;
	private byte[] SMSData ;
	private byte[] MailData ;
	private byte[] TelephoneData ;
	private byte[] LocationData ;
	private String network;
	private String password;
	private String text_data;
	private String pass_name;
	private String URI;
	private int pass_length;
	private String net_name;
	private int net_length;
	private String[] mac_add;
	private Uri teluri;
	private String BT_Device_name,Mac_address,MultiContacts,v_name,v_number,v_add,v_mail;
	private int index_bt_item,noOfContacts;
	private Uri locationuri;
	
	
	public void setv_mail(String v_mail)
	{
		this.v_mail=v_mail;
	}

	
	public String getv_mail()
	{
		return v_mail;
	}
	
	
	public void setv_add(String v_add)
	{
		this.v_add=v_add;
	}

	
	public String getv_add()
	{
		return v_add;
	}
	
	public void setv_number(String v_number)
	{
		this.v_number=v_number;
	}

	
	public String getv_number()
	{
		return v_number;
	}
	
	public void setv_name(String v_name)
	{
		this.v_name=v_name;
	}

	
	public String getv_name()
	{
		return v_name;
	}
	
	public void setMultiContacts(String MultiContacts)
	{
		this.MultiContacts=MultiContacts;
	}

	
	public String getMultiContacts()
	{
		return MultiContacts;
	}
	
	public void setnoOfContacts(int noOfContacts)
	{
		this.noOfContacts=noOfContacts;
	}

	
	public int getnoOfContacts()
	{
		return noOfContacts;
	}
	
	public void setMac_address(String Mac_address)
	{
		this.Mac_address=Mac_address;
	}

	
	public String getMac_address()
	{
		return Mac_address;
	}
	
	public void setindex_bt_item(int index_bt_item)
	{
		this.index_bt_item=index_bt_item;
	}

	
	public int getindex_bt_item()
	{
		return index_bt_item;
	}
	
	public void setBT_Device_name(String BT_Device_name)
	{
		this.BT_Device_name=BT_Device_name;
	}

	
	public String getBT_Device_name()
	{
		return BT_Device_name;
	}
	
	public void setWifiNetwork(String network)
	{
		this.network=network;
	}

	
	public String getWifiNetwork()
	{
		return network;
	}
	
	public void setWifiPassword(String password)
	{
		this.password=password;
	}

	
	public String getWifiPassword()
	{
		return password;
	}
	
	int check=1;
	
	public void setTelephoneUri(Uri uri)
	{
		teluri=uri;
	}
	
	public void setLocationUri(Uri uri)
	{
		locationuri=uri;
	}
	
	public Uri getTelephoneUri()
	{
		return teluri;
	}
	
	public Uri getLocationUri()
	{
		return locationuri;
	}
	
	public String getWriteText() 
	{
		return  text_data;
	}

	public void setWriteText(String text_data) 
	{
		this.text_data =  text_data;
	}
	
	
	public String[] getWriteMac() 
	{
		return  mac_add;
	}

	public void setWriteMac(String[] mac_add) 
	{
		this.mac_add =  mac_add;
	}
	
	public String getWritePassName() 
	{
		return  pass_name;
	}

	public void setWritePassName(String pass_name) 
	{
		this.pass_name =  pass_name;
	}
	
	public String getWriteNetName() 
	{
		return  net_name;
	}

	public void setWriteNetName(String net_name) 
	{
		this.net_name =  net_name;
	}
	
	
	public String getWriteUri() 
	{
		return  URI;
	}

	public void setWriteURI(String URI) 
	{
		this.URI =  URI;
	}
	
	
	public int getWritePassLen() 
	{
		return  pass_length;
	}

	public void setWritePassLen(int pass_length) 
	{
		this.pass_length =  pass_length;
	}
	
	
	public int getWriteNetLen() 
	{
		return  net_length;
	}

	public void setWriteNetLen(int net_length) 
	{
		this.net_length =  net_length;
	}
	
	
	
	
	public byte[] getTextData() 
	{
		return  TextData;
	}

	public void setTextData(byte[] TextData) 
	{
		this.TextData =  TextData;
	}
	
	public byte[] getUriData() 
	{
		return  UriData;
	}

	public void setUriData(byte[] UriData) 
	{
		this.UriData =  UriData;
	}
	
	public byte[] getContactData() 
	{
		return  ContactData;
	}

	public void setContactData(byte[] ContactData) 
	{
		this.ContactData =  ContactData;
	}
	
	
	public byte[] getWiFiData() 
	{
		return  WiFiData;
	}

	public void setWiFiData(byte[] WiFiData) 
	{
		this.WiFiData =  WiFiData;
	}
	
	public byte[] getBTData() 
	{
		return  BTData;
	}

	public void setBTData(byte[] BTData) 
	{
		this.BTData =  BTData;
	}
	
	
	
	public byte[] getSMSData() 
	{
		return  SMSData;
	}

	public void setSMSData(byte[] SMSData) 
	{
		this.SMSData =  SMSData;
	}
	
	
	
	public byte[] getMailData() 
	{
		return  MailData;
	}

	public void setMailData(byte[] MailData) 
	{
		this.MailData =  MailData;
	}
	
	
	public byte[] getTelephoneData() 
	{
		return  TelephoneData;
	}

	public void setTelephoneData(byte[] TelephoneData) 
	{
		this.TelephoneData =  TelephoneData;
	}
	
	
	public byte[] getLocationData() 
	{
		return  LocationData;
	}

	public void setLocationData(byte[] LocationData) 
	{
		this.LocationData =  LocationData;
	}
	
	
	public void setCurrentTag(Tag currentTag) {
		this.currentTag = currentTag;
	}

	public Tag getCurrentTag() {
		return currentTag;
	}

	public boolean getActivityVisible() {
	    return activityVisible;
	  }  
	public void setActivityVisible(boolean flag)
	{
		activityVisible=flag;
	}

	public byte[] getArrayenergyData() 
	{
		return  arrayEnergyData;
	}

	public void setArrayEnergyData2(byte[] arrayData) 
	{
		this.arrayEnergyData =  arrayData;
	}
	
	public String getEngineeringInformation()
	{
		return EngineeringInfo;
	}
	
	public void setEngineeringInformation(String s)
	{
		EngineeringInfo = s;
	}
	
	public String getEngineeringInformation2()
	{
		return EngineeringInfo2;
	}
	
	public void setEngineeringInformation2(String s)
	{
		EngineeringInfo2 = s;
	}
	public byte[] getArrayEEPROMData() 
	{
		return arrayEEPROMData;
	}
	
	public void setArrayEnergyData(byte[] arrayEEPROM) 
	{
		this.arrayEEPROMData = arrayEEPROM;
	}
	
	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getUid() 
	{
		return uid;
	}

	public void setTechno(String techno) 
	{
		this.techno = techno;
	}

	public String getTechno() {
		return techno;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setDsfid(String dsfid) {
		this.dsfid = dsfid;
	}

	public String getDsfid() {
		return dsfid;
	}

	public void setAfi(String afi) {
		this.afi = afi;
	}

	public String getAfi() 
	{
		return afi;
	}

	public void setCheck()
	{
		this.check=check+1;
		
	}
	
	public int getCheck()
	{
		return check;
		
	}
	public void setMemorySize(String memorySize) 
	{
		this.memorySize = memorySize;
	}

	public String getMemorySize() 
	{
		return memorySize;
	}

	public void setBlockSize(String blockSize) 
	{
		this.blockSize = blockSize;
	}

	public String getBlockSize() 
	{
		return blockSize;
	}

	public void setIcReference(String icReference) 
	{
		this.icReference = icReference;
	}

	public String getIcReference() 
	{
		return icReference;
	}

	public void setBasedOnTwoBytesAddress(boolean basedOnTwoBytesAddress) 
	{
		this.basedOnTwoBytesAddress = basedOnTwoBytesAddress;
	}

	public boolean isBasedOnTwoBytesAddress() 
	{
		return basedOnTwoBytesAddress;
	}
	
	public boolean GetReadOperation()
	{
		return readOperation;
	}
	
	public void SetReadOperation(boolean flag)
	{
		readOperation = flag;
	}
	//Bluetooth Parameters
	public void setBluetoothDeviceAddress(String bDeviceAddress)
	{
		this.bDeviceAddress=bDeviceAddress;
	}
	public String getBluetoothDeviceAddress()
	{
		return bDeviceAddress;
	}
	public void setBluetoothLocalName(String bLocalName)
	{
		this.bLocalName=bLocalName;
	}
	public String getLocalName()
	{
		return bLocalName;
	}
	

}
/******************** (C) COPYRIGHT 2014 STMicroelectronics ********************/
