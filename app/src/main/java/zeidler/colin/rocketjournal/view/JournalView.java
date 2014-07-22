package zeidler.colin.rocketjournal.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.view.JournalViewFrag;

/**
 * Created by Colin on 2014-07-15.
 *
 * Activity to display the detailed contents of a single rocket journal entry
 * specific Journal is based on the one selected from the JournalListView
 */
public class JournalView extends Activity {

    private JournalViewFrag jView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_view);
        jView = new JournalViewFrag();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container_view, jView)
                    .commit();
        }

        context = this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu: return true; //TODO add actual action
            case R.id.edit_menu: return true;   //TODO add actual action
        }
        return super.onOptionsItemSelected(item);
    }



}