package zeidler.colin.rocketjournal.dataviews.flightlog.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;

import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.FlightLog;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.Rocket;
import zeidler.colin.rocketjournal.dataviews.flightlog.AddFlightLog;

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
        setHasOptionsMenu(true);
        mContext = container.getContext();
        try {
            flightLogID = getActivity().getIntent().getExtras().getInt("Journal");
            populate();
        } catch (Exception e) {
            //do nothing
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            populate();
        } catch (NullPointerException e) {

        }
    }

    public void update(int id) {
        flightLogID = id;
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

        Format formatter = new SimpleDateFormat(getResources().getString(R.string.date_format_use));
        String dateT = formatter.format(flightLog.getDate());
        dateV.setText(dateT);

        noteV.setText(flightLog.getNotes());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FlightLog flightLog = DataModel.getInstance(mContext).getFlightLog(flightLogID);
        switch (item.getItemId()) {
            case R.id.delete_menu_flightlog:
                DataModel.getInstance(mContext).deleteFlightLog(flightLog.getId());
                return true;
            case R.id.edit_menu_flightlog:
                Intent intent = new Intent();
                intent.setClass(mContext, AddFlightLog.class);
                intent.putExtra("Journal", flightLog);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
