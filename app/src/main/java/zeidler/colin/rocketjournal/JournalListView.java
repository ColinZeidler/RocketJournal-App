package zeidler.colin.rocketjournal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

import zeidler.colin.rocketjournal.dbmanager.DataManager;
import zeidler.colin.rocketjournal.view.JournalView;


public class JournalListView extends Activity {

    private Context context;
    private JournalList jList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        jList = new JournalList();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, jList)
                    .commit();
        }

        context = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.add_test:
                Intent intent = new Intent();
                intent.setClass(context, AddJournal.class);
                startActivity(intent);
                return true;
            case R.id.action_delete_all:
                jList.deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("JournalListA", "resumed");
    }

    public static class JournalList extends Fragment {

        private DataManager dbManager;
        private List<Journal> journals;
        private Context context;
        private JournalListAdapter arrAdapter;

        public JournalList() {
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
                    intent.setClass(context, JournalView.class);
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
            Log.d("JournalListF", "resumed");
            updateList();
        }
    }
}
