package com.example.landodev;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreviewActivity extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "Preview";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreviewActivity(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // Depreciated setting for Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Log.d(TAG, "constructor: mCamera: " + mCamera);
        Log.d(TAG, "constructor: mHolder: " + mHolder);
    }

    public void surfaceCreated(SurfaceHolder holder) {
    	// Hook up camera to surface so it can draw the live preview.
    	//try {
    		// These two lines on surfaceCreated may be causing a camera error 100.
    		// Do these only in surfaceChanged.
    		// Note: removal of the two following lines solves the camera error 100
    		//  (and camera service crash) problem.
    		// This problem is known to affect only some devices. Leaving out should
    		//  work for all devices.
    		//mCamera.setPreviewDisplay(holder);
    		//mCamera.startPreview();
    	// without .setPreviewDisplay, catch isn't needed.
    	//} catch (IOException e) {
    		// Something went wrong when starting up the camera.
    	//	Log.d(TAG, "Error setting camera preview: " + e.getMessage());
    	//}
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    	// Camera is being held by the calling activity. Make sure it's released.
    	Log.d(TAG, "Surface was destroyed.");
    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	if (mHolder.getSurface() == null) {
    		// There is no surface.
    		Log.d(TAG, "There is no surface.");
    		return;
    	}
    	
    	// Stop preview, make changes, then start back up again.
    	try {
    		mCamera.stopPreview();
    		Log.d(TAG, "Preview stopped.");
    	} catch (Exception e) {
    		// Preview doesn't exist at this point. The camera isn't on,
    		//  doesn't exist, or is being used by something else at this
    		//  moment.
    		Log.d(TAG, "Error: camera preview doesn't exist: " + e.getMessage());
    	}
    	
    	// Do changes to preview here.
    	
    	// Restart preview
    	try {
    		mCamera.setPreviewDisplay(holder);
    		mCamera.startPreview();
    		Log.d(TAG, "Preview restarted.");
    	} catch (Exception e) {
    		// Something went wrong with starting the camera.
    		Log.d(TAG, "Error re-setting camera preview: " + e.getMessage());
    	}
    }
}