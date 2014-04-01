package com.android.smsscheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/*
 * This class receives the broadcast for our set alarms,
 * it is responsible for starting the SendSmsService. We are 
 * starting a service instead of performing the action of sending 
 * sms here. The reason is "upon returning from onReceive() the 
 * system will consider its process to be empty and aggressively
 * kill it so that resources are available for other more important processes."
 * Component may get destroyed while we are doing some background operation.
 * Refer:http://developer.android.com/reference/android/content/BroadcastReceiver.html
 * 
 * */

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String smsText = intent.getExtras()
					.getString(Constants.SMS_STR_KEY);
			String phoneNo = intent.getExtras().getString(
					Constants.PHONE_NO_KEY);

			Intent msgIntent = new Intent(context, SendSmsService.class);
			Bundle b = new Bundle();
			b.putString(Constants.SMS_STR_KEY, smsText);
			b.putString(Constants.PHONE_NO_KEY, phoneNo);
			msgIntent.putExtras(b);

			context.startService(msgIntent);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("Receiver", "Exception in receiver.");
		}
	}

}
