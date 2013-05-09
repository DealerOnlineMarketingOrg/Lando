package com.example.landodev;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraPreviewActivity extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "Preview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    // holds the context of the calling activity.
    private Context mContext;
    
    public CameraPreviewActivity(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mContext = context;
        
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
    
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
    		// Preview doesn't exist at this point. It probably hasn't been started.
    		// This isn't an error, and since we want an unstarted preview past this
    		//   point, do nothing (but log event).
    		Log.d(TAG, "camera preview doesn't exist: " + e.getMessage());
    	}
    	
    	// Do changes to preview here.
    	
    	// This routine rotates the camera in response to the device's current rotation,
    	//  in order to keep it at the correct orientation.
        Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if(display.getRotation() == Surface.ROTATION_0)
        {
            parameters.setPreviewSize(height, width);                           
            mCamera.setDisplayOrientation(90);
        }
        if(display.getRotation() == Surface.ROTATION_90)
        {
            parameters.setPreviewSize(width, height);                           
        }
        if(display.getRotation() == Surface.ROTATION_180)
        {
            parameters.setPreviewSize(height, width);               
        }
        if(display.getRotation() == Surface.ROTATION_270)
        {
            parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(180);
        }
        mCamera.setParameters(parameters);
    	
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