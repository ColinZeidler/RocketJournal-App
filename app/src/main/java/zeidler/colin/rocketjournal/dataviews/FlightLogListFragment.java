package zeidler.colin.rocketjournal.dataviews;

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

import zeidler.colin.rocketjournal.FlightLogListAdapter;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.DataModel;

/**
 * Created by Colin on 2014-07-21.
 *
 */
public class FlightLogListFragment extends Fragment {

    private DataModel dataModel;
    private Context context;
    private FlightLogListAdapter arrAdapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.flightlog_list, menu);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flightlog_list, container, false);
        setHasOptionsMenu(true);
        context = container.getContext();

        dataModel = DataModel.getInstance(context);
        arrAdapter = new FlightLogListAdapter(context, R.layout.adapter_flightlog,
                dataModel.getFlightLogs());

        ListView lView = (ListView) rootView.findViewById(R.id.listView);
        lView.setAdapter(arrAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int j = (Integer)view.getTag();
                Intent intent = new Intent();
                intent.setClass(context, FlightLogDetailActivity.class);
                intent.putExtra("Journal", j);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void deleteAll() {
        dataModel.deleteAllFlightLogs();
        updateList();
    }

    public void updateList() {
        arrAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }
}