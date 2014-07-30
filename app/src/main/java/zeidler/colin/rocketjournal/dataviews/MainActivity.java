package zeidler.colin.rocketjournal.dataviews;

import java.util.Locale;

import android.content.Intent;
import android.support.v4.view.PagerTabStrip;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import zeidler.colin.rocketjournal.AddFlightLog;
import zeidler.colin.rocketjournal.AddRocket;
import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.DataModel;

public class MainActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        PagerTabStrip mPagerTab = (PagerTabStrip) findViewById(R.id.tab_strip);
        mPagerTab.setDrawFullUnderline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataModel.getInstance(this).saveAllData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent();
        switch(id) {
            case R.id.action_settings: return true;
            case R.id.add_item_flightlog:
                intent.setClass(this, AddFlightLog.class);
                startActivity(intent);
                return true;
            case R.id.add_item_rocket:
                intent.setClass(this, AddRocket.class);
                startActivity(intent);
                return true;
            case R.id.delete_all_flightlogs:
                DataModel.getInstance(this).deleteAllFlightLogs();
                return true;
            case R.id.delete_all_rockets:
                DataModel.getInstance(this).deleteAllRockets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: return new RocketListFragment();
                case 1: return new FlightLogListFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section2);
                case 1:
                    return getString(R.string.title_section1);
            }
            return null;
        }
    }
}
