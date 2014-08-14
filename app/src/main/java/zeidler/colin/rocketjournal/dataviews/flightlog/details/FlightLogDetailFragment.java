package zeidler.colin.rocketjournal.dataviews.flightlog.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;

import zeidler.colin.rocketjournal.SettingsActivity;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.FlightLog;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-21.
 *
 */
public class FlightLogDetailFragment extends Fragment {

    private View rootView;
    private int flightLogID;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_flightlog_view, container, false);
        mContext = container.getContext();
        flightLogID = getActivity().getIntent().getExtras().getInt("Journal");
        populate();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populate();
    }

    public void populate() {
        FlightLog flightLog = DataModel.getInstance(mContext).getFlightLog(flightLogID);

        TextView nameV = (TextView) rootView.findViewById(R.id.r_name);
        TextView motorV = (TextView) rootView.findViewById(R.id.r_motor);
        TextView weightV = (TextView) rootView.findViewById(R.id.r_weight);
        TextView resV = (TextView) rootView.findViewById(R.id.r_res);
        TextView dateV = (TextView) rootView.findViewById(R.id.r_date);
        TextView noteV = (TextView) rootView.findViewById(R.id.r_notes);
        TextView rAlt = (TextView) rootView.findViewById(R.id.r_altitude);

        Rocket r = DataModel.getInstance(mContext).getRocket(flightLog.getRocketID());
        nameV.setText(r.getName());

        String motorT = flightLog.getMotor() + " " + flightLog.getDelay() + " seconds";
        motorV.setText(motorT);
        rAlt.setText(String.valueOf(flightLog.getAltitude()));

        String weightT = r.getWeight() + " lbs";
        weightV.setText(weightT);

        resV.setText(flightLog.getResult().toString());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Format formatter = new SimpleDateFormat(prefs.getString(SettingsActivity.KEY_PREF_DATEFORMAT, ""));
        String dateT = formatter.format(flightLog.getDate());
        dateV.setText(dateT);

        noteV.setText(flightLog.getNotes());
    }
}
