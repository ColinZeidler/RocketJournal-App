package zeidler.colin.rocketjournal.dataviews.rocket.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

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
        getMenuInflater().inflate(R.menu.item_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Rocket rocket = DataModel.getInstance(mContext).getRocket(getIntent().getExtras().getInt("Rocket"));
        switch (item.getItemId()) {
            case R.id.delete_menu:
                DataModel.getInstance(this).deleteRocket(rocket.getId());
                finish();
                return true;
            case R.id.edit_menu:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AddRocket.class);
                intent.putExtra("Rocket", rocket);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
