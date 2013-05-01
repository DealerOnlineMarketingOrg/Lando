package com.dealeronlinemarketing.lando;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
//import android.support.v4.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

// This is the javascript -> java interface.
// Used for java-functions that are used and called in the
//  jquery-mobile site via javascript (Android.function());
public class WebAppInterface {
	
    Context mContext;
    private static final int CAMERA_PIC_REQUEST = 1337;
    
    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    // Adds the camera fragment to the view.
    public void createCameraView() {
    	//FragmentManager fragmentManger = getFragmentManager();
    	//FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
    	//CameraFragment fragment = new CameraFragment();
		//fragmentTransaction.add(R.id.surface_camera, fragment);
		//fragmentTransaction.commit();
    }
    
    // Removes the camera fragment from the view.
    public void removeCameraView() {
    	
    }
    
    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    
    private class capabilities {
    	boolean hasNetwork;
    	boolean hasBackCamera;
    	boolean hasTwitterInstalled;
    	boolean hasFacebookInstalled;
    } capabilities Capabilities;
    
    private void checkCapabilities() {
    	Capabilities.hasNetwork = isNetworkAvailable();
    	Capabilities.hasBackCamera = isCameraAvailable();
    	Capabilities.hasTwitterInstalled = isTwitterAvailable();
    	Capabilities.hasFacebookInstalled = isFacebookAvailable();
    }
    
    // Loading checks for capabilities.
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public boolean isCameraAvailable() {
		PackageManager packageManager = mContext.getPackageManager();
		 
		// if device support camera?
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			//yes
			return true;
		}else{
			//no
			return false;
		}
	}
	
	public boolean isTwitterAvailable() {
		return true;
	}
	
	public boolean isFacebookAvailable() {
		return true;
	}

	public void captureImage() {
	    // Capture image from camera
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //startActivityForResult(intent, CAMERA_PIC_REQUEST);
	}
	
}
