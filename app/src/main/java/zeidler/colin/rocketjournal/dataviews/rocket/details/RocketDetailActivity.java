package zeidler.colin.rocketjournal.dataviews.rocket.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import zeidler.colin.rocketjournal.dataviews.flightlog.AddFlightLog;
import zeidler.colin.rocketjournal.dataviews.rocket.AddRocket;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-26.
 */
public class RocketDetailActivity extends ActionBarActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_rocket_view);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_view, new RocketDetailFragment())
                    .commit();
        }

        mContext = this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rocket_details, menu);
        return true;
    }
}
