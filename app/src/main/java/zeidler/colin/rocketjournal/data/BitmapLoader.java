package zeidler.colin.rocketjournal.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Colin on 2014-08-17.
 *
 * Load bitmap image in the background.
 */
public class BitmapLoader extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;

    public BitmapLoader(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null)
                imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String dir = params[0];
        Bitmap bm = null;
        try {
            File f = new File(dir);
            bm = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
