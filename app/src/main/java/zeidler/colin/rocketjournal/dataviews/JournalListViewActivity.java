package zeidler.colin.rocketjournal.dataviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import zeidler.colin.rocketjournal.AddJournal;
import zeidler.colin.rocketjournal.R;


public class JournalListViewActivity extends Activity {

    private Context context;
    private JournalListViewFragment jList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        jList = new JournalListViewFragment();
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

}
