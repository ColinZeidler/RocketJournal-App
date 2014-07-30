package zeidler.colin.rocketjournal.dataviews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import zeidler.colin.rocketjournal.FlightLogListAdapter;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.FlightLog;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-26.
 */
public class RocketDetailFragment extends Fragment {

    private View rootView;
    private Context mContext;
    private Rocket rocket;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rocket_view, container, false);
        mContext = container.getContext();
        rocket = (Rocket) getActivity().getIntent().getExtras().getSerializable("Rocket");
        populate();
        return rootView;
    }

    public void populate() {
        TextView rName = (TextView) rootView.findViewById(R.id.rocket_name);
        TextView rWeight = (TextView) rootView.findViewById(R.id.r_weight);
        TextView rFCount = (TextView) rootView.findViewById(R.id.r_flights);

        rName.setText(rocket.getName());
        rWeight.setText(String.valueOf(rocket.getWeight()) + " lbs");
        rFCount.setText(String.valueOf(rocket.getFlightCount()));

        FlightLogListAdapter arrAdapter = new FlightLogListAdapter(mContext,
                R.layout.adapter_flightlog,
                DataModel.getInstance(mContext).getFlightLogs(rocket.getId()));


        ListView lView = (ListView) rootView.findViewById(R.id.flightlog_list);
        lView.setAdapter(arrAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlightLog f = (FlightLog)view.getTag();
                Intent intent = new Intent();
                intent.setClass(mContext, FlightLogDetailActivity.class);
                intent.putExtra("Journal", f);
                startActivity(intent);
            }
        });
    }
}
