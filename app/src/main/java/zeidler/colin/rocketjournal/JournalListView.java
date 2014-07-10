package zeidler.colin.rocketjournal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zeidler.colin.rocketjournal.dbmanager.DataManager;


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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings: return true;
            case R.id.add_test: jList.addNewTestItem(); return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public static class JournalList extends Fragment {

        private DataManager dbManager;
        private List<Journal> journals;
        private Context context;
        private ArrayAdapter<Journal> arrAdapter;

        public JournalList() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_journal, container, false);

            context = container.getContext();

            dbManager = DataManager.getInstance(context);
            journals = dbManager.getAllJournals();
            arrAdapter = new ArrayAdapter<Journal>(context, android.R.layout.simple_list_item_1,
                    journals);

            ListView lView = (ListView) rootView.findViewById(R.id.listView);
            lView.setAdapter(arrAdapter);

            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO implement custom arrayAdapter.
//                    Journal j = (Journal)view.getTag();
                }
            });

            return rootView;
        }

        public void addNewTestItem() {
            Journal j = new Journal("Stellar", "H255", 5, new Date(), "", Journal.LaunchRes.SUCCESS, 4.0f);
            dbManager.addJournal(j);

            journals.add(j);
            arrAdapter.notifyDataSetChanged();
        }

        @Override
        public void onResume() {
            super.onResume();
            journals = dbManager.getAllJournals();
            arrAdapter.notifyDataSetChanged();
        }
    }
}
