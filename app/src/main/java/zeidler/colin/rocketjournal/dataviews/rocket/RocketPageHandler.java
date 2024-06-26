package zeidler.colin.rocketjournal.dataviews.rocket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.UpdateList;
import zeidler.colin.rocketjournal.dataviews.rocket.details.RocketDetailActivity;
import zeidler.colin.rocketjournal.dataviews.rocket.details.RocketDetailFragment;

/**
 * Created by Colin on 2014-08-27.
 *
 * Class for handling the Fragments used when in phone layout or tablet layout.
 * If in a phone layout will display the list of rockets,
 * and on click will launch the details activity
 * If in a tablet layout will display the list of rockets,
 * and on click will update the details fragment pane
 */
public class RocketPageHandler extends Fragment implements AdapterView.OnItemClickListener, UpdateList{
    private Context mContext;
    private View rootView;
    private RocketDetailFragment detailFragment;
    private final String listFragmentTag = "RocketList";
    private final String detailFragmentTag = "Details";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.pager_item_rocket, container, false);
        setHasOptionsMenu(true);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction().add(R.id.rocket_list_fragment,
                    new RocketListFragment(), listFragmentTag).commit();
            if (rootView.findViewById(R.id.rocket_details_fragment) != null) {
                if (detailFragment == null)
                    detailFragment = new RocketDetailFragment();
                getChildFragmentManager().beginTransaction().add(R.id.rocket_details_fragment,
                        detailFragment, detailFragmentTag).commit();
            }
        }

        mContext = container.getContext();
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int r = (Integer) view.getTag();
        if (rootView.findViewById(R.id.rocket_details_fragment) != null) {
            detailFragment = (RocketDetailFragment) getChildFragmentManager().findFragmentByTag(detailFragmentTag);
            view.setSelected(true);
            detailFragment.update(r);
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, RocketDetailActivity.class);
            intent.putExtra("Rocket", r);
            startActivity(intent);
        }
    }

    @Override
    public void updateList() {
        ((RocketListFragment) getChildFragmentManager()
                .findFragmentByTag(listFragmentTag)).updateList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rocket_list, menu);
        if (detailFragment != null) {
            inflater.inflate(R.menu.rocket_details, menu);
        }
    }


}
