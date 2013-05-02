package com.dealeronlinemarketing.lando;

import java.io.IOException;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

@TargetApi(11)
public class CameraSurfaceFragment extends Fragment implements SurfaceHolder.Callback {
	// Pointer to the activity that this fragment has been associated with.
	Activity thisActivity = getActivity();
	SurfaceView mSurfaceView;
	SurfaceHolder mSurfaceHolder;
	Camera mCamera;
	boolean mPreviewRunning;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 thisActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);
		 thisActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 thisActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 thisActivity.setContentView(R.layout.fragment_camera_surface);
		 mSurfaceView = (SurfaceView) thisActivity.findViewById(R.id.fragment_camera_surface);
		 mSurfaceHolder = mSurfaceView.getHolder();
		 mSurfaceHolder.addCallback(this);
		 mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		thisActivity.getMenuInflater().inflate(R.menu.camera_view, menu);
		return true;
	}
	
	// Implementions for the three required functions in SurfaceHolder.Callback
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}
		
		Camera.Parameters p = mCamera.getParameters();
		p.setPreviewSize(w, h);
		mCamera.setParameters(p);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mCamera.startPreview();
		mPreviewRunning = true;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release();
	}

	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {

		}
	};
}
