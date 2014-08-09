package zeidler.colin.rocketjournal.dataviews.flightlog.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import zeidler.colin.rocketjournal.dataviews.flightlog.AddFlightLog;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.FlightLog;

/**
 * Created by Colin on 2014-07-15.
 *
 * Activity to display the detailed contents of a single rocket journal entry
 * specific Journal is based on the one selected from the JournalListViewActivity
 */
public class FlightLogDetailActivity extends ActionBarActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_view);
        FlightLogDetailFragment jView = new FlightLogDetailFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_view, jView)
                    .commit();
        }

        mContext = this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FlightLog flightLog = DataModel.getInstance(mContext).getFlightLog(getIntent().getExtras().getInt("Journal"));
        switch (item.getItemId()) {
            case R.id.delete_menu:
                DataModel.getInstance(this).deleteFlightLog(flightLog.getId());
                finish();
                return true;
            case R.id.edit_menu:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AddFlightLog.class);
                intent.putExtra("Journal", flightLog);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
