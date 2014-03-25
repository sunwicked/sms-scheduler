package com.android.smsscheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class AlaramReceiver extends BroadcastReceiver
{
	String phoneNo = "8800839680";
	String smsStr = "This is msg from application which is  scheduled " ;
	String TAG = "AlaramReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			smsStr = intent.getExtras().getString("sms_text");
			Log.i(TAG, ""+smsStr);
			SmsManager smsMgr = SmsManager.getDefault() ;
			smsMgr.sendTextMessage(phoneNo, null, smsStr, null, null);
			Toast.makeText(context, "SMS Sent!",
					Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			Toast.makeText(context,
					"SMS faild, please try again later!",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

}
