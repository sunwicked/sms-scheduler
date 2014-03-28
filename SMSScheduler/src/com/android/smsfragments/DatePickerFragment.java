package com.android.smsfragments;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener
{
	String TAG = "DatePickerFragment" ;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		 // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);

		
	}

	@Override
	public void onDateSet(DatePicker VIEW, int year, int month, int day)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "year:"+year+ "month:"+month+ "day:"+day);
	}
}
