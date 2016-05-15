// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.

package com.nfc.apps;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import android.util.Log;

public class FileManagement extends Activity {

    Button buttonLoadFromFile;
    Button buttonSaveInFile;
    byte[] GetSystemInfoAnswer = null;
    byte[] numberOfBlockToRead = null;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private String blockName = null;
    private String blockValue = null;
    private long cpt = 0;
    private byte[] WriteSingleBlockAnswer = null;
    private byte[] ReadMultipleBlockAnswer = null;
    private byte[] addressStart = null;
    private byte[] dataToWrite = new byte[4];

    private byte[] bufferFile = null;
    ;
    private int blocksToWrite = 0;

    private boolean FileError = false;

    private EditText textLoadFileName;
    private EditText textSaveFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_management_menu);

        Bundle objetbunble = this.getIntent().getExtras();
        DataDevice dataDevice = (DataDevice) getApplication();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mFilters = new IntentFilter[]{ndef,};
        mTechLists = new String[][]{new String[]{android.nfc.tech.NfcV.class.getName()}};

        FileError = false;

        initListener();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        DataDevice ma = (DataDevice) getApplication();
        ma.setCurrentTag(tagFromIntent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    protected void onPause() {
        cpt = 500;
        super.onPause();
    }

    private void initListener() {

        textSaveFileName = (EditText) findViewById(R.id.editTextSaveFileName);
        textSaveFileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                int astart = textSaveFileName.getSelectionStart();
                int aend = textSaveFileName.getSelectionEnd();

                String FieldValue = s.toString();//.toUpperCase();

                if (FieldValue.length() == 0) {
                    textSaveFileName.setTextColor(0xffff0000); //RED color
                    buttonSaveInFile.setClickable(false);
                } else {
                    textSaveFileName.setTextColor(0xff000000); //BLACK color
                    buttonSaveInFile.setClickable(true);

                }

                if (Helper.checkFileName(FieldValue) == false) {
                    textSaveFileName.setTextKeepState(Helper.checkAndChangeFileName(FieldValue));
                    textSaveFileName.setSelection(astart - 1, aend - 1);
                } else
                    textSaveFileName.setSelection(astart, aend);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }
        });

        textLoadFileName = (EditText) findViewById(R.id.editTextLoadFileName);
        //value1.setInputType(android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        textLoadFileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                int astart = textLoadFileName.getSelectionStart();
                int aend = textLoadFileName.getSelectionEnd();

                String FieldValue = s.toString();//.toUpperCase();

                if (FieldValue.length() == 0) {
                    textLoadFileName.setTextColor(0xffff0000); //RED color
                    buttonLoadFromFile.setClickable(false);
                } else {
                    textLoadFileName.setTextColor(0xff000000); //BLACK color
                    buttonLoadFromFile.setClickable(true);

                }

                if (Helper.checkFileName(FieldValue) == false) {
                    textLoadFileName.setTextKeepState(Helper.checkAndChangeFileName(FieldValue));
                    textLoadFileName.setSelection(astart - 1, aend - 1);
                } else
                    textLoadFileName.setSelection(astart, aend);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }
        });

        buttonSaveInFile = (Button) findViewById(R.id.SaveInFileButton);
        buttonLoadFromFile = (Button) findViewById(R.id.LoadFromFileButton);

        buttonSaveInFile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //Close Keyboard before stating activity
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textSaveFileName.getApplicationWindowToken(), 0);

                new StartSaveInFileTask().execute();
                //Used for DEBUG : Log.i("Write", "SUCCESS");
            }
        });

        buttonLoadFromFile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //Close Keyboard before stating activity
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textLoadFileName.getApplicationWindowToken(), 0);

                new StartLoadFromFileTask().execute();
                //Used for DEBUG : Log.i("Write", "SUCCESS");
            }
        });

    }

    //***********************************************************************/
    //* the function Decode the tag answer for the GetSystemInfo command
    //* the function fills the values (dsfid / afi / memory size / icRef /..)
    //* in the myApplication class. return true if everything is ok.
    //***********************************************************************/
    public boolean DecodeGetSystemInfoResponse(byte[] GetSystemInfoResponse) {
        DataDevice ma = (DataDevice) getApplication();
        //if the tag has returned a good response
        if (GetSystemInfoResponse[0] == (byte) 0x00 && GetSystemInfoResponse.length >= 12) {
            //DataDevice ma = (DataDevice)getApplication();
            String uidToString = "";
            byte[] uid = new byte[8];
            // change uid format from byteArray to a String
            for (int i = 1; i <= 8; i++) {
                uid[i - 1] = GetSystemInfoResponse[10 - i];
                uidToString += Helper.ConvertHexByteToString(uid[i - 1]);
            }

            //***** TECHNO ******
            ma.setUid(uidToString);
            if (uid[0] == (byte) 0xE0)
                ma.setTechno("ISO 15693");
            else if (uid[0] == (byte) 0xD0)
                ma.setTechno("ISO 14443");
            else
                ma.setTechno("Unknown techno");

            //***** MANUFACTURER ****
            if (uid[1] == (byte) 0x02)
                ma.setManufacturer("STMicroelectronics");
            else if (uid[1] == (byte) 0x04)
                ma.setManufacturer("NXP");
            else if (uid[1] == (byte) 0x07)
                ma.setManufacturer("Texas Instruments");
            else if (uid[1] == (byte) 0x01) //MOTOROLA (updated 20140228)
                ma.setManufacturer("Motorola");
            else if (uid[1] == (byte) 0x03) //HITASHI (updated 20140228)
                ma.setManufacturer("Hitachi");
            else if (uid[1] == (byte) 0x04) //NXP SEMICONDUCTORS
                ma.setManufacturer("NXP");
            else if (uid[1] == (byte) 0x05) //INFINEON TECHNOLOGIES (updated 20140228)
                ma.setManufacturer("Infineon");
            else if (uid[1] == (byte) 0x06) //CYLINC (updated 20140228)
                ma.setManufacturer("Cylinc");
            else if (uid[1] == (byte) 0x07) //TEXAS INSTRUMENTS TAG-IT
                ma.setManufacturer("Texas Instruments");
            else if (uid[1] == (byte) 0x08) //FUJITSU LIMITED (updated 20140228)
                ma.setManufacturer("Fujitsu");
            else if (uid[1] == (byte) 0x09) //MATSUSHITA ELECTRIC INDUSTRIAL (updated 20140228)
                ma.setManufacturer("Matsushita");
            else if (uid[1] == (byte) 0x0A) //NEC (updated 20140228)
                ma.setManufacturer("NEC");
            else if (uid[1] == (byte) 0x0B) //OKI ELECTRIC (updated 20140228)
                ma.setManufacturer("Oki");
            else if (uid[1] == (byte) 0x0C) //TOSHIBA (updated 20140228)
                ma.setManufacturer("Toshiba");
            else if (uid[1] == (byte) 0x0D) //MITSUBISHI ELECTRIC (updated 20140228)
                ma.setManufacturer("Mitsubishi");
            else if (uid[1] == (byte) 0x0E) //SAMSUNG ELECTRONICS (updated 20140228)
                ma.setManufacturer("Samsung");
            else if (uid[1] == (byte) 0x0F) //HUYNDAI ELECTRONICS (updated 20140228)
                ma.setManufacturer("Hyundai");
            else if (uid[1] == (byte) 0x10) //LG SEMICONDUCTORS (updated 20140228)
                ma.setManufacturer("LG");
            else
                ma.setManufacturer("Unknown manufacturer");

            if (uid[1] == (byte) 0x02) {
                //**** PRODUCT NAME *****
                if (uid[2] >= (byte) 0x04 && uid[2] <= (byte) 0x07) {
                    ma.setProductName("LRI512");
                    ma.setMultipleReadSupported(false);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= (byte) 0x14 && uid[2] <= (byte) 0x17) {
                    ma.setProductName("LRI64");
                    ma.setMultipleReadSupported(false);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= (byte) 0x20 && uid[2] <= (byte) 0x23) {
                    ma.setProductName("LRI2K");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= (byte) 0x28 && uid[2] <= (byte) 0x2B) {
                    ma.setProductName("LRIS2K");
                    ma.setMultipleReadSupported(false);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= (byte) 0x2C && uid[2] <= (byte) 0x2F) {
                    ma.setProductName("M24LR64");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else if (uid[2] >= (byte) 0x40 && uid[2] <= (byte) 0x43) {
                    ma.setProductName("LRI1K");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= (byte) 0x44 && uid[2] <= (byte) 0x47) {
                    ma.setProductName("LRIS64K");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else if (uid[2] >= (byte) 0x48 && uid[2] <= (byte) 0x4B) {
                    ma.setProductName("M24LR01E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= (byte) 0x4C && uid[2] <= (byte) 0x4F) {
                    ma.setProductName("M24LR16E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (ma.isBasedOnTwoBytesAddress() == false)
                        return false;
                } else if (uid[2] >= (byte) 0x50 && uid[2] <= (byte) 0x53) {
                    ma.setProductName("M24LR02E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= (byte) 0x54 && uid[2] <= (byte) 0x57) {
                    ma.setProductName("M24LR32E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (ma.isBasedOnTwoBytesAddress() == false)
                        return false;
                } else if (uid[2] >= (byte) 0x58 && uid[2] <= (byte) 0x5B) {
                    ma.setProductName("M24LR04E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else if (uid[2] >= (byte) 0x5C && uid[2] <= (byte) 0x5F) {
                    ma.setProductName("M24LR64E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (ma.isBasedOnTwoBytesAddress() == false)
                        return false;
                } else if (uid[2] >= (byte) 0x60 && uid[2] <= (byte) 0x63) {
                    ma.setProductName("M24LR08E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else if (uid[2] >= (byte) 0x64 && uid[2] <= (byte) 0x67) {
                    ma.setProductName("M24LR128E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (ma.isBasedOnTwoBytesAddress() == false)
                        return false;
                } else if (uid[2] >= (byte) 0x6C && uid[2] <= (byte) 0x6F) {
                    ma.setProductName("M24LR256E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (ma.isBasedOnTwoBytesAddress() == false)
                        return false;
                } else if (uid[2] >= (byte) 0xF8 && uid[2] <= (byte) 0xFB) {
                    ma.setProductName("detected product");
                    ma.setBasedOnTwoBytesAddress(true);
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else {
                    ma.setProductName("Unknown product");
                    ma.setBasedOnTwoBytesAddress(false);
                    ma.setMultipleReadSupported(false);
                    ma.setMemoryExceed2048bytesSize(false);
                }

                //*** DSFID ***
                ma.setDsfid(Helper.ConvertHexByteToString(GetSystemInfoResponse[10]));

                //*** AFI ***
                ma.setAfi(Helper.ConvertHexByteToString(GetSystemInfoResponse[11]));

                //*** MEMORY SIZE ***
                if (ma.isBasedOnTwoBytesAddress()) {
                    String temp = new String();
                    temp += Helper.ConvertHexByteToString(GetSystemInfoResponse[13]);
                    temp += Helper.ConvertHexByteToString(GetSystemInfoResponse[12]);
                    ma.setMemorySize(temp);
                } else
                    ma.setMemorySize(Helper.ConvertHexByteToString(GetSystemInfoResponse[12]));

                //*** BLOCK SIZE ***
                if (ma.isBasedOnTwoBytesAddress())
                    ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
                else
                    ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[13]));

                //*** IC REFERENCE ***
                if (ma.isBasedOnTwoBytesAddress())
                    ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[15]));
                else
                    ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
            } else {
                ma.setProductName("Unknown product");
                ma.setBasedOnTwoBytesAddress(false);
                ma.setMultipleReadSupported(false);
                ma.setMemoryExceed2048bytesSize(false);
                //ma.setAfi("00 ");
                ma.setAfi(Helper.ConvertHexByteToString(GetSystemInfoResponse[11]));                //changed 22-10-2014
                //ma.setDsfid("00 ");
                ma.setDsfid(Helper.ConvertHexByteToString(GetSystemInfoResponse[10]));                //changed 22-10-2014
                //ma.setMemorySize("FF ");
                ma.setMemorySize(Helper.ConvertHexByteToString(GetSystemInfoResponse[12]));        //changed 22-10-2014
                //ma.setBlockSize("03 ");
                ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[13]));            //changed 22-10-2014
                //ma.setIcReference("00 ");
                ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));        //changed 22-10-2014
            }

            return true;
        }

        // in case of Inventory OK and Get System Info HS
        else if (ma.getTechno() == "ISO 15693") {
            ma.setProductName("Unknown product");
            ma.setBasedOnTwoBytesAddress(false);
            ma.setMultipleReadSupported(false);
            ma.setMemoryExceed2048bytesSize(false);
            ma.setAfi("00 ");
            ma.setDsfid("00 ");
            ma.setMemorySize("3F ");            //changed 22-10-2014
            ma.setBlockSize("03 ");
            ma.setIcReference("00 ");
            return true;
        }

        //if the tag has returned an error code
        else
            return false;

    }

    private class StartSaveInFileTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(FileManagement.this);

        // can use UI thread here
        protected void onPreExecute() {

            FileError = false;

            this.dialog.setMessage("Please, place your phone near the card");
            this.dialog.show();

            DataDevice dataDevice = (DataDevice) getApplication();

            GetSystemInfoAnswer = NFCCommand.SendGetSystemInfoCommandCustom(dataDevice.getCurrentTag(), dataDevice);

            if (DecodeGetSystemInfoResponse(GetSystemInfoAnswer)) {

                int MemorySizeBlocksNumber = (Helper.ConvertStringToInt((dataDevice.getMemorySize().replace(" ", ""))) + 1);

                addressStart = Helper.ConvertIntTo2bytesHexaFormat(0x00);

                numberOfBlockToRead = Helper.ConvertIntTo2bytesHexaFormat(MemorySizeBlocksNumber);

                this.dialog.setMessage("Please, keep your phone close to the tag");
                this.dialog.show();


            }
        }

        // automatically done on worker thread (separate from UI thread)
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            cpt = 0;
            DataDevice dataDevice = (DataDevice) getApplication();

            String strSaveFileNameTest = textSaveFileName.getText().toString();

            ReadMultipleBlockAnswer = null;
            if (DecodeGetSystemInfoResponse(GetSystemInfoAnswer) && strSaveFileNameTest.length() > 0) {

                //if(Helper.Convert2bytesHexaFormatToInt(numberOfBlockToRead) <=1) //ex: 1 byte to be read
                //{
                //	while ((ReadMultipleBlockAnswer == null || ReadMultipleBlockAnswer[0] == 1) && cpt <= 10 )
                //	{
                //		Log.i("ScanRead", "Dans le read SINGLE le cpt est -----> " + String.valueOf(cpt));
                //		ReadMultipleBlockAnswer = NFCCommand.SendReadSingleBlockCommand(dataDevice.getCurrentTag(),addressStart, dataDevice);
                //		cpt ++;
                //	}
                //	cpt = 0;
                //}
                //else if(ma.isMultipleReadSupported() == false) //ex: LRIS2K
                if (dataDevice.isMultipleReadSupported() == false || Helper.Convert2bytesHexaFormatToInt(numberOfBlockToRead) <= 1) //ex: LRIS2K
                {
                    while ((ReadMultipleBlockAnswer == null || ReadMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                        //Used for DEBUG : Log.i("ScanRead", "Dans le several read single block le cpt est -----> " + String.valueOf(cpt));
                        ReadMultipleBlockAnswer = NFCCommand.Send_several_ReadSingleBlockCommands_NbBlocks(dataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, dataDevice);
                        cpt++;
                    }
                    cpt = 0;
                } else if (Helper.Convert2bytesHexaFormatToInt(numberOfBlockToRead) < 32) {
                    while ((ReadMultipleBlockAnswer == null || ReadMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                        //Used for DEBUG : Log.i("ScanRead", "Dan le read MULTIPLE 1 le cpt est -----> " + String.valueOf(cpt));
                        ReadMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom(dataDevice.getCurrentTag(), addressStart, numberOfBlockToRead[1], dataDevice);
                        cpt++;
                    }
                    cpt = 0;
                } else {
                    while ((ReadMultipleBlockAnswer == null || ReadMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                        //Used for DEBUG : Log.i("ScanRead", "Dans le read MULTIPLE 2 le cpt est -----> " + String.valueOf(cpt));
                        ReadMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom2(dataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, dataDevice);
                        cpt++;
                    }
                    cpt = 0;
                }


                int MemorySizeBytes = (Helper.ConvertStringToInt((dataDevice.getMemorySize().replace(" ", ""))) + 1) * 4;

                if (ReadMultipleBlockAnswer[0] == (byte) 0x00 && ReadMultipleBlockAnswer.length > MemorySizeBytes) {
                    byte DataRead[] = new byte[MemorySizeBytes];
                    int i = 1;
                    while (i <= MemorySizeBytes) {
                        DataRead[i - 1] = ReadMultipleBlockAnswer[i];
                        i++;
                    }

                    try {

                        File folder = new File(Environment.getExternalStorageDirectory(), "NfcV-Reader");
                        if (!folder.exists())
                            folder.mkdir();

                        String strSaveFileName = textSaveFileName.getText().toString();

                        FileOutputStream fileIS;
                        fileIS = new FileOutputStream(Environment.getExternalStorageDirectory() + "/NfcV-Reader/" + strSaveFileName.replace(" ", ""));

                        //if (fileSize > MemorySizeBytes)
                        //{
                        //	Toast toast = Toast.makeText(getApplicationContext(), "File too big versus Memory size", Toast.LENGTH_SHORT);
                        //	toast.show();
                        //	//finish();
                        //}
                        //else if (fileSize == 0 )
                        //{
                        //	Toast toast = Toast.makeText(getApplicationContext(), "File empty", Toast.LENGTH_SHORT);
                        //	toast.show();
                        //	//finish();
                        //}
                        fileIS.write(DataRead);

                    } catch (IOException e) {
                        FileError = true;
                        Toast toast = Toast.makeText(getApplicationContext(), "File cannot be created", Toast.LENGTH_SHORT);
                        toast.show();
                        //finish();
                    }

                }
            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (textSaveFileName.getText().toString().length() == 0)
                Toast.makeText(getApplicationContext(), "File name error", Toast.LENGTH_SHORT).show();
            else if (FileError == false) {
                if (ReadMultipleBlockAnswer == null) {
                    Toast.makeText(getApplicationContext(), "ERROR File transfer (No tag answer)", Toast.LENGTH_SHORT).show();
                } else if (ReadMultipleBlockAnswer[0] == (byte) 0x01) {
                    Toast.makeText(getApplicationContext(), "ERROR File transfer", Toast.LENGTH_SHORT).show();
                } else if (ReadMultipleBlockAnswer[0] == (byte) 0xFF) {
                    Toast.makeText(getApplicationContext(), "ERROR File transfer", Toast.LENGTH_SHORT).show();
                } else if (ReadMultipleBlockAnswer[0] == (byte) 0xAF) {
                    Toast.makeText(getApplicationContext(), "ERROR Reading memory", Toast.LENGTH_SHORT).show();
                } else if (ReadMultipleBlockAnswer[0] == (byte) 0x00) {
                    Toast.makeText(getApplicationContext(), "File transfer Sucessfull", Toast.LENGTH_SHORT).show();
                    //finish();
                } else {
                    Toast.makeText(getApplicationContext(), "File transfer ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class StartLoadFromFileTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(FileManagement.this);

        // can use UI thread here
        protected void onPreExecute() {

            this.dialog.setMessage("Please, keep your phone near the card ... write on going");
            this.dialog.show();

            DataDevice dataDevice = (DataDevice) getApplication();

            GetSystemInfoAnswer = NFCCommand.SendGetSystemInfoCommandCustom(dataDevice.getCurrentTag(), dataDevice);

            if (DecodeGetSystemInfoResponse(GetSystemInfoAnswer)) {

                int MemorySizeBytes = (Helper.ConvertStringToInt((dataDevice.getMemorySize().replace(" ", ""))) + 1) * 4;
                FileError = false;

                //read binary file
                try {
                    File folder = new File(Environment.getExternalStorageDirectory(), "NfcV-Reader");
                    if (!folder.exists())
                        folder.mkdir();

                    String strFileName = textLoadFileName.getText().toString();

                    File f = new File(Environment.getExternalStorageDirectory() + "/NfcV-Reader/" + strFileName.replace(" ", ""));
                    FileInputStream fileIS = new FileInputStream(f);
                    BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
                    int fileSize = (int) f.length();

                    if ((fileSize % 4) > 0) {
                        Toast toast = Toast.makeText(getApplicationContext(), "File format error : multiple of 4 bytes need (4 bytes per block)", Toast.LENGTH_SHORT);
                        toast.show();
                        FileError = true;
                        //finish();
                    } else if (fileSize > MemorySizeBytes) {
                        Toast toast = Toast.makeText(getApplicationContext(), "File too big for your memory Tag", Toast.LENGTH_SHORT);
                        toast.show();
                        FileError = true;
                        //finish();
                    } else if (fileSize == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(), "File empty", Toast.LENGTH_SHORT);
                        toast.show();
                        FileError = true;
                        //finish();
                    } else {
                        bufferFile = new byte[MemorySizeBytes];
                        fileIS.read(bufferFile);
                        blocksToWrite = (int) fileSize / 4;
                    }
                } catch (IOException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "File not found or not formated as binary file", Toast.LENGTH_SHORT);
                    toast.show();
                    FileError = true;
                    //finish();
                }

            }
        }

        // automatically done on worker thread (separate from UI thread)
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            cpt = 0;
            DataDevice dataDevice = (DataDevice) getApplication();

            if (FileError == false) {
                WriteSingleBlockAnswer = null;
                if (DecodeGetSystemInfoResponse(GetSystemInfoAnswer)) {
                    int ResultWriteAnswer = 0;
                    for (int iAddressStart = 0; iAddressStart < blocksToWrite; iAddressStart++) {
                        addressStart = Helper.ConvertIntTo2bytesHexaFormat(iAddressStart);
                        dataToWrite[0] = bufferFile[iAddressStart * 4];
                        dataToWrite[1] = bufferFile[iAddressStart * 4 + 1];
                        dataToWrite[2] = bufferFile[iAddressStart * 4 + 2];
                        dataToWrite[3] = bufferFile[iAddressStart * 4 + 3];
                        cpt = 0;
                        WriteSingleBlockAnswer = null;
                        while ((WriteSingleBlockAnswer == null || WriteSingleBlockAnswer[0] == 1) && cpt <= 10) {
                            WriteSingleBlockAnswer = NFCCommand.SendWriteSingleBlockCommand(dataDevice.getCurrentTag(), addressStart, dataToWrite, dataDevice);
                            cpt++;
                        }
                        if (WriteSingleBlockAnswer[0] != (byte) 0x00) {
                            ResultWriteAnswer = ResultWriteAnswer + 1;
                            WriteSingleBlockAnswer[0] = (byte) 0xE1;
                            return null;
                        }
                    }
                    if (ResultWriteAnswer > 0)
                        WriteSingleBlockAnswer[0] = (byte) 0xFF;
                    else
                        WriteSingleBlockAnswer[0] = (byte) 0x00;
                }
            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();

            if (FileError == false) {
                if (WriteSingleBlockAnswer == null) {
                    Toast.makeText(getApplicationContext(), "ERROR File Transfer (No tag answer) ", Toast.LENGTH_SHORT).show();
                } else if (WriteSingleBlockAnswer[0] == (byte) 0x01) {
                    Toast.makeText(getApplicationContext(), "ERROR File Transfer ", Toast.LENGTH_SHORT).show();
                } else if (WriteSingleBlockAnswer[0] == (byte) 0xFF) {
                    Toast.makeText(getApplicationContext(), "ERROR File Transfer ", Toast.LENGTH_SHORT).show();
                } else if (WriteSingleBlockAnswer[0] == (byte) 0xE1) {
                    Toast.makeText(getApplicationContext(), "ERROR File Transfer process stopped", Toast.LENGTH_SHORT).show();
                } else if (WriteSingleBlockAnswer[0] == (byte) 0x00) {
                    Toast.makeText(getApplicationContext(), "Write Sucessfull ", Toast.LENGTH_SHORT).show();
                    //finish();
                } else {
                    Toast.makeText(getApplicationContext(), "File Transfer ERROR ", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

}