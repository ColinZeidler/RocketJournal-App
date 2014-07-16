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
import android.widget.TextView;


import java.text.Format;
import java.text.SimpleDateFormat;

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
        //load Journal object and
        Journal journal = (Journal) getIntent().getExtras().getSerializable("Journal");
        jView.populate(journal);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static class JournalViewFrag extends Fragment {

        private Context context;
        private View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_journal_view, container, false);
            context = container.getContext();
            return rootView;
        }

        public void populate(Journal journal) {
            TextView nameV = (TextView) rootView.findViewById(R.id.r_name);
            TextView motorV = (TextView) rootView.findViewById(R.id.r_motor);
            TextView weightV = (TextView) rootView.findViewById(R.id.r_weight);
            TextView resV = (TextView) rootView.findViewById(R.id.r_res);
            TextView dateV = (TextView) rootView.findViewById(R.id.r_date);
            TextView noteV = (TextView) rootView.findViewById(R.id.r_notes);

            nameV.setText(journal.getrName());

            String motorT = journal.getMotor() + " " + journal.getDelay() + " seconds";
            motorV.setText(motorT);

            String weightT = journal.getWeight() + " lbs";
            weightV.setText(weightT);

            resV.setText(journal.getResult().toString());

            Format formatter = new SimpleDateFormat("dd EEE, yyyy: hh:mm a");
            String dateT = formatter.format(journal.getLaunchDate());
            dateV.setText(dateT);

            noteV.setText(journal.getNotes());
        }
    }
}