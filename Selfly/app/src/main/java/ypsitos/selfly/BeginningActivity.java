package ypsitos.selfly;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ypsitos.selfly.data.Datum;
import ypsitos.selfly.instagram.InstagramAPIResults;
import ypsitos.selfly.remote.InstagramAPI;

import com.google.api.client.json.JsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.squareup.picasso.Target;

import static junit.framework.Assert.assertEquals;

import com.google.api.client.extensions.android.http.AndroidHttp;



public class BeginningActivity extends AppCompatActivity {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyCHP_CTB1G_C3xuCQ6HgD-1OWakFyfGkZ0";
    public static final String FILE_NAME = "temp.jpg";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    public TextView mSampleTv;
    public String mUrl;
    ArrayList<Target> targetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBeginning);
        setSupportActionBar(toolbar);

        mSampleTv = (TextView) findViewById(R.id.testTv);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;

        ShowcaseView.Builder showCaseBuilder = new ShowcaseView.Builder(BeginningActivity.this);
        showCaseBuilder.setTarget(new ViewTarget(fab));
        showCaseBuilder.setContentTitle("To Use Selfly..");
        showCaseBuilder.setContentText("Press The Sync Button! It Lets Us Find Where Your Beautiful Selfies Are Hiding! :)");

        showCaseBuilder.build();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstagramAPI.Factory.getInstance().getInstagram().enqueue(new Callback<InstagramAPIResults>() {
                    List<Datum> data = new ArrayList<Datum>();

                    @Override
                    public void onResponse(Call<InstagramAPIResults> call, Response<InstagramAPIResults> response) {
                        if (response.isSuccessful()) {
                            Log.d(BeginningActivity.class.getName(), "Call was successful");


                            data.addAll(response.body().getData());
                            for (int i = 0; i < data.size(); i++) {
                                mUrl = (data.get(i).getImages().getStandardResolution().getUrl());
                                Log.e("Url", mUrl);
                                if (mUrl != null) {
                                    newTarget(mUrl);
                                    Log.e(BeginningActivity.class.getName(), "targetList size: " + targetList.size() + "   URL: " + mUrl);
                                    Picasso.with(BeginningActivity.this).load(mUrl).into(targetList.get(i));
                                }
                            }
                        }
                    }

                    public void onFailure(Call<InstagramAPIResults> results, Throwable t) {
                        Log.e("Failed", t.getMessage());
                    }
                });
            }
        });
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(this, CAMERA_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }


    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
            startCamera();
        }
    }

    private void newTarget(final String url) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.v("Added 1", "Added 1");
                FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();
                Detector<Face> safeDetector = new SafeFaceDetector(detector);
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face> faces = safeDetector.detect(frame);

                if (!safeDetector.isOperational()) {
                    // Note: The first time that an app using face API is installed on a device, GMS will
                    // download a native library to the device in order to do detection.  Usually this
                    // completes before the app is run for the first time.  But if that download has not yet
                    // completed, then the above call will not detect any faces.
                    //
                    // isOperational() can be used to check if the required native library is currently
                    // available.  The detector will automatically become operational once the library
                    // download completes on device.
                    Log.w(TAG, "Face detector dependencies are not yet available.");

                    // Check for low storage.  If there is low storage, the native library will not be
                    // downloaded, so detection will not become operational.
                    IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                    boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                    if (hasLowStorage) {
                        Toast.makeText(BeginningActivity.this, "Low Storage", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Low Storage");
                    }
                }

                FaceView overlay = (FaceView) findViewById(R.id.faceView);
                overlay.setContent(bitmap, faces);

                // Although detector may be used multiple times for different images, it should be released
                // when it is no longer needed in order to free native resources.
                Log.e("Released", "Released");
                safeDetector.release();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Log.e("Made it to list", "Made it to list");
        targetList.add(target);
    }
}



