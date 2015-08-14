package zeidler.colin.rocketjournal.dataviews.rocket.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import zeidler.colin.rocketjournal.FlightLogListAdapter;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.UpdateList;
import zeidler.colin.rocketjournal.data.BitmapLoader;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.Rocket;
import zeidler.colin.rocketjournal.dataviews.flightlog.AddFlightLog;
import zeidler.colin.rocketjournal.dataviews.flightlog.details.FlightLogDetailActivity;
import zeidler.colin.rocketjournal.dataviews.rocket.AddRocket;

/**
 * Created by Colin on 2014-07-26.
 *
 * Populate the details page for a rocket,
 */
public class RocketDetailFragment extends Fragment {

    private View rootView;
    private Context mContext;
    private int rocketID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rocket_view, container, false);
        setHasOptionsMenu(true);
        mContext = container.getContext();

        final View rocketImageView = rootView.findViewById(R.id.rocket_image);
        rocketImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                displayFullImage(((ImageView)rocketImageView).getDrawable());
            }
        });

        //TODO change this for blank view
        try {
            rocketID = getActivity().getIntent().getExtras().getInt("Rocket");
            populate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //end of change
        return rootView;
    }

    private void displayFullImage(Drawable image) {
        final ImageView expandedView = (ImageView) rootView.findViewById(R.id.expanded_image);
        expandedView.setImageDrawable(image);
        expandedView.setVisibility(View.VISIBLE);
        expandedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            populate();
        } catch (NullPointerException e) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Rocket rocket = DataModel.getInstance(mContext).getRocket(rocketID);
        switch (item.getItemId()) {
            case R.id.delete_menu_rocket:
                DataModel.getInstance(mContext).deleteRocket(rocket.getId());
                Fragment parent = getParentFragment();
                if (parent != null) {
                    ((UpdateList) parent).updateList();
                }
                return true;
            case R.id.edit_menu_rocket:
                Intent rocketIntent = new Intent();
                rocketIntent.setClass(mContext, AddRocket.class);
                rocketIntent.putExtra("Rocket", rocket);
                startActivity(rocketIntent);
                return true;
            case R.id.add_item_flightlog:
                Intent logIntent = new Intent();
                logIntent.setClass(mContext, AddFlightLog.class);
//                logIntent.putExtra("RocketID", rocket.getId());
                startActivity(logIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void update(int id) {
        rocketID = id;
        populate();
    }

    public void populate() {
        Rocket rocket = DataModel.getInstance(mContext).getRocket(rocketID);

        TextView rName = (TextView) rootView.findViewById(R.id.rocket_name);
        TextView rWeight = (TextView) rootView.findViewById(R.id.r_weight);
        TextView rFCount = (TextView) rootView.findViewById(R.id.r_flights);
        TextView rAlt = (TextView) rootView.findViewById(R.id.r_altitude);
        TextView rMD = (TextView) rootView.findViewById(R.id.r_motorTubeDiam);
        TextView rML = (TextView) rootView.findViewById(R.id.r_motorTubeLen);
        TextView rChute = (TextView) rootView.findViewById(R.id.r_chuteSize);

        final ImageView rImage = (ImageView) rootView.findViewById(R.id.rocket_image);

        rName.setText(rocket.getName());
        rWeight.setText(String.valueOf(rocket.getWeight()) + " lbs");
        rFCount.setText(String.valueOf(rocket.getFlightCount()));
        rAlt.setText(String.valueOf(rocket.getMaxAltitude()));
        rMD.setText(String.valueOf(rocket.getMotorTubeDiam()));
        rML.setText(String.valueOf(rocket.getMotorTubeLen()));
        rChute.setText(String.valueOf(rocket.getChuteSize()));

        final String image = rocket.getImage();
        if (!image.equals("")) {
            BitmapLoader task = new BitmapLoader(rImage);
            task.execute(image);
        } else {
            rImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.default_rocket));
        }

        FlightLogListAdapter arrAdapter = new FlightLogListAdapter(mContext,
                R.layout.adapter_flightlog,
                DataModel.getInstance(mContext).getFlightLogs(rocket.getId()));


        ListView lView = (ListView) rootView.findViewById(R.id.flightlog_list);
        lView.setAdapter(arrAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int f = (Integer)view.getTag();
                Intent intent = new Intent();
                intent.setClass(mContext, FlightLogDetailActivity.class);
                intent.putExtra("Journal", f);
                startActivity(intent);
            }
        });
    }
}
