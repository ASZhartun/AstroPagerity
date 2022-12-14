package com.example.astropagerity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MoonPhaseFragment extends Fragment {

	final String DATE_PATTERN = "dd.MM.yyyy HH:mm";

	EditText dateField;
	EditText zoneField;

	ListView moonParams;
	ListView moonPhases;

	Button runBtn;

	ArrayList<HashMap<String, String>> sourceParams;
	ArrayList<HashMap<String, String>> sourcePhases;

	SimpleAdapter paramAdapter;
	SimpleAdapter phaseAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View myView = inflater.inflate(R.layout.moonphase_fragment, container, false);

		// ????? ???????, ??????, ???????
		dateField = (EditText) myView.findViewById(R.id.dateField);
		zoneField = (EditText) myView.findViewById(R.id.zoneField);
		moonParams = (ListView) myView.findViewById(R.id.moonParams);
		moonPhases = (ListView) myView.findViewById(R.id.moonPhases);
		runBtn = (Button) myView.findViewById(R.id.runBtn);

		// ????????? ??????? ???? ? ?????? ??????? ? ???????? ?????
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		Calendar myCal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Minsk"));
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
		dateField.setText(sdf.format(myCal.getTime()));
		int hoursGMT = TimeZone.getTimeZone("Europe/Minsk").getRawOffset() / 3600000;
		zoneField.setText("GMT: " + Integer.toString(hoursGMT));

		// ??????????? ????????? ???????? ????
		sourceParams = getParamCalculations(dateField.getText().toString());
		paramAdapter = new SimpleAdapter(getActivity(), sourceParams, R.layout.moonphase_item_data,
				new String[] { MoonPhaseParameterItem.NAME, MoonPhaseParameterItem.VALUE },
				new int[] { R.id.name, R.id.value });
		moonParams.setAdapter(paramAdapter);

		// ????????? ????????? ???????? ??? ????
		sourcePhases = getPhasesCalculations(dateField.getText().toString());
		phaseAdapter = new SimpleAdapter(getActivity(), sourcePhases, R.layout.moonphase_item_data,
				new String[] { MoonPhaseParameterItem.NAME, MoonPhaseParameterItem.VALUE },
				new int[] { R.id.name, R.id.value });
		moonPhases.setAdapter(phaseAdapter);

		runBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sourceParams.clear();
				ArrayList<HashMap<String, String>> newerParamSource = getParamCalculations(
						dateField.getText().toString());
				for (int i = 0; i < newerParamSource.size(); i++) {
					sourceParams.add(newerParamSource.get(i));
				}
				paramAdapter.notifyDataSetChanged();

				sourcePhases.clear();
				ArrayList<HashMap<String, String>> newerPhasesSource = getPhasesCalculations(
						dateField.getText().toString());
				for (int i = 0; i < newerPhasesSource.size(); i++) {
					sourcePhases.add(newerPhasesSource.get(i));
				}
				phaseAdapter.notifyDataSetChanged();
			}
		});

		return myView;
	}

	private ArrayList<HashMap<String, String>> getParamCalculations(String dateForm) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		Jdays myDate = new Jdays();
		myDate.set(dateForm, true);
		double jd = myDate.get();
		HashMap<String, String> jdMap = new MoonPhaseParameterItem("??. ????", Double.toString(jd));

		double[] pp = new double[6];
		Phase.phase(jd, pp);

		int phase = (int) (pp[0] * 100);
		HashMap<String, String> phaseMap = new MoonPhaseParameterItem("????", Integer.toString(phase));

		String grow = (int) pp[1] + "? " + (int) (24 * (pp[1] - Math.floor(pp[1]))) + "? "
				+ (int) (1440 * (pp[1] - Math.floor(pp[1]))) % 60 + "?";
		HashMap<String, String> growMap = new MoonPhaseParameterItem("???????", grow);

		ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		results.add(jdMap);
		results.add(phaseMap);
		results.add(growMap);
		return results;
	}

	private ArrayList<HashMap<String, String>> getPhasesCalculations(String dateForm) {
		double jd = new Jdays(dateForm).get();

		double[] pp = new double[5];
		Phase.phasehunt5(jd, pp);

		HashMap<String, String> newMoonBegin = new MoonPhaseParameterItem("?????????: ", new Jdays(pp[0]).format(10));
		HashMap<String, String> firstQuarter = new MoonPhaseParameterItem("1 ????????: ", new Jdays(pp[1]).format(10));
		HashMap<String, String> fullMoon = new MoonPhaseParameterItem("??????????: ", new Jdays(pp[2]).format(10));
		HashMap<String, String> thirdQuarter = new MoonPhaseParameterItem("3 ????????: ", new Jdays(pp[3]).format(10));
		HashMap<String, String> newMoonEnd = new MoonPhaseParameterItem("?????????: ", new Jdays(pp[4]).format(10));

		ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		results.add(newMoonBegin);
		results.add(firstQuarter);
		results.add(fullMoon);
		results.add(thirdQuarter);
		results.add(newMoonEnd);
		return results;
	}
}
