package zeidler.colin.rocketjournal.dataviews.rocket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.RocketListAdapter;
import zeidler.colin.rocketjournal.UpdateList;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-26.
 *
 */
public class RocketListFragment extends Fragment implements UpdateList,
        PopupMenu.OnMenuItemClickListener{

    private Context mContext;
    private DataModel dataModel;
    private RocketListAdapter arrAdapter;
    private Rocket.RocketCompare mComparator;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        rootView = inflater.inflate(R.layout.fragment_rocket_list, container, false);

        mContext = container.getContext();

        dataModel = DataModel.getInstance(mContext);
        arrAdapter = new RocketListAdapter(mContext, R.layout.adapter_rocket,
                dataModel.getRockets());

        mComparator = new Rocket.NameCompare();
        arrAdapter.sort(mComparator);
        ListView lView = (ListView) rootView.findViewById(R.id.listView);
        lView.setAdapter(arrAdapter);
        TextView emptyText = (TextView) rootView.findViewById(R.id.empty_list);
        lView.setEmptyView(emptyText);

        //This fragment will always be a child to RocketPageHandler
        lView.setOnItemClickListener((AdapterView.OnItemClickListener)getParentFragment());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_rocket) {
            PopupMenu popup = new PopupMenu(mContext, getActivity().findViewById(R.id.sort_rocket));
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.sort_rocket, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.sort_name:
                setmComparator(new Rocket.NameCompare());
                updateList();
                return true;
            case R.id.sort_flights:
                setmComparator(new Rocket.FlightCountCompare());
                updateList();
                return true;
            case R.id.sort_max_alt:
                setmComparator(new Rocket.AltitudeCompare());
                updateList();
                return true;
        }
        return false;
    }

    private void setmComparator(Rocket.RocketCompare rocketComparator) {
        if (mComparator.getType() == rocketComparator.getType())
            mComparator.flipSort();
        else {
            mComparator = rocketComparator;
        }
    }
}