package zeidler.colin.rocketjournal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-26.
 */
public class RocketListAdapter extends ArrayAdapter<Rocket> {

    private Context context;
    private int layoutID;
    private List<Rocket> rockets;

    public RocketListAdapter(Context context, int resource, List<Rocket> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.rockets = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layoutID, null);
        }

        ImageView iView = (ImageView) v.findViewById(R.id.rocket_image);

        Rocket rocket = rockets.get(position);
        if (rocket != null) {
            String image = rocket.getImage();
//        if (!image.equals("")) {
//            //loadimage from disk
//        } else {
            iView.setImageDrawable(v.getResources().getDrawable(R.drawable.default_rocket));
//        }

            TextView nameView = (TextView) v.findViewById(R.id.rocket_name);
            TextView flightView = (TextView) v.findViewById(R.id.flight_count);

            nameView.setText(rocket.getName());
            flightView.setText(rocket.getFlightCount() + " flights");   //TODO use resource instead of hardcode

            v.setTag(rocket);
        }
        return v;
    }
}
