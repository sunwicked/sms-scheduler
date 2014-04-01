package com.android.smsscheduler;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/*
 * This class is responsible for sending sms.Notice we have extended Intentservice, 
 * the advatages of using intent service is that we do not need to worry about 
 * starting and stopping a running service it will shut itself after finishing the
 * assigned work. Moreover we need not worry about starting a background thread as 
 * Instentservice starts a worker thread to perform the action 
 * 
 * */

public class SendSmsService extends IntentService {

	String phoneNo = "0000000000";
	String smsStr = "This is msg from application which is  scheduled ";
	static String TAG = "SendSmsService";

	// km5Aw8he9sj6

	public SendSmsService() {
		super(TAG);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		try {
			smsStr = intent.getExtras().getString(Constants.SMS_STR_KEY);
			phoneNo = intent.getExtras().getString(Constants.PHONE_NO_KEY);
			Log.i(TAG, "" + smsStr);
			Log.i(TAG, "" + phoneNo);
			SmsManager smsMgr = SmsManager.getDefault();
			smsMgr.sendTextMessage(phoneNo, null, smsStr, null, null);
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
