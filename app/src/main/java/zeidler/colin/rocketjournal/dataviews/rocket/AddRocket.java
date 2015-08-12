package zeidler.colin.rocketjournal.dataviews.rocket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;

import zeidler.colin.rocketjournal.R;
import zeidler.colin.rocketjournal.data.BitmapLoader;
import zeidler.colin.rocketjournal.data.DataModel;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-28.
 *
 * handles the creation of a new rocket or editing an old one
 */
public class AddRocket extends ActionBarActivity {

    private DataModel model;
    private boolean editing;
    private static final int REQUEST_GALLERY_IMAGE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int IMAGE_SCALE_WIDTH = 1000;
    private static final int IMAGE_SCALE_HEIGHT = 1000;
    private static final String TEMP_IMAGE_NAME = "temp.jpg";
    private File TEMP_CAMERA_FILE;
    private Bitmap picture;
    private String imageLocation = "";
    private boolean camera = false;
    private Context mContext;

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
        mContext = this;
        model = DataModel.getInstance(mContext);

        if (rocket != null) {
            editing = true;
        }

    }

    /**
     * called when the image of the rocket is clicked on
     * @param v the View object that was selected
     */
    public void loadImage(View v) {
//        Log.i("AddRocket", "loadImage");
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle(R.string.imageDialogTitle)
//                .setItems(R.array.imageDialogArray, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
                                Log.i("AddRocket", "Camera");
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    TEMP_CAMERA_FILE = new File(Environment.getExternalStorageDirectory(),
                                            TEMP_IMAGE_NAME);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(TEMP_CAMERA_FILE));
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
//                                break;
//                            case 1:
//                                Log.i("AddRocket", "Gallery");
//                                Intent getPictureIntent = new Intent();
//                                getPictureIntent.setType("image/*");
//                                getPictureIntent.setAction(Intent.ACTION_GET_CONTENT);
//                                startActivityForResult(Intent.createChooser(getPictureIntent, "Select Picture"), REQUEST_GALLERY_IMAGE);
//                                break;
//                        }
//                    }
//                });
//        builder.show();
    }

    /**
     *
     * @param rocket the rocket to update, null if creating a new one
     * @return true if save succeeded, false if it was aborted
     */
    public boolean saveToDB(Rocket rocket) {
        TextView name = (TextView) findViewById(R.id.rocket_name);
        TextView weight = (TextView) findViewById(R.id.rocket_weight);
        TextView mTD = (TextView) findViewById(R.id.rocket_mTubeDiam);
        TextView mTL = (TextView) findViewById(R.id.rocket_mTubeLen);
        TextView chuteSize = (TextView) findViewById(R.id.rocket_chuteSize);

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
        rocket.setWeight(w);

        if (camera) {
            if (picture != null)
                rocket.setImage(saveImageToStorage(rocket.getId()));
            else
                rocket.setImage("");
        } else {
            rocket.setImage(imageLocation);
        }

        int  mTubeDiam, mTubeLen, cSize;
        try {
            mTubeDiam = Integer.parseInt(mTD.getText().toString());
        } catch (NumberFormatException e) {
            Toast error = Toast.makeText(getApplicationContext(),
                    getResources().getText(R.string.error_invalid_mTD),
                    Toast.LENGTH_SHORT);
            error.show();
            return false;
        }
        rocket.setMotorTubeDiam(mTubeDiam);
        try {
            mTubeLen = Integer.parseInt(mTL.getText().toString());
        } catch (NumberFormatException e) {
            Toast error = Toast.makeText(getApplicationContext(),
                    getResources().getText(R.string.error_invalid_mTL),
                    Toast.LENGTH_SHORT);
            error.show();
            return false;
        }
        rocket.setMotorTubeLen(mTubeLen);
        try {
            cSize = Integer.parseInt(chuteSize.getText().toString());
        } catch (NumberFormatException e) {
            Toast error = Toast.makeText(getApplicationContext(),
                    getResources().getText(R.string.error_invalid_chute),
                    Toast.LENGTH_SHORT);
            error.show();
            return false;
        }
        rocket.setChuteSize(cSize);

        if (!editing)
            model.addRocket(rocket);
        else
            model.update(rocket);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ImageView iView = (ImageView) findViewById(R.id.rocket_image);
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                camera = true;
                //Full image
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                picture = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() +  File.separator + TEMP_IMAGE_NAME,
                        bmOptions);
                picture = scaleDown(picture, IMAGE_SCALE_WIDTH, IMAGE_SCALE_HEIGHT);
                iView.setImageBitmap(picture);
                Log.i("PICTURE", picture.getHeight() + " height");
                Log.i("PICTURE", picture.getWidth() + " width");
                new File(Environment.getExternalStorageDirectory().getPath(), TEMP_IMAGE_NAME).delete();
            } else if (requestCode == REQUEST_GALLERY_IMAGE) {
                camera = false;
                Uri selectedImage = data.getData();
                imageLocation = getPath(selectedImage);
                Log.i("ImageResult", ""+imageLocation);
                Bitmap temp = BitmapFactory.decodeFile(imageLocation);
                iView.setImageBitmap(temp);
            }
        }
    }

    private String getPath(Uri uri) {
        if (uri == null)
            //TODO let user know that load failed
            return null;

        //try to get image from media store first
        String[] projection = { MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(col);
        }
        return uri.getPath();
    }

    private Bitmap scaleDown(Bitmap original, int width, int height) {
        float ratio = Math.min((float) width / original.getWidth(),
                (float) height / original.getHeight());
        width = Math.round(ratio * original.getWidth());
        height = Math.round(ratio * original.getHeight());
        return Bitmap.createScaledBitmap(original, width, height, true);
    }

    private String saveImageToStorage(int rocketID) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        //path to /data/data/<app>/app_imageDir/
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // TODO change this to the public storage dir

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
                TextView mTubeDiam = (TextView) rootView.findViewById(R.id.rocket_mTubeDiam);
                TextView mTubeLen = (TextView) rootView.findViewById(R.id.rocket_mTubeLen);
                TextView chuteSize = (TextView) rootView.findViewById(R.id.rocket_chuteSize);

                String imageS = rocket.getImage();
                if (!imageS.equals("")) {
                    BitmapLoader task = new BitmapLoader(image);
                    task.execute(imageS);
                } else {
                    image.setImageDrawable(rootView.getResources().getDrawable(R.drawable.default_rocket));
                }

                name.setText(rocket.getName());
                weight.setText(String.valueOf(rocket.getWeight()));
                mTubeDiam.setText(String.valueOf(rocket.getMotorTubeDiam()));
                mTubeLen.setText(String.valueOf(rocket.getMotorTubeLen()));
                chuteSize.setText(String.valueOf(rocket.getChuteSize()));
            }

            return rootView;
        }
    }
}
