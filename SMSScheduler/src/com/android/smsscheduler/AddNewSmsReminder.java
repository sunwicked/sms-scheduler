package com.android.smsscheduler;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.Calendar;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.smsDatabase.model.SmsModel;
import com.android.smsfragments.DatePickerFragment;
import com.android.smsfragments.DatePickerFragment.DatePickedListener;
import com.android.smsfragments.TimePickerFragment;
import com.android.smsfragments.TimePickerFragment.TimePickedListener;

public class AddNewSmsReminder extends FragmentActivity implements
		TimePickedListener, DatePickedListener {

	private Button sendBtn;
	private EditText smsEdit, phoneNumEdit;
	private String contactName, smsStr, phoneNo;
	private String TAG = "AddNewSmsReminder";
	public static int PICK_CONTACT = 1;
	private DatabaseManager dm;

	private long contactId, photoId;
	private TextView showDate, showTime;
	private Calendar calSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_layout);

		init();
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				scheduleAlarm();
			}
		});
	}

	/**
	 * Schedule alarm method sets up alarm for sending sms at stipulate time
	 * Checks : If phone number is empty or message is empty
	 * 
	 */
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
			alertDialogBuilder.show();
		} else {
			try {
				smsStr = smsEdit.getText().toString();

				Intent intent = new Intent(AddNewSmsReminder.this,
						AlarmReceiver.class);

				Bundle b = new Bundle();
				b.putString(Constants.SMS_STR_KEY, smsStr);
				b.putString(Constants.PHONE_NO_KEY, phoneNo);
				intent.putExtras(b);

				long setTime = calSet.getTimeInMillis();

				SmsModel smsObj = new SmsModel();
				smsObj.setContactName(contactName);
				smsObj.setContactNumber(phoneNo);
				smsObj.setMessage(smsStr);
				smsObj.setInitialTime(System.currentTimeMillis());
				smsObj.setSendTime(calSet.getTimeInMillis());
				smsObj.setContact_id(contactId);
				smsObj.setPhoto_id(photoId);
				dm.addRow(smsObj);

				AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

				alarmMgr.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),
						PendingIntent.getBroadcast(this, 1, intent,
								PendingIntent.FLAG_UPDATE_CURRENT));

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
							contactId = phones
									.getLong(phones
											.getColumnIndex(ContactsContract.Contacts._ID));
							photoId = phones
									.getLong(phones
											.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));

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

	private void init() {
		sendBtn = (Button) findViewById(R.id.sendBtn);
		smsEdit = (EditText) findViewById(R.id.smsEdit);
		phoneNumEdit = (EditText) findViewById(R.id.phoneNumEdit);
		showDate = (TextView) findViewById(R.id.showDate);
		showTime = (TextView) findViewById(R.id.showTime);
		smsStr = "";
		phoneNo = "";
		dm = new DatabaseManager(this);
		calSet = Calendar.getInstance();
	}

	public void showTimePickerDialog(View view) {
		android.support.v4.app.DialogFragment timeFragment = new TimePickerFragment();
		timeFragment.show(getSupportFragmentManager(), "time_fragment");

	}

	@Override
	public void onTimePicked(Calendar cTime) {
		// TODO Auto-generated method stb
		calSet = Calendar.getInstance();
		calSet.clear(Calendar.HOUR_OF_DAY);
		calSet.set(Calendar.HOUR_OF_DAY, cTime.get(cTime.HOUR_OF_DAY)); // HOUR
		calSet.clear(Calendar.MINUTE);
		calSet.set(Calendar.MINUTE, cTime.get(cTime.MINUTE)); // MIN
		Log.d(TAG, DateFormat.format("h:mm a", cTime.getTimeInMillis())
				.toString());
		Log.d(TAG, DateFormat.format("h:mm a", calSet.getTimeInMillis())
				.toString());
		showTime.setText(DateFormat.format("h:mm a", cTime.getTimeInMillis()));
	}

	public void showDatePickerDialog(View view) {
		android.support.v4.app.DialogFragment dateFragment = new DatePickerFragment();
		dateFragment.show(getSupportFragmentManager(), "date_fragment");
	}

	@Override
	public void onDatePicked(Calendar dTime) {

		calSet = Calendar.getInstance();
		calSet.clear(Calendar.DAY_OF_MONTH);
		calSet.set(Calendar.DAY_OF_MONTH, dTime.get(dTime.DAY_OF_MONTH)); // 1-31
		calSet.clear(Calendar.MONTH);
		calSet.set(Calendar.MONTH, dTime.get(dTime.MONTH));
		calSet.clear(Calendar.YEAR);
		calSet.set(Calendar.YEAR, dTime.get(dTime.YEAR));// year...
		Log.d(TAG, DateFormat.format("d/M/y", dTime).toString());
		Log.d(TAG, DateFormat.format("d/M/y", calSet).toString());
		showDate.setText(DateFormat.format("d/M/y", dTime));
	}
}
