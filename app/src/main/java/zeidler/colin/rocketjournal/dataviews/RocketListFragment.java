package zeidler.colin.rocketjournal.dataviews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.RocketListAdapter;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-26.
 *
 */
public class RocketListFragment extends Fragment {

    private Context context;
    private DataModel dataModel;
    private RocketListAdapter arrAdapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rocket_list, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_rocket_list, container, false);

        context = container.getContext();

        dataModel = DataModel.getInstance(context);
        arrAdapter = new RocketListAdapter(context, R.layout.adapter_rocket,
                dataModel.getRockets());

        ListView lView = (ListView) rootView.findViewById(R.id.listView);
        lView.setAdapter(arrAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rocket r = (Rocket)view.getTag();
                Intent intent = new Intent();
                intent.setClass(context, RocketDetailActivity.class);
                intent.putExtra("Rocket", r);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void deleteAll() {
        dataModel.deleteAllRockets();
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