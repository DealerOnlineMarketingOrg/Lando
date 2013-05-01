package com.dealeronlinemarketing.lando;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class CameraView extends Activity implements SurfaceHolder.Callback {
	SurfaceView mSurfaceView;
	SurfaceHolder mSurfaceHolder;
	Camera mCamera;
	boolean mPreviewRunning;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 getWindow().setFormat(PixelFormat.TRANSLUCENT);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 setContentView(R.layout.camera_surface);
		 mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		 mSurfaceHolder = mSurfaceView.getHolder();
		 mSurfaceHolder.addCallback(this);
		 mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_view, menu);
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
