package com.android.smsscheduler;

import java.util.GregorianCalendar;

import com.android.smsfragments.DatePickerFragment;
import com.android.smsfragments.TimePickerFragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
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
				scheduleAlarm();
			}
		});

		
		
	}

	public void showTimePickerDialog(View view)
	{
		android.support.v4.app.DialogFragment timeFragment = new TimePickerFragment();
		timeFragment.show(getSupportFragmentManager(), "time_fragment");
		
	}
	
	public void showDatePickerDialog(View view)
	{
		android.support.v4.app.DialogFragment dateFragment = new DatePickerFragment();
		dateFragment.show(getSupportFragmentManager(), "date_fragment");
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
