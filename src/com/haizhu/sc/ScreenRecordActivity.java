package com.haizhu.sc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.haizhu.screenrecord.R;

public class ScreenRecordActivity extends Activity implements OnClickListener {

    private static final int REQUEST_CODE = 111;

    private static final String TAG = "ScreenRecordActivity";

    private Button mRunButton;

    private MediaProjectionManager mMediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mRunButton = (Button) findViewById(R.id.run);
        mRunButton.setOnClickListener(this);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Log.e(TAG, "data=" + data);
        if (data != null) {
            Log.e(TAG, "" + data.getData() + "  " + data.getAction() + "  " + data.getDataString()
                    + "   " + data.getExtras());
        }
        MediaProjection projection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        if (projection != null) {
            Log.e(TAG, "projection=" + projection);
            startProjection(projection);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startProjection(final MediaProjection projection) {
        VirtualDisplay.Callback callback = new VirtualDisplay.Callback() {

            public void onResumed() {
                Log.e(TAG, "");
            };

            @Override
            public void onPaused() {
                Log.e(TAG, "onPaused");
            }

            @Override
            public void onStopped() {
                Log.e(TAG, "onPaused");
            }
        };
        
        Surface surface = null;

        projection.createVirtualDisplay("haizhuDisplay", 1080, 1920, getResources()
                .getDisplayMetrics().densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                surface, callback, new Handler(Looper.getMainLooper()));
        
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                projection.stop();
            }
        }, 20000);
    }

    @Override
    public void onClick(View v) {
        Intent intent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(intent, REQUEST_CODE);

    }
}
