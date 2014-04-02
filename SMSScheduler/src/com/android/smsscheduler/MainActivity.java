package com.android.smsscheduler;

import java.io.InputStream;
import java.util.ArrayList;

import AABDatabaseManager.DatabaseManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.smsDatabase.model.SmsModel;

public class MainActivity extends FragmentActivity {
	final static String TAG = "MainActivity";
	final static int SMS_REQ_CODE = 100;
	ListView listReminder;
	ArrayList<SmsModel> smsModelList = new ArrayList<SmsModel>();
	DatabaseManager dm;
	CustomListAdapter cAdapter;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Button addNewButton = (Button) findViewById(R.id.addNew);
		dm = new DatabaseManager(this);
		cAdapter = new CustomListAdapter();
		listReminder = (ListView) findViewById(R.id.list);
		listReminder.setAdapter(cAdapter);

		registerForContextMenu(listReminder);

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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater m = getMenuInflater();
		m.inflate(R.menu.del_mennu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete_item:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			position = (int) info.id;
			cAdapter.delRow(position);
			smsModelList.remove(position);
			cAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 100:
			if (resultCode == Activity.RESULT_OK) {
				smsModelList = dm.getAllData();
				cAdapter.notifyDataSetChanged();
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

		public void delRow(int position) {
			// TODO Auto-generated method stub

			dm.deleteRow(smsModelList.get(position).getId());

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
			ImageView image = (ImageView) vi.findViewById(R.id.contactPic);

			SmsModel smsObj = smsModelList.get(position);
			name.setText(smsObj.getContactName());
			number.setText(smsObj.getContactNumber());
			message.setText(smsObj.getMessage());
			image.setImageBitmap(loadContactPhoto(getContentResolver(),
					smsObj.getContact_id(), smsObj.getPhoto_id()));
			return vi;
		}

	}

	public static Bitmap loadContactPhoto(ContentResolver cr, long id,
			long photo_id) {

		Uri uri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, id);
		InputStream input = ContactsContract.Contacts
				.openContactPhotoInputStream(cr, uri);
		if (input != null) {
			return BitmapFactory.decodeStream(input);
		} else {
			Log.d("PHOTO", "first try failed to load photo");

		}

		byte[] photoBytes = null;

		Uri photoUri = ContentUris.withAppendedId(
				ContactsContract.Data.CONTENT_URI, photo_id);

		Cursor c = cr.query(photoUri,
				new String[] { ContactsContract.CommonDataKinds.Photo.PHOTO },
				null, null, null);

		try {
			if (c.moveToFirst())
				photoBytes = c.getBlob(0);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {

			c.close();
		}

		if (photoBytes != null)
			return BitmapFactory.decodeByteArray(photoBytes, 0,
					photoBytes.length);
		else
			Log.d("PHOTO", "second try also failed");
		return null;
	}

}
