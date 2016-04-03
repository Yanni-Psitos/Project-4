package ypsitos.selfly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Color;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.Vertex;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ypsitos.selfly.data.Datum;
import ypsitos.selfly.instagram.InstagramAPIResults;
import ypsitos.selfly.remote.InstagramAPI;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.squareup.picasso.Target;

public class BeginningActivity extends AppCompatActivity {

    private static final String TAG = "PhotoViewerActivity";

    TextView mSampleTv;
    String mUrl;
    Target loadTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBeginning);
        setSupportActionBar(toolbar);

        mSampleTv = (TextView) findViewById(R.id.testTv);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
                            if (mUrl != null) {
                                loadBitmap(mUrl);
                            }

                        }
                        }else{
                            Log.d(BeginningActivity.class.getName(), "Error in body");
                        }
                    }

                    public void onFailure(Call<InstagramAPIResults> results, Throwable t) {
                        Log.e("Failed", t.getMessage());
                    }
                });
//                ShowcaseView.Builder showCaseBuilder = new ShowcaseView.Builder(BeginningActivity.this);
//                showCaseBuilder.setTarget(new ViewTarget(fab));
//                showCaseBuilder.setContentTitle(R.string.welcome);
//                showCaseBuilder.setContentText(R.string.showcase_msg);
//
//                showCaseBuilder.build();
            }
        });
    }


    public void loadBitmap(String url) {
        if (loadTarget == null) loadTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // do something with the Bitmap
                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this).load(url).into(loadTarget);
    }
    public void handleLoadedBitmap(Bitmap b){
        // do something here
    }
}

//    public static void imageDownload(Context ctx, String url) {
//        Picasso.with(ctx)
//                .load(url)
//                .into(getTarget(url));
//    }
//
//    private static Target getTarget(final String url) {
//        Target target = new Target() {
//
//            @Override
//            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
//                        try {
//                            file.createNewFile();
//                            FileOutputStream ostream = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
//                            ostream.flush();
//                            ostream.close();
//                        } catch (IOException e) {
//                            Log.e("IOException", e.getLocalizedMessage());
//                        }
//                    }
//                }).start();
//
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        };
//        return target;
//    }
//
//}
