package com.android.smsscheduler;

import java.util.GregorianCalendar;

import AABDatabaseManager.DatabaseManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.smsDatabase.model.SmsModel;
import com.android.smsfragments.DatePickerFragment;
import com.android.smsfragments.TimePickerFragment;

public class AddNewSmsReminder extends FragmentActivity {

	Button sendBtn;
	EditText smsEdit, phoneNumEdit;
	String contactName, smsStr, phoneNo;
	String TAG = "AddNewSmsReminder";
	public static int PICK_CONTACT = 1;
	DatabaseManager dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_layout);

		sendBtn = (Button) findViewById(R.id.sendBtn);
		smsEdit = (EditText) findViewById(R.id.smsEdit);
		phoneNumEdit = (EditText) findViewById(R.id.phoneNumEdit);
		smsStr = "";
		phoneNo = "";
		dm = new DatabaseManager(this);

		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				scheduleAlarm();
			}
		});
	}

	public void showTimePickerDialog(View view) {
		android.support.v4.app.DialogFragment timeFragment = new TimePickerFragment();
		timeFragment.show(getSupportFragmentManager(), "time_fragment");

	}

	public void showDatePickerDialog(View view) {
		android.support.v4.app.DialogFragment dateFragment = new DatePickerFragment();
		dateFragment.show(getSupportFragmentManager(), "date_fragment");
	}

	public void scheduleAlarm() {
		if (TextUtils.isEmpty(smsEdit.getText().toString())
				|| TextUtils.isEmpty(phoneNumEdit.getText().toString()))

		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Empty fields");
			alertDialogBuilder.setMessage(
					"Please fill phone number and message").setCancelable(true);
			alertDialogBuilder.setNegativeButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
		} else {
			try {
				smsStr = smsEdit.getText().toString();

				Long time = new GregorianCalendar().getTimeInMillis() + 60 * 1000;

				Intent intent = new Intent(AddNewSmsReminder.this,
						AlarmReceiver.class);

				Bundle b = new Bundle();
				b.putString(Constants.SMS_STR_KEY, smsStr);
				b.putString(Constants.PHONE_NO_KEY, phoneNo);
				intent.putExtras(b);

				SmsModel smsObj = new SmsModel();
				smsObj.setContactName(contactName);
				smsObj.setContactNumber(phoneNo);
				smsObj.setMessage(smsStr);
				smsObj.setInitialTime(System.currentTimeMillis());
				smsObj.setSendTime(time);
				dm.addRow(smsObj);

				AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

				alarmMgr.set(AlarmManager.RTC_WAKEUP, time, PendingIntent
						.getBroadcast(this, 1, intent,
								PendingIntent.FLAG_UPDATE_CURRENT));
				Log.i(TAG, "Alarm Scheduled :" + time);

				Toast.makeText(getApplicationContext(), "Alarm Scheduled ",
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG, "Exception occured while scheduling alarm");
			} finally {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	public void readContacts(View view) {
		try {
			Intent cIntent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(cIntent, PICK_CONTACT);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "Exception while picking contact");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			switch (requestCode) {
			case 1:
				if (resultCode == Activity.RESULT_OK)

				{
					Uri contactData = data.getData();
					Cursor c = managedQuery(contactData, null, null, null, null);
					if (c.moveToFirst()) {
						String id = c
								.getString(c
										.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

						String hasPhone = c
								.getString(c
										.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

						if (hasPhone.equalsIgnoreCase("1")) {
							Cursor phones = getContentResolver()
									.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Phone.CONTACT_ID
													+ " = " + id, null, null);
							phones.moveToFirst();
							phoneNo = phones.getString(phones
									.getColumnIndex("data1"));
							Log.i(TAG, "phone_no: " + phoneNo);

							contactName = phones
									.getString(phones
											.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
							phoneNumEdit.setText(phoneNo.toString());

						}
						String name = c
								.getString(c
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						Log.i(TAG, "name: " + name);

					}
				}
			}
		}

	}
}
