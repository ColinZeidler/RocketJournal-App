package zeidler.colin.rocketjournal.dataviews.rocket;

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
import zeidler.colin.rocketjournal.UpdateList;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.dataviews.rocket.details.RocketDetailActivity;

/**
 * Created by Colin on 2014-07-26.
 *
 */
public class RocketListFragment extends Fragment implements UpdateList{

    private Context mContext;
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

        mContext = container.getContext();

        dataModel = DataModel.getInstance(mContext);
        arrAdapter = new RocketListAdapter(mContext, R.layout.adapter_rocket,
                dataModel.getRockets());

        ListView lView = (ListView) rootView.findViewById(R.id.listView);
        lView.setAdapter(arrAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int r = (Integer)view.getTag();
                Intent intent = new Intent();
                intent.setClass(mContext, RocketDetailActivity.class);
                intent.putExtra("Rocket", r);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void updateList() {
        arrAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }


}