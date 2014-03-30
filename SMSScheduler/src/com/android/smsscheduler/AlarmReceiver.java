package com.android.smsscheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver 
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		try
		{
			String smsText = intent.getExtras().getString(Constants.SMS_STR_KEY);
			String phoneNo = intent.getExtras().getString(Constants.PHONE_NO_KEY);

			Intent msgIntent = new Intent(context, SendSmsService.class);
			Bundle b = new Bundle();
			b.putString(Constants.SMS_STR_KEY, smsText);
			b.putString(Constants.PHONE_NO_KEY, phoneNo);
			msgIntent.putExtras(b);

			context.startService(msgIntent);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.i("Receiver", "Exception in receiver.");
		}
	}

}
