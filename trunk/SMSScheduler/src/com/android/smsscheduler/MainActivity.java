package com.android.smsscheduler;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity
{
	Button sendBtn ;
	EditText smsEdit;
	String smsStr, phoneNo;
	String TAG = "MainActivity" ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sendBtn = (Button)findViewById(R.id.sendBtn);
		smsEdit = (EditText)findViewById(R.id.smsEdit);
		smsStr = "";
		phoneNo = "8800839680";

		sendBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//sendSMS();
				scheduleAlarm();
			}
		});

	}

	public void sendSMS()
	{
		smsStr = smsEdit.getText().toString();

		if(smsStr != null)
		{
			try
			{
				SmsManager smsMgr = SmsManager.getDefault() ;
				smsMgr.sendTextMessage(phoneNo, null, smsStr, null, null);
				Toast.makeText(getApplicationContext(), "SMS Sent!",
						Toast.LENGTH_LONG).show();
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(),
						"SMS faild, please try again later!",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
	}

	public void scheduleAlarm()
	{
		smsStr = smsEdit.getText().toString();
		
		Long time = new GregorianCalendar().getTimeInMillis()+60*1000;
		
		Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
		
		Bundle b = new Bundle() ;
		b.putString("sms_text", smsStr);
		intent.putExtras(b);
		
		AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		alarmMgr.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intent,
				PendingIntent.FLAG_UPDATE_CURRENT));
		Log.i(TAG, "Alarm Scheduled :"+time);
		
		Toast.makeText(getApplicationContext(), "Alarm Scheduled ",
				Toast.LENGTH_LONG).show();
	}
}
