package ypsitos.selfly;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView;
    FloatingActionButton mCameraFab;
    FloatingActionButton mSaveFab;
    String mCurrentPhotoPath;
    CameraPermissions cameraPermissions = new CameraPermissions(CameraActivity.this);
    File photoFile;
    int mCount;
    TextView mScoreTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mImageView = (ImageView) findViewById(R.id.usersPhoto);
        mScoreTv = (TextView)findViewById(R.id.scoreTv);

        mCameraFab = (FloatingActionButton) findViewById(R.id.cameraFab);
        mSaveFab = (FloatingActionButton)findViewById(R.id.addFab);

        if(mCount < 1) {
            ShowcaseView.Builder showCaseBuilder = new ShowcaseView.Builder(CameraActivity.this);
            showCaseBuilder.setTarget(new ViewTarget(mCameraFab));
            showCaseBuilder.setContentText("Time To Put Your Best Self Forward! Press The Camera Button And Smile! (:");
            showCaseBuilder.build();
        }

        mCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
            Log.v("note", "note");
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap)extras.get("data");
                FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();
                Detector<Face> safeDetector = new SafeFaceDetector(detector);
                Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
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
                    Log.w("", "Face detector dependencies are not yet available.");

                    // Check for low storage.  If there is low storage, the native library will not be
                    // downloaded, so detection will not become operational.
                    IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                    boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                    if (hasLowStorage) {
                        Toast.makeText(CameraActivity.this, "Low Storage", Toast.LENGTH_LONG).show();
                        Log.w("", "Low Storage");
                    }
                }

                FaceView overlay = (FaceView) findViewById(R.id.faceView2);
                overlay.setContent(imageBitmap, faces);
                safeDetector.release();
                mCount++;
                mScoreTv.setVisibility(View.VISIBLE);
                mScoreTv.setText("You Are " + Math.random() + "% Yourselfly!");
                mSaveFab.setVisibility(View.VISIBLE);
                mSaveFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        galleryAddPic();
                        Toast.makeText(CameraActivity.this, "Saved To Gallery!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {

        if (!cameraPermissions.checkPermissionForCamera()) {
            cameraPermissions.requestPermissionForCamera();
        } else {
            if (!cameraPermissions.checkPermissionForExternalStorage()) {
                cameraPermissions.requestPermissionForExternalStorage();
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}