package zeidler.colin.rocketjournal.dataviews;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

import zeidler.colin.rocketjournal.JournalListAdapter;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.Journal;
import zeidler.colin.rocketjournal.dbmanager.DataManager;

/**
 * Created by Colin on 2014-07-21.
 *
 */
public class JournalListViewFragment extends Fragment {

    private DataManager dbManager;
    private List<Journal> journals;
    private Context context;
    private JournalListAdapter arrAdapter;

    public JournalListViewFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);

        context = container.getContext();

        dbManager = DataManager.getInstance(context);
        journals = dbManager.getAllJournals();
        arrAdapter = new JournalListAdapter(context, R.layout.journal_adapter,
                journals);

        ListView lView = (ListView) rootView.findViewById(R.id.listView);
        lView.setAdapter(arrAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Journal j = (Journal)view.getTag();
                Intent intent = new Intent();
                intent.setClass(context, JournalViewActivity.class);
                intent.putExtra("Journal", j);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void addNewTestItem() {
        Journal j = new Journal("Stellar", "H255", 5, new Date(), "", Journal.LaunchRes.SUCCESS, 4.0f);
        dbManager.addJournal(j);
        updateList();
    }

    public void deleteAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbManager.deleteAll();
                updateList();
            }
        }).start();
    }

    public void updateList() {
        journals.clear();
        journals.addAll(dbManager.getAllJournals());
        arrAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }
}