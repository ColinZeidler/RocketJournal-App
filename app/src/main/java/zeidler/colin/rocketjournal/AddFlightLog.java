package zeidler.colin.rocketjournal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.FlightLog;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-10.
 */
public class AddFlightLog extends ActionBarActivity {

    private DataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflate Done/Cancel actionbar view
        final LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.actionbar_done_cancel, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveToDB();
                        finish();
                    }
                }
        );
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        //END of custom actionbar

        setContentView(R.layout.activity_new_journal);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.add_j_container, new AddJournalView())
                    .commit();
        }
        Context context = this;
        model = DataModel.getInstance(context);

    }

    private void saveToDB() {
        TextView motor = (TextView) findViewById(R.id.motor_field);
        TextView delay = (TextView) findViewById(R.id.delay_field);
        TextView notes = (TextView) findViewById(R.id.notes_field);

        Spinner res = (Spinner) findViewById(R.id.spinner);
        Spinner rockets = (Spinner) findViewById(R.id.rocket_spinner);

        Rocket r = (Rocket) rockets.getSelectedItem();

        FlightLog j = new FlightLog(-1, r.getId(),//TODO make this select the Rocket used
                motor.getText().toString(),
                Integer.parseInt(delay.getText().toString()),
                new Date(), notes.getText().toString(),
                FlightLog.LaunchRes.fromString(res.getSelectedItem().toString()));

        model.addFlightLog(j);
    }

    public static class AddJournalView extends Fragment {

        public AddJournalView() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_flightlog, container, false);

            Spinner results = (Spinner) rootView.findViewById(R.id.spinner);
            Spinner rockets = (Spinner) rootView.findViewById(R.id.rocket_spinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, FlightLog.LaunchRes.names());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            results.setAdapter(adapter);

            ArrayAdapter<Rocket> rocketAdapter = new ArrayAdapter<Rocket>(container.getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    DataModel.getInstance(container.getContext()).getRockets());
            rocketAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rockets.setAdapter(rocketAdapter);

            //TODO populate rocket_spinner with list of rocket objects, need custom spinner adapter
            return rootView;
        }
    }
}
