package zeidler.colin.rocketjournal.dataviews.flightlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.UpdateList;
import zeidler.colin.rocketjournal.dataviews.flightlog.details.FlightLogDetailActivity;
import zeidler.colin.rocketjournal.dataviews.flightlog.details.FlightLogDetailFragment;

/**
 * Created by Colin on 2014-08-27.
 */
public class FlightLogPageHandler extends Fragment implements AdapterView.OnItemClickListener, UpdateList{
    private View rootView;
    private Context mContext;
    private FlightLogDetailFragment detailFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.pager_item_flightlog, container, false);
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction().add(R.id.flightlog_list_fragment,
                    new FlightLogListFragment()).commit();
            if (rootView.findViewById(R.id.flightlog_details_fragment) != null) {
                if (detailFragment == null)
                    detailFragment = new FlightLogDetailFragment();
                getChildFragmentManager().beginTransaction().add(R.id.flightlog_details_fragment,
                        detailFragment).commit();
            }
        }
        mContext = container.getContext();
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int j = (Integer)view.getTag();
        if (detailFragment == null) {   //always returning null TODO fix me
            Intent intent = new Intent();
            intent.setClass(mContext, FlightLogDetailActivity.class);
            intent.putExtra("Journal", j);
            startActivity(intent);
        } else {
            detailFragment.update(j);
        }
    }

    @Override
    public void updateList() {
        ((FlightLogListFragment) getFragmentManager()
                .findFragmentById(R.id.flightlog_list_fragment)).updateList();
    }
}
