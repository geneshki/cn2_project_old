package de.tu.darmstadt.cn2.idb34;

import java.util.ArrayList;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import de.tu.darmstadt.cn2.idb34.util.Signal;

public class MainActivity extends ActionBarActivity {
    public static final Logger LOGGER = Logger.getLogger("MainActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
        }
        final Intent intent = new Intent(this, ScaleActivity.class);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("imagePaths", "image.jpg");

        final ArrayList<String> imageNames = new ArrayList<String>();
        imageNames.add("imageA");
        imageNames.add("imageB");
        intent.putStringArrayListExtra("de.tu.darmstadt.cn2.idb34.ImageNames",
                imageNames);
        intent.putExtra("imageA", R.drawable.imagea);
        intent.putExtra("imageB", R.drawable.imageb);
        intent.putExtra("maxHeight", 250);
        intent.putExtra("maxWidth", 250);

        startActivityForResult(intent, 0);
    }

    private static Bitmap convertTo2DUsingGetRGB(Bitmap image) {
        final int width = image.getWidth();
        final int height = image.getHeight();

        final Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        final Canvas c = new Canvas(bmpGrayscale);
        final Paint paint = new Paint();
        final ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        final ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(image, 0, 0, paint);
        return bmpGrayscale;
    }

    private Integer[][] getBitmapMatrix(Bitmap rgbBitmap) {
        final Bitmap bitmap = convertTo2DUsingGetRGB(rgbBitmap);
        final int[][] pixels = new int[bitmap.getHeight()][bitmap.getWidth()];
        for (int i = 0; i < bitmap.getHeight(); i++) {
            bitmap.getPixels(pixels[i], 0, bitmap.getWidth(), 0, i,
                    bitmap.getWidth(), 1);
        }

        final Integer[][] result = new Integer[pixels.length][pixels[0].length];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                result[i][j] = (Integer) pixels[i][j];
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            LOGGER.info("Horray!!! The images are present!");
            final Bitmap bitmapA = (Bitmap) data.getParcelableExtra("imageA");
            final Integer[][] bitmapMatrixA = getBitmapMatrix(bitmapA);
            final Bitmap bitmapB = (Bitmap) data.getParcelableExtra("imageB");
            final Integer[][] bitmapMatrixB = getBitmapMatrix(bitmapB);
            long startTime = System.nanoTime();
            Signal.xcorr_fft2(bitmapMatrixA, bitmapMatrixA);
            long endTime = System.nanoTime();
            long time = endTime - startTime;
            LOGGER.info("FFT:" + time);
            // startTime = System.nanoTime();
            // Signal.xcorr2(bitmapMatrixA, bitmapMatrixA);
            //
            // endTime = System.nanoTime();
            // time = endTime - startTime;
            // LOGGER.info("conventional:" + time);

        } else if (resultCode == Activity.RESULT_CANCELED) {
            throw new IllegalArgumentException("The images were not found!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
