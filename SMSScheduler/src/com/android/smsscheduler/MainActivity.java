package com.android.smsscheduler;

import java.util.ArrayList;

import AABDatabaseManager.DatabaseManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.smsDatabase.model.SmsModel;

public class MainActivity extends FragmentActivity {
	final static String TAG = "MainActivity";
	final static int SMS_REQ_CODE = 100;
	ListView listReminder;
	ArrayList<SmsModel> smsModelList = new ArrayList<SmsModel>();
	DatabaseManager dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Button addNewButton = (Button) findViewById(R.id.addNew);
		listReminder = (ListView) findViewById(R.id.list);
		listReminder.setAdapter(new CustomListAdapter());
		dm = new DatabaseManager(this);
		addNewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent intent = new Intent(MainActivity.this,
						AddNewSmsReminder.class);
				startActivityForResult(intent, SMS_REQ_CODE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			switch (requestCode) {
			case 1:
				if (resultCode == Activity.RESULT_OK) {

				}
			}
		}
	}

	public class CustomListAdapter extends BaseAdapter {
		LayoutInflater inflater;

		public CustomListAdapter() {
			// TODO Auto-generated constructor stub
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			smsModelList = dm.getAllData();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return smsModelList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.sms_list_row, null);

			TextView name = (TextView) vi.findViewById(R.id.name);
			TextView number = (TextView) vi.findViewById(R.id.number);
			TextView message = (TextView) vi.findViewById(R.id.message);
			SmsModel smsObj = smsModelList.get(position);
			name.setText(smsObj.getContactName());
			number.setText(smsObj.getContactNumber());
			message.setText(smsObj.getMessage());
			return vi;
		}

	}
}
