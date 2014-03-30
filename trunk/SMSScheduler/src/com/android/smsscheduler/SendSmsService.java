package com.android.smsscheduler;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class SendSmsService extends IntentService 
{

	String phoneNo = "9711082272";
	String smsStr = "This is msg from application which is  scheduled ";
	static String TAG = "AlaramReceiver";

	// km5Aw8he9sj6

	public SendSmsService() 
	{
		super(TAG);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{

		try 
		{
			smsStr = intent.getExtras().getString(Constants.SMS_STR_KEY);
			phoneNo = intent.getExtras().getString(Constants.PHONE_NO_KEY);
			Log.i(TAG, "" + smsStr);
			Log.i(TAG, "" + phoneNo);
			SmsManager smsMgr = SmsManager.getDefault();
			smsMgr.sendTextMessage(phoneNo, null, smsStr, null, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

}
