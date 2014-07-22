package zeidler.colin.rocketjournal.listviews;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;

import zeidler.colin.rocketjournal.data.Journal;
import zeidler.colin.rocketjournal.R;

/**
 * Created by Colin on 2014-07-21.
 *
 */
public class JournalViewFrag extends Fragment {

    private Context context;
    private View rootView;
    private Journal journal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_journal_view, container, false);
        context = container.getContext();
        journal = (Journal) getActivity().getIntent().getExtras().getSerializable("Journal");
        populate();
        return rootView;
    }

    public void populate() {
        TextView nameV = (TextView) rootView.findViewById(R.id.r_name);
        TextView motorV = (TextView) rootView.findViewById(R.id.r_motor);
        TextView weightV = (TextView) rootView.findViewById(R.id.r_weight);
        TextView resV = (TextView) rootView.findViewById(R.id.r_res);
        TextView dateV = (TextView) rootView.findViewById(R.id.r_date);
        TextView noteV = (TextView) rootView.findViewById(R.id.r_notes);

        nameV.setText(journal.getrName());

        String motorT = journal.getMotor() + " " + journal.getDelay() + " seconds";
        motorV.setText(motorT);

        String weightT = journal.getWeight() + " lbs";
        weightV.setText(weightT);

        resV.setText(journal.getResult().toString());

        Format formatter = new SimpleDateFormat("dd EEE, yyyy: hh:mm a");
        String dateT = formatter.format(journal.getLaunchDate());
        dateV.setText(dateT);

        noteV.setText(journal.getNotes());
    }
}
