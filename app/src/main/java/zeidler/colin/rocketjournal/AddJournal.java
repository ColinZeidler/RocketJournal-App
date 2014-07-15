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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import zeidler.colin.rocketjournal.dbmanager.DataManager;

/**
 * Created by Colin on 2014-07-10.
 */
public class AddJournal extends Activity{

    private Context context;
    private AddJournalView addJView;
    private DataManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journal);
        addJView = new AddJournalView();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.add_j_container, addJView)
                    .commit();
        }
        context = this;

        db = DataManager.getInstance(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                finish();
                return true;
            case R.id.action_done:
                saveToDB();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDB() {
        TextView name = (TextView) findViewById(R.id.name_field);
        TextView motor = (TextView) findViewById(R.id.motor_field);
        TextView delay = (TextView) findViewById(R.id.delay_field);
        TextView weight = (TextView) findViewById(R.id.weight_field);
        TextView notes = (TextView) findViewById(R.id.notes_field);

        Spinner res = (Spinner) findViewById(R.id.spinner);

        Journal j = new Journal(name.getText().toString(), motor.getText().toString(),
                Integer.parseInt(delay.getText().toString()), new Date(), notes.getText().toString(),
                Journal.LaunchRes.fromString(res.getSelectedItem().toString()),
                Float.parseFloat(weight.getText().toString()));

        db.addJournal(j);
    }

    public static class AddJournalView extends Fragment {

        public AddJournalView() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_journal, container, false);
            Spinner results = (Spinner) rootView.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(container.getContext(),
                    R.array.results_array, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            results.setAdapter(adapter);
            return rootView;
        }
    }
}
