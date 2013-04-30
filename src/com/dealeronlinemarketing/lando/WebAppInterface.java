package com.dealeronlinemarketing.lando;

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

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    
    private class capabilities {
    	boolean hasNetwork;
    	boolean hasInternet;
    	boolean hasBackCamera;
    	boolean hasTwitterInstalled;
    	boolean hasFacebookInstalled;
    } capabilities Capabilities;
    
    private void checkCapabilities() {
    	Capabilities.hasNetwork = isNetworkAvailable();
    	Capabilities.hasInternet = isInternetAvailable();
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
	
	private boolean isInternetAvailable() {
		// Attempt to load an internet page.
		return true;
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
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA_PIC_REQUEST);
	}

}
