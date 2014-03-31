package AABDatabaseManager;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.smsDatabase.model.SmsModel;

public class DatabaseManager {

	private SQLiteDatabase db; // a reference to the database manager class.
	private final String DB_NAME = "sms"; // the name of our database
	private final int DB_VERSION = 1; // the version of the database

	// the names for our database columns
	private final String TABLE_NAME = "sms_table";
	private final String TABLE_ROW_ID = "id";
	private final String TABLE_ROW_MSG = "message";
	private final String TABLE_ROW_NAME = "contact_name";
	private final String TABLE_ROW_PHONENUM = "contact_number";
	private final String TABLE_ROW_INITIALTIME = "initial_time";
	private final String TABLE_ROW_SENDTIME = "send_time";
	Context context;

	// TODO: write the constructor and methods for this class

	public DatabaseManager(Context context) {
		this.context = context;

		// create or open the database
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		this.db = helper.getWritableDatabase();
	}

	// the beginnings our SQLiteOpenHelper class
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

		public CustomSQLiteOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// the SQLite query string that will create our 3 column database
			// table.
			String newTableQueryString = "create table " + TABLE_NAME + " ("
					+ TABLE_ROW_ID
					+ " integer primary key autoincrement not null,"
					+ TABLE_ROW_MSG + " text" + TABLE_ROW_NAME + " text"
					+ TABLE_ROW_PHONENUM + " integer not null"
					+ TABLE_ROW_INITIALTIME + " long" + TABLE_ROW_SENDTIME
					+ " long" + ");";

			// execute the query string to the database.
			db.execSQL(newTableQueryString);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			// NOTHING TO DO HERE. THIS IS THE ORIGINAL DATABASE VERSION.
			// OTHERWISE, YOU WOULD SPECIFIY HOW TO UPGRADE THE DATABASE
			// FROM OLDER VERSIONS.

		}
	}

	public void addRow(SmsModel smsObj) {
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();

		// this is how you add a value to a ContentValues object
		// we are passing in a key string and a value string each time
		values.put(TABLE_ROW_NAME, smsObj.getContactName());
		values.put(TABLE_ROW_PHONENUM, smsObj.getContactNumber());
		values.put(TABLE_ROW_MSG, smsObj.getMessage());
		values.put(TABLE_ROW_INITIALTIME, smsObj.getInitialTime());
		values.put(TABLE_ROW_SENDTIME, smsObj.getSendTime());

		// ask the database object to insert the new data
		try {
			db.insert(TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString()); // prints the error message to the
												// log
			e.printStackTrace(); // prints the stack trace to the log
		}
	}

	public void deleteRow(int rowID) {
		// ask the database manager to delete the row of given id
		try {
			db.delete(TABLE_NAME, TABLE_ROW_ID + "=" + rowID, null);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}

	public void updateRow(int rowID, SmsModel smsObj) {
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();
		values.put(TABLE_ROW_NAME, smsObj.getContactName());
		values.put(TABLE_ROW_PHONENUM, smsObj.getContactNumber());
		values.put(TABLE_ROW_MSG, smsObj.getMessage());
		values.put(TABLE_ROW_INITIALTIME, smsObj.getInitialTime());
		values.put(TABLE_ROW_SENDTIME, smsObj.getSendTime());

		// ask the database object to update the database row of given rowID
		try {
			db.update(TABLE_NAME, values, TABLE_ROW_ID + "=" + rowID, null);
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}

	public SmsModel getRowAsObject(int rowID) {
		SmsModel rowSMSObj = new SmsModel();
		Cursor cursor;

		try {

			cursor = db.query(TABLE_NAME, new String[] { TABLE_ROW_ID,
					TABLE_ROW_MSG, TABLE_ROW_NAME, TABLE_ROW_PHONENUM,
					TABLE_ROW_INITIALTIME, TABLE_ROW_SENDTIME }, TABLE_ROW_ID
					+ "=" + rowID, null, null, null, null, null);
			if (!cursor.isAfterLast()) {
				do {
					rowSMSObj.setMessage(cursor.getString(1));
					rowSMSObj.setContactName(cursor.getString(2));
					rowSMSObj.setContactNumber(cursor.getInt(3));
					rowSMSObj.setInitialTime(cursor.getLong(4));
					rowSMSObj.setSendTime(cursor.getLong(5));
				} while (cursor.moveToNext()); // try to move the cursor's
												// pointer forward one position.
			}
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}

		return rowSMSObj;
	}

	public ArrayList<SmsModel> getAllData() {
		ArrayList<SmsModel> allRowsObj = new ArrayList<SmsModel>();
		Cursor cursor;
		SmsModel rowSMSObj;
		try {

			cursor = db.query(TABLE_NAME, new String[] { TABLE_ROW_ID,
					TABLE_ROW_MSG, TABLE_ROW_NAME, TABLE_ROW_PHONENUM,
					TABLE_ROW_INITIALTIME, TABLE_ROW_SENDTIME }, null, null,
					null, null, null);
			if (!cursor.isAfterLast()) {
				do {
					rowSMSObj = new SmsModel();
					rowSMSObj.setMessage(cursor.getString(1));
					rowSMSObj.setContactName(cursor.getString(2));
					rowSMSObj.setContactNumber(cursor.getInt(3));
					rowSMSObj.setInitialTime(cursor.getLong(4));
					rowSMSObj.setSendTime(cursor.getLong(5));
					allRowsObj.add(rowSMSObj);

				} while (cursor.moveToNext()); // try to move the cursor's
												// pointer forward one position.
			}
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}

		return allRowsObj;

	}

}
