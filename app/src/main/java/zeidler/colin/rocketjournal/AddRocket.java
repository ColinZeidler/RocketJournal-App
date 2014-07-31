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
import android.widget.TextView;

import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-28.
 */
public class AddRocket extends ActionBarActivity {

    private DataModel model;
    private boolean editing;
    protected Rocket testValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editing = false;

        Bundle extras = getIntent().getExtras();
        final Rocket rocket;
        if (extras != null) {
            rocket = (Rocket) extras.getSerializable("Rocket");
        } else {
            rocket = null;
        }

        //Inflate Done/Cancel actionbar view
        final LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.actionbar_done_cancel, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveToDB(rocket);
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

        setContentView(R.layout.activity_new_rocket);
        if (savedInstanceState == null) {
            AddRocketView aRV = new AddRocketView();
            Bundle args = new Bundle();
            args.putSerializable("rocket", rocket);
            aRV.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.add_r_container, aRV)
                    .commit();
        }
        Context context = this;
        model = DataModel.getInstance(context);

        if (rocket != null) {
            editing = true;
        }

    }

    public void saveToDB(Rocket rocket) {
        TextView name = (TextView) findViewById(R.id.rocket_name);
        TextView weight = (TextView) findViewById(R.id.rocket_weight);

        if (rocket == null)
            rocket = new Rocket(-1);
        rocket.setName(name.getText().toString());
        rocket.setWeight(Float.parseFloat(weight.getText().toString()));
        if (!editing)
            model.addRocket(rocket);
        else
            model.update(rocket);
    }

    public static class AddRocketView extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_rocket, container, false);

            final Rocket rocket = (Rocket) getArguments().getSerializable("rocket");

            if (rocket != null) {
                TextView name = (TextView) rootView.findViewById(R.id.rocket_name);
                TextView weight = (TextView) rootView.findViewById(R.id.rocket_weight);

                name.setText(rocket.getName());
                weight.setText(String.valueOf(rocket.getWeight()));
            }

            return rootView;
        }
    }
}
