package com.dealeronlinemarketing.lando;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceActivity extends Activity implements SurfaceHolder.Callback {

	SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera camera;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Link up surface content to layout activity_main,
        //  which contains the surface we want to use.
        setContentView(R.layout.activity_main);
    }
    
    @Override
    public void onResume(){
    	// We're starting. Open the camera.
        camera = Camera.open();
        
        // Intialize the surface view held by main.
        mSurfaceView = (SurfaceView)this.findViewById(R.id.surface_view);
	        mSurfaceHolder = mSurfaceView.getHolder(); 
	        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
	        mSurfaceHolder.setSizeFromLayout();
	        mSurfaceHolder.addCallback(this); 
        
        super.onResume();
    }
    
    @Override
    public void onPause(){
        camera.stopPreview();
        camera.release();
        super.onPause();
    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
        	// Link up camera's live-preview to the surface holder.
            camera.setPreviewDisplay(holder);
            // Start the preview.
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }         
    }
 
    public void surfaceCreated(SurfaceHolder holder) {
        try {
        	// Link up camera's live-preview to the surface holder.
            camera.setPreviewDisplay(holder);
            // Start the preview.
            camera.startPreview();
            
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
  
    public void surfaceDestroyed(SurfaceHolder holder) {
    	// Destroy all references.
    	
        camera.stopPreview();
    }
}