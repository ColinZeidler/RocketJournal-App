package zeidler.colin.rocketjournal.dataviews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;

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
    private FlightLog flightLog;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_flightlog_view, container, false);
        context = container.getContext();
        flightLog = (FlightLog) getActivity().getIntent().getExtras().getSerializable("Journal");
        populate();
        return rootView;
    }

    public void populate() {
        TextView nameV = (TextView) rootView.findViewById(R.id.r_name);
        TextView motorV = (TextView) rootView.findViewById(R.id.r_motor);
        TextView weightV = (TextView) rootView.findViewById(R.id.r_weight);
        TextView resV = (TextView) rootView.findViewById(R.id.r_res);
        TextView dateV = (TextView) rootView.findViewById(R.id.r_date);
        TextView noteV = (TextView) rootView.findViewById(R.id.r_notes);

        Rocket r = DataModel.getInstance(context).getRocket(flightLog.getRocketID());
        nameV.setText(r.getName());

        String motorT = flightLog.getMotor() + " " + flightLog.getDelay() + " seconds";
        motorV.setText(motorT);

        String weightT = r.getWeight() + " lbs";
        weightV.setText(weightT);

        resV.setText(flightLog.getResult().toString());

        Format formatter = new SimpleDateFormat("dd EEE, yyyy: hh:mm a");
        String dateT = formatter.format(flightLog.getDate());
        dateV.setText(dateT);

        noteV.setText(flightLog.getNotes());
    }
}
