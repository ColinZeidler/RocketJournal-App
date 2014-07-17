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

/**
 * Created by Colin on 2014-07-16.
 *
 */
public class JournalListAdapter extends ArrayAdapter<Journal> {

    private int layoutId;
    private List<Journal> journals;
    private Context mContext;

    public JournalListAdapter(Context context, int layoutId, List<Journal> journals) {
        super(context, layoutId, journals);

        this.mContext = context;
        this.layoutId = layoutId;
        this.journals = journals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutId, null);
        }

        Journal journal = journals.get(position);
        if (journal != null) {
            TextView name = (TextView) v.findViewById(R.id.adapter_name);
            TextView status = (TextView) v.findViewById(R.id.adapter_status);

            name.setText(journal.getrName());
            Format formatter = new SimpleDateFormat("dd EEE, yyyy: hh:mm a");
            String statusT = journal.getResult().toString() + " launched: " + formatter.format(journal.getLaunchDate());
            status.setText(statusT);

            v.setTag(journal);
        }

        return v;
    }
}
