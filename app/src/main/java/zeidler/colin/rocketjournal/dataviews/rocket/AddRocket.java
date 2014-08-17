package zeidler.colin.rocketjournal.dataviews.rocket;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-28.
 */
public class AddRocket extends ActionBarActivity {

    private DataModel model;
    private boolean editing;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private File TEMP_CAMERA_FILE;
    private Bitmap picture;
    private Bitmap thumbnail;

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
                        if (saveToDB(rocket))
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

    public void loadImage(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            TEMP_CAMERA_FILE = new File(Environment.getExternalStorageDirectory(),
                    "temp.jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(TEMP_CAMERA_FILE));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     *
     * @param rocket the rocket to update, null if creating a new one
     * @return true if save succeeded, false if it was aborted
     */
    public boolean saveToDB(Rocket rocket) {
        TextView name = (TextView) findViewById(R.id.rocket_name);
        TextView weight = (TextView) findViewById(R.id.rocket_weight);

        if (rocket == null)
            rocket = new Rocket(-1);
        rocket.setName(name.getText().toString());

        float w;
        try {
            w = Float.parseFloat(weight.getText().toString());
        } catch (NumberFormatException e) {
            Toast error = Toast.makeText(getApplicationContext(),
                    getResources().getText(R.string.error_invalid_weight),
                    Toast.LENGTH_SHORT);
            error.show();
            return false;
        }

        rocket.setImage(saveImageToStorage(rocket.getId()));

        rocket.setWeight(w);
        if (!editing)
            model.addRocket(rocket);
        else
            model.update(rocket);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView iView = (ImageView) findViewById(R.id.rocket_image);

            //Full image
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            picture = BitmapFactory.decodeFile(TEMP_CAMERA_FILE.getAbsolutePath(),
                    bmOptions);
            iView.setImageBitmap(picture);
            TEMP_CAMERA_FILE.delete();
        }
    }

    private String saveImageToStorage(int rocketID) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        //path to /data/data/<app>/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        final File myPic = new File(directory, "rocket"+rocketID+".jpg");

        Thread saveImage = new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream picFOS;
                try {
                    picFOS = new FileOutputStream(myPic);
                    picture.compress(Bitmap.CompressFormat.PNG, 85, picFOS);
                    picFOS.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        saveImage.start();
        Log.i("PICTURE PATH", myPic.getAbsolutePath());
        return myPic.getAbsolutePath();
    }

    public static class AddRocketView extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_rocket, container, false);

            final Rocket rocket = (Rocket) getArguments().getSerializable("rocket");

            if (rocket != null) {
                TextView name = (TextView) rootView.findViewById(R.id.rocket_name);
                TextView weight = (TextView) rootView.findViewById(R.id.rocket_weight);
                ImageView image = (ImageView) rootView.findViewById(R.id.rocket_image);

                String imageS = rocket.getImage();
                if (!imageS.equals("")) {
//                loadimage from disk
                } else {
                    image.setImageDrawable(rootView.getResources().getDrawable(R.drawable.default_rocket));
                }

                name.setText(rocket.getName());
                weight.setText(String.valueOf(rocket.getWeight()));
            }

            return rootView;
        }
    }
}
