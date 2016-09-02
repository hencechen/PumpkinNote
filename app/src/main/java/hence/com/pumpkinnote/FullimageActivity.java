package hence.com.pumpkinnote;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.rey.material.widget.LinearLayout;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Hence on 2016/8/22.
 */

public class FullimageActivity extends AppCompatActivity {
    private ImageView imageView;
    public static final String LOG_TAG = "Log";
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimage_layout);

        imageView = (ImageView) findViewById(R.id.full_image);

        Intent intent = getIntent();
        String filepath = intent.getStringExtra("IMG_URI");
        Log.d(LOG_TAG, "full image path" +filepath);
        File imgFile = new File(filepath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            imageView.setAdjustViewBounds(true);
        }
        mAttacher = new PhotoViewAttacher(imageView);

    }

}
