package com.android.smsscheduler;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.smsfragments.DatePickerFragment;
import com.android.smsfragments.TimePickerFragment;

public class MainActivity extends FragmentActivity
{
	Button sendBtn ;
	EditText smsEdit, phoneNumEdit;
	String smsStr, phoneNo;
	String TAG = "MainActivity" ;
	public static int PICK_CONTACT = 1 ; 

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sendBtn = (Button)findViewById(R.id.sendBtn);
		smsEdit = (EditText)findViewById(R.id.smsEdit);
		phoneNumEdit = (EditText)findViewById(R.id.phoneNumEdit);
		smsStr = "";
		phoneNo = "";

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
		try
		{
			smsStr = smsEdit.getText().toString();

			Long time = new GregorianCalendar().getTimeInMillis()+60*1000;

			Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

			Bundle b = new Bundle() ;
			b.putString(Constants.SMS_STR_KEY, smsStr);
			b.putString(Constants.PHONE_NO_KEY, phoneNo);
			intent.putExtras(b);

			AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

			alarmMgr.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intent,
					PendingIntent.FLAG_UPDATE_CURRENT));
			Log.i(TAG, "Alarm Scheduled :"+time);

			Toast.makeText(getApplicationContext(), "Alarm Scheduled ",
					Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.i(TAG, "Exception occured while scheduling alarm");
		}
	}

	public void readContacts(View view)
	{
		try
		{
			Intent cIntent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(cIntent, PICK_CONTACT);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.i(TAG, "Exception while picking contact");
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if(data != null)
		{
			switch(requestCode)
			{
			case 1:
				if(resultCode == Activity.RESULT_OK)
					
				{
					Uri contactData = data.getData();
					Cursor c =  managedQuery(contactData, null, null, null, null);
					if (c.moveToFirst()) 
					{
						String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

						String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

						if (hasPhone.equalsIgnoreCase("1")) 
						{
							Cursor phones = getContentResolver().query( 
									ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, 
									null, null);
							phones.moveToFirst();
							phoneNo = phones.getString(phones.getColumnIndex("data1"));
							Log.i(TAG, "phone_no: "+phoneNo);
							
							phoneNumEdit.setText(phoneNo.toString());

						}
						String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						Log.i(TAG, "name: "+name);

					}
				}
			}
		}

	}
}
