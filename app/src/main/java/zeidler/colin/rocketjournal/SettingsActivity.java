package zeidler.colin.rocketjournal;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Colin on 2014-08-14.
 */
public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_PREF_DATEFORMAT = "pref_date_format";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
