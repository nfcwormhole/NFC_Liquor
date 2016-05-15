package com.nfcwormhole.liquor;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.nfc.apps.DataDevice;
import com.nfc.apps.Helper;
import com.nfc.apps.NDEFMessages;
import com.nfc.apps.NFCCommand;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private DataDevice ma = (DataDevice) getApplication();
    private byte[] fullNdefMessage = null;
    private long cpt = 0;
    private long numberOfBlockToRead = 0;
    private String sNDEFMessage = "nothing";


    byte[] GetSystemInfoAnswer = null;
    private byte[] WriteStatus;
    private byte[] NdefTextMessageToWrite;
    private String NDEFTextMessage;

    Button button;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.button2)
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mFilters = new IntentFilter[]{ndef,};
        mTechLists = new String[][]{new String[]{android.nfc.tech.NfcV.class.getName()}};
        ma = (DataDevice) getApplication();

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "read");
                new StartReadTask().execute();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "write");
                String string = "";
                if (checkBox.isChecked()) {
                    string += "1";
                } else {
                    string += "0";
                }
                string += "" + (char) 0x0D + (char) 0x0A;
                string += editText.getText().toString();
                string += "" + (char) 0x0D + (char) 0x0A;
                string += editText2.getText().toString();
                string += "" + (char) 0x0D + (char) 0x0A;
                string += editText3.getText().toString();
                Log.e(TAG, string);
                new StartWriteTask().execute(string);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        DataDevice ma = (DataDevice) getApplication();
        ma.setCurrentTag(tagFromIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    protected void onPause() {
        cpt = 500;
        super.onPause();
    }

    private class StartReadTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Please, keep your phone close to the tag.");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            DataDevice dataDevice = (DataDevice) getApplication();

            boolean msgIsEmpty = false;
            fullNdefMessage = null;

            //Check Tag detected (memory size, nb byte address ...) by reading System Info
            byte[] GetSystemInfoAnswer = NFCCommand.SendGetSystemInfoCommandCustom(dataDevice.getCurrentTag(), dataDevice);

            if (DecodeGetSystemInfoResponse(GetSystemInfoAnswer)) {

                byte[] resultBlock0 = new byte[4];
                byte[] resultBlock1 = new byte[12];
                cpt = 0;

                resultBlock0 = null;
                //while ((resultBlock0 == null || resultBlock0[0] == 1)&& cpt<1500)
                while ((resultBlock0 == null || resultBlock0[0] == 1) && cpt < 10) {
                    resultBlock0 = NFCCommand.SendReadSingleBlockCommand(dataDevice.getCurrentTag(), new byte[]{0x00, 0x00}, dataDevice);
                    cpt++;
                    //Used for DEBUG : Log.v("CPT ", " CPT Read Block 0 ===> " + String.valueOf(cpt));
                }

                //NDEF format : 4th first bytes of NDEF header
                //CC0 = E1h  =  NDEF message is present
                //CC1 = bit7-6 : Major version
                //		 bit5-4 : Minor version
                //		 bit3-2 : Read access (00:free access)
                //		 bit1-0 : Write access (00:free access / 10:write need password / 11:no write access)
                //CC2 = Memory size of data field (CC2 *8)
                //CC3 = bit7-3 : rfu
                //		 bit2 : IC memory exceed 2040 bytes
                //		 bit1 : rfu
                //		 bit0 : 1=support Multiple read / read single only


                //if(resultBlock0[0]==(byte)0x00 && resultBlock0[1]==(byte)0xE1 && resultBlock0[2]==(byte)0x40)
                if (resultBlock0[0] == (byte) 0x00 && resultBlock0[1] == (byte) 0xE1) {
                    //NDEF TAG Format valid
                    cpt = 0;
                    resultBlock1 = null;

                    boolean boolMultipleReadCC3 = false;
                    //CC3 bit0 analysis for Read Single /Read Multiple option
                    if ((resultBlock0[4] & (byte) 0x01) == (byte) 0x01)
                        boolMultipleReadCC3 = true;
                    else
                        boolMultipleReadCC3 = false;

                    //while ((resultBlock1 == null || resultBlock1[0] == 1)&& cpt<1500)
                    while ((resultBlock1 == null || resultBlock1[0] == 1) && cpt < 10) {
                        //resultBlock1 = NFCCommand.SendReadSingleBlockCommand(dataDevice.getCurrentTag(), new byte[]{0x00,0x01}, dataDevice);
                        resultBlock1 = NFCCommand.Send_several_ReadSingleBlockCommands(dataDevice.getCurrentTag(), new byte[]{0x00, 0x01}, new byte[]{0x00, 0x0C}, dataDevice);
                        cpt++;
                        //Used for DEBUG : Log.v("CPT ", " CPT Read Block 0 ===> " + String.valueOf(cpt));
                    }

                    byte[] resultBlockX = new byte[2];
                    int resultBlockY = 0;

                    int nbBytesToRead = 0;
                    int numberOfBytesToRead = 0;

                    //"00" = "Used to Padding"
                    //"01" = "Defines details of the lock bits"
                    //"02" = "Identify reserved memory areas"
                    //"03" = "Contains NDEF message"
                    //"FD" = "Tag proprietary information"
                    //"FE" = "Last TLV block in the data area"

                    if (resultBlock1[0] == (byte) 0x00 && resultBlock1[1] == (byte) 0x03)  //"03" = "Contains NDEF message"
                    {
                        //LField : if byte1=00 -> message is empty or not well written
                        //Lfield : if byte1=FF -> Lfield coded on 2 bytes (byte2byte3) else byte1
                        if (resultBlock1[2] == (byte) 0x00) {
                            msgIsEmpty = true;
                        } else if (resultBlock1[2] == (byte) 0xFF) {
                            resultBlockX[0] += (byte) resultBlock1[3];
                            resultBlockX[1] += (byte) resultBlock1[4];
                            numberOfBytesToRead = (Helper.Convert2bytesHexaFormatToInt(resultBlockX));
                            numberOfBytesToRead += 12; //10;
                            if (numberOfBytesToRead % 4 > 0)
                                numberOfBytesToRead += (4 - (numberOfBytesToRead % 4)) + 4;
                            resultBlockX = Helper.ConvertIntTo2bytesHexaFormat(numberOfBytesToRead);
                        } else {
                            resultBlockX[0] += (byte) 0x00;
                            resultBlockX[1] += (byte) resultBlock1[2];
                            numberOfBytesToRead = (Helper.Convert2bytesHexaFormatToInt(resultBlockX));
                            numberOfBytesToRead += 12; //8;
                            if (numberOfBytesToRead % 4 > 0)
                                numberOfBytesToRead += (4 - (numberOfBytesToRead % 4)) + 4;
                            resultBlockX = Helper.ConvertIntTo2bytesHexaFormat(numberOfBytesToRead);
                        }
                    } else if (resultBlock1[0] == (byte) 0x00 && resultBlock1[1] == (byte) 0x01)  //"01" = "Defines details of the lock bits"
                    {
                        //resultBlock1[2]==(byte)0x03) length lock bits data
                        //resultBlock1[3]==(byte)0x03) lock bits data 1
                        //resultBlock1[4]==(byte)0x03) lock bits data 2
                        //resultBlock1[5]==(byte)0x03) lock bits data 3

                        //LField : if byte1=00 -> message is empty or not well written
                        //Lfield : if byte1=FF -> Lfield coded on 2 bytes (byte2byte3) else byte1
                        if (resultBlock1[6] == (byte) 0x00) {
                            msgIsEmpty = true;
                        } else if (resultBlock1[6] == (byte) 0xFF) {
                            resultBlockX[0] += (byte) resultBlock1[7];
                            resultBlockX[1] += (byte) resultBlock1[8];
                            numberOfBytesToRead = (Helper.Convert2bytesHexaFormatToInt(resultBlockX));
                            numberOfBytesToRead += 12; //10;
                            if (numberOfBytesToRead % 4 > 0)
                                numberOfBytesToRead += (4 - (numberOfBytesToRead % 4)) + 4;
                            resultBlockX = Helper.ConvertIntTo2bytesHexaFormat(numberOfBytesToRead);
                        } else {
                            resultBlockX[0] += (byte) 0x00;
                            resultBlockX[1] += (byte) resultBlock1[7];
                            numberOfBytesToRead = (Helper.Convert2bytesHexaFormatToInt(resultBlockX));
                            numberOfBytesToRead += 12; //8
                            if (numberOfBytesToRead % 4 > 0)
                                numberOfBytesToRead += (4 - (numberOfBytesToRead % 4)) + 4;
                            resultBlockX = Helper.ConvertIntTo2bytesHexaFormat(numberOfBytesToRead);
                        }
                    }

                    //bNumberOfBlock = Helper.ConvertIntTo2bytesHexaFormat(numberOfBlockToRead);

                    ////Lfield : if byte1=FF -> Lfield coded on 2 bytes (byte2byte3) else byte1
                    //if(resultBlock1[2]==(byte)0xFF)
                    //	 numberOfBlockToRead = ((((byte)resultBlock1[3] << 8) | (byte)resultBlock1[4]) / 4) + 10;
                    //else
                    //	 numberOfBlockToRead = (resultBlock1[2]/4)+10;

                    //bNumberOfBlock = Helper.ConvertIntTo2bytesHexaFormat(numberOfBlockToRead);

                    if (msgIsEmpty == false) {
                        cpt = 0;
                        //if(numberOfBlockToRead <32 || boolMultipleReadCC3==false)

                        if (boolMultipleReadCC3 == false || dataDevice.isMultipleReadSupported() == false) //ex: LRIS2K no Multiple read allowed
                        {
                            while ((fullNdefMessage == null || fullNdefMessage[0] == 1) && cpt < 10 && numberOfBytesToRead != 0) {
                                //fullNdefMessage = NFCCommand.SendReadMultipleBlockCommandCustom(dataDevice.getCurrentTag(),new byte[]{0x00,0x00}, bNumberOfBlock[1], dataDevice);
                                //fullNdefMessage = NFCCommand.Send_several_ReadSingleBlockCommands(dataDevice.getCurrentTag(),new byte[]{0x00,0x00}, nbBytesToRead, dataDevice);
                                fullNdefMessage = NFCCommand.Send_several_ReadSingleBlockCommands(dataDevice.getCurrentTag(), new byte[]{0x00, 0x00}, resultBlockX, dataDevice);
                                cpt++;
                            }
                        } else {
                            while ((fullNdefMessage == null || fullNdefMessage[0] == 1) && cpt < 10 && numberOfBytesToRead != 0) {
                                //fullNdefMessage = NFCCommand.SendReadMultipleBlockCommandCustom2(dataDevice.getCurrentTag(),new byte[]{0x00,0x00}, bNumberOfBlock, dataDevice);
                                fullNdefMessage = NFCCommand.Send_several_ReadMultipleBlockCommands(dataDevice.getCurrentTag(), resultBlockX, dataDevice);
                                //Used for DEBUG : Log.i("NDEF Text", "fullNdefMessage **** : " + Helper.ConvertHexByteArrayToString(fullNdefMessage));
                                cpt++;
                                //Used for DEBUG : Log.i("CPT ", "***** " + String.valueOf(cpt));
                            }

                        }
                    } else {
                        fullNdefMessage = new byte[1];
                        fullNdefMessage[0] = (byte) 0x00;
                    }
                } else {
                    //Not NDEF TAG Format
                    numberOfBlockToRead = 0;
                }
            }
            //else

            return null;
        }

        protected void onPostExecute(final Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();
            if (fullNdefMessage != null) {
                Log.e(TAG, "fullNdefMessage is not equal null");
//                for (byte b : fullNdefMessage) {
//                    Log.d("sam test", "b=" + Integer.toHexString(b));
//                }
            }
            if (fullNdefMessage == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "No NDEF message", Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else if (fullNdefMessage.length == 1 && fullNdefMessage[0] == (byte) 0x00) {
                Toast toast = Toast.makeText(getApplicationContext(), "NDEF field equal to 0", Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else if (fullNdefMessage[0] == (byte) 0xAF) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error during Read", Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else if (fullNdefMessage.length > 1) {
                byte[] NdefMessage = new byte[fullNdefMessage.length];
                for (int i = 1; i < fullNdefMessage.length; i++) {
                    NdefMessage[i - 1] = fullNdefMessage[i];
                }
                sNDEFMessage = NDEFMessages.ConvertNDEF_ByteArrayToString_Adapted(NdefMessage);

                if (!sNDEFMessage.equals("No Ndef Message Found")) {
//                    Intent i = new Intent(NDEFMenu.this, NDEFRead.class);
//                    i.putExtra(NDEFRead.EXTRA_1, sNDEFMessage);
//                    startActivity(i);
//                    TODO: add display
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    i.putExtra(Main2Activity.EXTRA_1, sNDEFMessage);
                    startActivity(i);
//                    Log.e(TAG, sNDEFMessage.replaceFirst("type:TEXT", ""));
                    String[] strings = sNDEFMessage.split("\\r\\n");
                    for (String s : strings) {
                        Log.e(TAG, s);
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Tag ?", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else

            {
                sNDEFMessage = "No NDEF message";
            }
        }
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

    private class StartWriteTask extends AsyncTask<String, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Programming...");
            this.dialog.show();
        }
        // automatically done on worker thread (separate from UI thread)

        @Override
        protected Void doInBackground(String... params) {
            DataDevice dataDevice = (DataDevice) getApplication();
            ma = (DataDevice) getApplication();

            GetSystemInfoAnswer = NFCCommand.SendGetSystemInfoCommandCustom(dataDevice.getCurrentTag(), dataDevice);

            if (DecodeGetSystemInfoResponse(GetSystemInfoAnswer)) {
                NDEFTextMessage = params[0];
//                NDEFTextMessage = ndefTextEdit.getText().toString();
//                NdefTextMessageToWrite = NDEFMessages.ConvertStringToNDEF_Text_ByteArray(NDEFTextMessage, dataDevice);
                NdefTextMessageToWrite = NDEFMessages.ConvertStringToNDEF_Text_ByteArray(NDEFTextMessage, dataDevice);

                if (NdefTextMessageToWrite != null) {

                    //1st : store TLF 4 bytes begining + write 0x00 to length byte(s)
                    byte[] TLV2write = new byte[4];
                    byte[] TLV2write_zeroLength = new byte[4];
                    for (int i = 0; i < 4; i++)
                        TLV2write[i] = NdefTextMessageToWrite[i + 4];
                    if (TLV2write[1] == 0xFF) {
                        TLV2write_zeroLength[0] = TLV2write[0];
                        TLV2write_zeroLength[1] = 0x00;
                        TLV2write_zeroLength[2] = 0x00;
                        TLV2write_zeroLength[3] = 0x00;
                    } else {
                        TLV2write_zeroLength[0] = TLV2write[0];
                        TLV2write_zeroLength[1] = 0x00;
                        TLV2write_zeroLength[2] = TLV2write[2];
                        TLV2write_zeroLength[3] = TLV2write[3];
                    }
                    cpt = 0;
                    WriteStatus = null;
                    while ((WriteStatus == null || WriteStatus[0] == 1) && cpt < 10) {
                        //Used for DEBUG : Log.i("NDEFWrite", "Dan le WRITE MULTIPLE le cpt est -----> " + String.valueOf(cpt));
                        WriteStatus = NFCCommand.SendWriteMultipleBlockCommand(ma.getCurrentTag(), new byte[]{0x00, 0x01}, TLV2write_zeroLength, dataDevice);
                        cpt++;
                    }

                    if (WriteStatus[0] == 0x00) {
                        //2nd Write CCfield if o write error
                        byte[] CCfield2write = new byte[4];
                        for (int i = 0; i < 4; i++)
                            CCfield2write[i] = NdefTextMessageToWrite[i];
                        cpt = 0;
                        WriteStatus = null;
                        while ((WriteStatus == null || WriteStatus[0] == 1) && cpt < 10) {
                            //Used for DEBUG : Log.i("NDEFWrite", "Dan le WRITE MULTIPLE le cpt est -----> " + String.valueOf(cpt));
                            WriteStatus = NFCCommand.SendWriteMultipleBlockCommand(ma.getCurrentTag(), new byte[]{0x00, 0x00}, CCfield2write, dataDevice);
                            cpt++;
                        }

                        if (WriteStatus[0] == 0x00) {
                            //3rd write rest of the NDEF message if no previous errors
                            byte[] restNDEFmsg2write = new byte[NdefTextMessageToWrite.length];
                            for (int i = 0; i < NdefTextMessageToWrite.length - 8; i++)
                                restNDEFmsg2write[i] = NdefTextMessageToWrite[i + 8];

                            cpt = 0;
                            WriteStatus = null;
                            while ((WriteStatus == null || WriteStatus[0] == 1) && cpt < 10) {
                                //Used for DEBUG : Log.i("NDEFWrite", "Dan le WRITE MULTIPLE le cpt est -----> " + String.valueOf(cpt));
                                WriteStatus = NFCCommand.SendWriteMultipleBlockCommand(ma.getCurrentTag(), new byte[]{0x00, 0x02}, restNDEFmsg2write, dataDevice);
                                cpt++;
                            }
                            if (WriteStatus[0] == 0x00) {
                                //4rth write store TLF 4 bytes begining with length byte(s)
                                cpt = 0;
                                WriteStatus = null;
                                while ((WriteStatus == null || WriteStatus[0] == 1) && cpt < 10) {
                                    //Used for DEBUG : Log.i("NDEFWrite", "Dan le WRITE MULTIPLE le cpt est -----> " + String.valueOf(cpt));
                                    WriteStatus = NFCCommand.SendWriteMultipleBlockCommand(ma.getCurrentTag(), new byte[]{0x00, 0x01}, TLV2write, dataDevice);
                                    cpt++;
                                }
                            }
                        }
                    }
                } else {
                    WriteStatus = new byte[]{(byte) 0x02};//error code for message too long for memory
                }
            }
            return null;

        }

        // can use UI thread here
        protected void onPostExecute(final Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();
            if (DecodeGetSystemInfoResponse(GetSystemInfoAnswer)) {
                if (WriteStatus == null) {
                    Toast.makeText(ma.getApplicationContext(), "Error during write programming please retry", Toast.LENGTH_SHORT).show();
                } else if (WriteStatus[0] == (byte) 0x00) {
                    Toast.makeText(ma.getApplicationContext(), "Write OK", Toast.LENGTH_SHORT).show();
//                    finish();
                } else if (WriteStatus[0] == (byte) 0x02) {
                    Toast.makeText(ma.getApplicationContext(), "The message you want to write is too long for the memory size", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ma.getApplicationContext(), "Error during write programming please retry", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ma.getApplicationContext(), "No tag detected", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
