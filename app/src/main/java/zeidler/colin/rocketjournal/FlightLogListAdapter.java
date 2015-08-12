package zeidler.colin.rocketjournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.FlightLog;

/**
 * Created by Colin on 2014-07-16.
 *
 */
public class FlightLogListAdapter extends ArrayAdapter<FlightLog> {

    private int layoutId;
    private List<FlightLog> flightLogs;
    private Context mContext;

    public FlightLogListAdapter(Context context, int layoutId, List<FlightLog> flightLogs) {
        super(context, layoutId, flightLogs);

        this.mContext = context;
        this.layoutId = layoutId;
        this.flightLogs = flightLogs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutId, null);
        }

        FlightLog flightLog = flightLogs.get(position);
        if (flightLog != null) {
            TextView name = (TextView) v.findViewById(R.id.adapter_name);
            TextView status = (TextView) v.findViewById(R.id.adapter_status);

            String n = DataModel.getInstance(mContext).getRocket(flightLog.getRocketID()).getName();
            n = n + ", " + flightLog.getMotor();
            name.setText(n);
            Format formatter = new SimpleDateFormat(v.getResources().getString(R.string.date_format_use));
            String statusT = flightLog.getResult().toString() + ", launched: " + formatter.format(flightLog.getDate());
            status.setText(statusT);

            v.setTag(flightLog.getId());
        }

        return v;
    }
}
