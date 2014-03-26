package com.android.smsscheduler;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendSmsService extends IntentService {

	String phoneNo = "8800839680";
	String smsStr = "This is msg from application which is  scheduled ";
	String TAG = "AlaramReceiver";

	public SendSmsService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		try {
			smsStr = intent.getExtras().getString("sms_text");
			Log.i(TAG, "" + smsStr);
			SmsManager smsMgr = SmsManager.getDefault();
			smsMgr.sendTextMessage(phoneNo, null, smsStr, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
