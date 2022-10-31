package com.example.astropagerity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HoroFragment extends Fragment {
	String mainContent;

	HoroEngine engine;

	final String DATE_PATTERN = "dd.MM.yyyy";
	int mYear = 1990, mMonth = 0, mDay = 1;

	EditText birthdayField;
	View overviewZodiacFragment;
	View overviewChineseFragment;
	View descriptionFragment;
	
	Button chooseBtn;
	Button runBtn;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainContent = readContent();
		engine = new HoroEngine(mainContent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View myView = inflater.inflate(R.layout.horo_fragment, container, false);

		birthdayField = (EditText) myView.findViewById(R.id.BirthdayField);
		overviewZodiacFragment = myView.findViewById(R.id.OverviewZodiac);
		overviewChineseFragment = myView.findViewById(R.id.OverviewChineseSign);
		descriptionFragment = myView.findViewById(R.id.decriptionContent);
		chooseBtn = (Button) myView.findViewById(R.id.ChooseButton);
		runBtn = (Button) myView.findViewById(R.id.RunButton);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		sdf.setTimeZone(TimeZone.getDefault());
		birthdayField.setText(sdf.format(date));
		
		chooseBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onDatePicker(v);
			}
			
		});;
		
		runBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCalculate(v);
			}
			
		});;

		return myView;
	}

	public void onDatePicker(View v) {
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				mDay = dayOfMonth;
				mMonth = month;
				mYear = year;
				month++;
				String day = String.valueOf(dayOfMonth);
				String corrMonth = String.valueOf(month);
				if (dayOfMonth < 10) {
					day = "0" + dayOfMonth;
				}
				if (month < 10) {
					corrMonth = "0" + month;
				}

				StringBuilder sb = new StringBuilder();
				sb.append(day).append(".").append(corrMonth).append(".").append(year);
				birthdayField.setText(sb.toString());
			}
		}, mYear, mMonth, mDay);
		dpd.show();
	}

	public void onCalculate(View v) {
		String date = birthdayField.getText().toString();
		// Устанавливаем значения для китайской карточки
		TextView tvChineseName = (TextView) overviewChineseFragment.findViewById(R.id.signName);
		TextView tvChineseExtraName = (TextView) overviewChineseFragment.findViewById(R.id.signCategory);
		ImageView chineseImage = (ImageView) overviewChineseFragment.findViewById(R.id.decorLeft);
		String chineseSign = engine.getChineseSign(date);
		tvChineseName.setText(chineseSign);
		String chineseExtraSign = engine.getExtraChineseSign(birthdayField.getText().toString());
		tvChineseExtraName.setText(chineseExtraSign);
		chineseImage.setImageResource(getChineseSignImage());
//		chineseImage.setImageResource(getChineseSignImageByName(chineseSign));

		// Устанавливаем значения для зодиакальной карточки
		TextView tvZodiacName = (TextView) overviewZodiacFragment.findViewById(R.id.signName);
		TextView tvZodiacExtraName = (TextView) overviewZodiacFragment.findViewById(R.id.signCategory);
		ImageView zodiacImage = (ImageView) overviewZodiacFragment.findViewById(R.id.decorLeft);
		String zodiacSign = engine.getZodiacSign(date);
		tvZodiacName.setText(zodiacSign);
		tvZodiacExtraName.setText("");
		zodiacImage.setImageResource(getZodiacSignImage());

		// Устанавливаем значения для описания знака структурного гороскопа
		ImageView horoImage = (ImageView) descriptionFragment.findViewById(R.id.horoSign);
		TextView horoTitle = (TextView) descriptionFragment.findViewById(R.id.title);
		TextView horoContent = (TextView) descriptionFragment.findViewById(R.id.descriptionContent);
		horoTitle.setText(engine.calculateHoroSign(date)[0]);
		horoContent.setText(engine.calculateHoroSign(date)[1]);
		horoImage.setImageResource(getHoroSignImage());
	}

	public int getChineseSignImage() {
		int index = engine.chineseSign(engine.getCurrYear());
		String pos = String.valueOf(++index);
		if (index < 10) {
			pos = "0" + pos;
		}
		return getActivity().getResources().getIdentifier("chinese_sign_" + pos, "drawable",
				getActivity().getPackageName());
	}

	public int getZodiacSignImage() {
		int index = engine.zodiacSign(engine.getCurrYear(), engine.getCurrMonth(), engine.getCurrDay());
		String pos = String.valueOf(++index);
		if (index < 10) {
			pos = "0" + pos;
		}
		return getActivity().getResources().getIdentifier("zodiac_sign_" + pos, "drawable",
				getActivity().getPackageName());
	}

	public int getHoroSignImage() {
		int index = engine.getHoroIndex();
		String pos = String.valueOf(index);
		return getActivity().getResources().getIdentifier("horo_" + pos, "drawable", getActivity().getPackageName());
	}

	public String getMainContent() {
		return mainContent;
	}

	public void setMainContent(String mainContent) {
		this.mainContent = mainContent;
	}
	
	private String readContent() {
		InputStream in = this.getResources().openRawResource(R.raw.treats);
		InputStreamReader inStream = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(inStream);
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			while ((str = reader.readLine()) != null) {
				sb.append(str).append("$");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
			inStream.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str = sb.toString();
		return str;
	}

}
