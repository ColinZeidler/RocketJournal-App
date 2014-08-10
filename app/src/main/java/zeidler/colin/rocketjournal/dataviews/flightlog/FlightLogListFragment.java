package zeidler.colin.rocketjournal.dataviews.flightlog;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Comparator;

import zeidler.colin.rocketjournal.FlightLogListAdapter;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.UpdateList;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.FlightLog;
import zeidler.colin.rocketjournal.dataviews.flightlog.details.FlightLogDetailActivity;

/**
 * Created by Colin on 2014-07-21.
 *
 */
public class FlightLogListFragment extends Fragment implements UpdateList{

    private DataModel dataModel;
    private Context mContext;
    private FlightLogListAdapter arrAdapter;
    private Comparator<FlightLog> mComparator;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.flightlog_list, menu);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flightlog_list, container, false);
        setHasOptionsMenu(true);
        mContext = container.getContext();

        dataModel = DataModel.getInstance(mContext);
        arrAdapter = new FlightLogListAdapter(mContext, R.layout.adapter_flightlog,
                dataModel.getFlightLogs());

        mComparator = new FlightLog.DateCompare();
        arrAdapter.sort(mComparator);
        ListView lView = (ListView) rootView.findViewById(R.id.listView);
        lView.setAdapter(arrAdapter);
        TextView emptyText = (TextView) rootView.findViewById(R.id.empty_list);
        lView.setEmptyView(emptyText);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int j = (Integer)view.getTag();
                Intent intent = new Intent();
                intent.setClass(mContext, FlightLogDetailActivity.class);
                intent.putExtra("Journal", j);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void updateList() {
        arrAdapter.notifyDataSetChanged();
        arrAdapter.sort(mComparator);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }
}