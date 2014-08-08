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
import android.widget.Toast;

import java.util.Date;

import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.FlightLog;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-10.
 */
public class AddFlightLog extends ActionBarActivity {

    private DataModel model;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editing = false;

        //null when creating new flight log, not when editing
        final FlightLog flightLog;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            flightLog = (FlightLog) extras.getSerializable("Journal");
        } else {
            flightLog = null;
        }


        //Inflate Done/Cancel actionbar view
        final LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.actionbar_done_cancel, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (saveToDB(flightLog))
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
            AddJournalView aJV = new AddJournalView();
            Bundle args = new Bundle();
            args.putSerializable("log", flightLog);
            aJV.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.add_j_container, aJV)
                    .commit();
        }
        Context context = this;
        model = DataModel.getInstance(context);

        if (flightLog != null) {
            editing = true;
        }
    }

    /**
     *
     * @param fLog the flight log to update, null if creating a new one
     * @return true if save successful, false otherwise
     */
    private boolean saveToDB(FlightLog fLog) {
        TextView motor = (TextView) findViewById(R.id.motor_field);
        TextView delay = (TextView) findViewById(R.id.delay_field);
        TextView notes = (TextView) findViewById(R.id.notes_field);

        Spinner res = (Spinner) findViewById(R.id.spinner);
        Spinner rockets = (Spinner) findViewById(R.id.rocket_spinner);

        Rocket r = (Rocket) rockets.getSelectedItem();

        Date d;
        if (fLog == null) { //creating new FlightLog
            fLog = new FlightLog(-1);
            d = new Date();
        } else {            //Editing old flight log
            d = fLog.getDate();
        }

        fLog.setRocketID(r.getId());
        fLog.setMotor(motor.getText().toString());

        int i;
        try {
            i = Integer.parseInt(delay.getText().toString());
        } catch (NumberFormatException e) {
            Toast error = Toast.makeText(getApplicationContext(),
                    getResources().getText(R.string.error_invalid_num),
                    Toast.LENGTH_SHORT);
            error.show();
            return false;
        }

        fLog.setDelay(i);
        fLog.setNotes(notes.getText().toString());
        fLog.setResult(FlightLog.LaunchRes.fromString(res.getSelectedItem().toString()));
        fLog.setDate(d);

        if (!editing)
            model.addFlightLog(fLog);
        else
            model.update(fLog);

        return true;
    }

    public static class AddJournalView extends Fragment {

        public AddJournalView() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_flightlog, container, false);

            final DataModel model = DataModel.getInstance(container.getContext());
            final FlightLog flightLog = (FlightLog) getArguments().getSerializable("log");

            Spinner results = (Spinner) rootView.findViewById(R.id.spinner);
            Spinner rockets = (Spinner) rootView.findViewById(R.id.rocket_spinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, FlightLog.LaunchRes.names());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            results.setAdapter(adapter);

            ArrayAdapter<Rocket> rocketAdapter = new ArrayAdapter<Rocket>(container.getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    model.getRockets());
            rocketAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rockets.setAdapter(rocketAdapter);

            if (flightLog != null) {
                TextView motor = (TextView) rootView.findViewById(R.id.motor_field);
                TextView delay = (TextView) rootView.findViewById(R.id.delay_field);
                TextView notes = (TextView) rootView.findViewById(R.id.notes_field);

                motor.setText(flightLog.getMotor());
                delay.setText(String.valueOf(flightLog.getDelay()));
                notes.setText(flightLog.getNotes());

                results.setSelection(flightLog.getResult().ordinal());
                rockets.setSelection(model.getRocketPos(flightLog.getRocketID()));
            }

            return rootView;
        }
    }
}
