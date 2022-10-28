package com.example.astropagerity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ThousandsFragment extends FragmentActivity {
	
	ArrayList<String> dates = new ArrayList<String>();
	EditText dateHolder;
	ArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thousands_fragment);
		
		ListView myLv = (ListView) findViewById(R.id.listView1);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dates);
		myLv.setAdapter(adapter);
		
		Button myCalcButton = (Button) findViewById(R.id.button2);
		Button mySysTimeButton = (Button) findViewById(R.id.button1);
		dateHolder = (EditText) findViewById(R.id.editText1);
		
		Date myDate = new Date(0);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		dateHolder.setText(formatter.format(myDate));
		
		mySysTimeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				dateHolder.setText(formatter.format(new Date()));
			}
		});
		
		myCalcButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dates.clear();
				String myDate = dateHolder.getText().toString();
				String pattern = getDatePattern(myDate);
				
				Date date = new Date();
				try {
					date = new SimpleDateFormat(pattern).parse(myDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				long step = 3600000000L;
				
				for (int i = 1; i <= 42; i++) {
					long currentDateMs = date.getTime();
					currentDateMs += step * i;
					Date temp = new Date(currentDateMs); 
					SimpleDateFormat tempFormatter = new SimpleDateFormat(pattern);
					SimpleDateFormat weekDayFormatter = new SimpleDateFormat("E");
					String row = tempFormatter.format(temp) + "\t\t\t\t\t\t" + weekDayFormatter.format(temp) + "\t\t\t\t\t\t" + i * 1000;
					dates.add(row);
				}
				
				adapter.notifyDataSetChanged();
				}
			
		});
		
		
	}
	
	private String getDatePattern(String myDate) {
		String pattern;
		if (myDate.contains("-")) {
			pattern = "dd-MM-yyyy";
		} else if (myDate.contains(":")) {
			pattern = "dd:MM:yyyy";
		} else if (myDate.contains("/")) {
			pattern = "dd/MM/yyyy";
		} else {
			pattern = "dd MM yyyy";
		}
		return pattern;
	}

}
