package com.example.landodev;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import android.graphics.Rect;

// This is the javascript -> java interface.
// Used for java-functions that are used and called in the
//  jquery-mobile site via javascript (Android.function());
public class WebAppInterface {
	private static final String TAG = "Interface";

	// The context of the caller (caller.this)
    Context mContext;
    //private static final int CAMERA_PIC_REQUEST = 1337;
    
    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }
    
    // Function to run the javascript callbacks.
    // Javascript callbacks are functions that are called
    //  after an interface function is completed. These are
    //  used for async operations.
    // funcName is the name of the callback function.
    // args is a comma-delimited list of arguments.
    //  strings in args need to be wrapped in single quotes.
    // If there are no arguments, set args to "".
    private void runCallback(String funcName, String args) {
    	// Ensure we have a callback function to call.
    	if (funcName != "")
    		((MainActivity)mContext).callJS(mContext, funcName, args);
    }
    
    // Adds the camera preview window to the view at the specified size and location.
    @JavascriptInterface
    public void openCameraPreview(int left, int top, int width, int height, String callback) {
    	// Open up the camera preview view.
    	Rect clientRect = new Rect(left, top, width+left, height+top);
    	((MainActivity)mContext).openCamera(clientRect);
    	
    	// Call javascript callback when done.
    	runCallback(callback, "");
    }
    
    // Saves the picture.
    @JavascriptInterface
    public void takeCameraPicture(String callback) {
    	((MainActivity)mContext).takePicture(callback);
    	
    	// The callback will be called from the takePicture methods in main activity.
    }
    
    // Removes the camera fragment from the view.
    @JavascriptInterface
    public void closeCameraPreview(String callback) {
    	// Close the camera window down.
    	((MainActivity)mContext).closeCamera();
    	
    	// Call javascript callback when done.
    	runCallback(callback, "");
    }
    
    // Shows an image from the specified path on the thumbnail.
    // Left, top, width and height are in pixels from the bounding
    //  element's size and position (relative to the page) to place this at.
    @JavascriptInterface
    public void showThumbnail(String filePath, int left, int top, int width, int height, String callback) {
    	// Convert size and position to Rect for addImageThumb.
    	Rect clientRect = new Rect(left, top, width+left, height+top);
    	((MainActivity)mContext).addImageThumb(mContext, filePath, clientRect);
    	
    	// Call javascript callback when done.
    	runCallback(callback, "");
    }
    
    // Removes the imageview thumbnail from the view.
    @JavascriptInterface
    public void removeThumbnail(String callback) {
    	((MainActivity)mContext).removeImageThumb(mContext);
    	
    	// Call javascript callback when done.
    	runCallback(callback, "");
    }
    
    // This function is for debugging from javascript.
    // Writes to the log using the tag and message.
    @JavascriptInterface
    public void debugLog(String logTag, String logMessage) {
    	Log.d(logTag, logMessage);
    }
    
    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }
    
    // Show a message box from javascript
    @JavascriptInterface
	public void messageBox(String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
		alert.setTitle("Facebook");
		// Set alert properties.
		alert
			.setMessage("Got to Facebook")
			.setCancelable(false)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// When user clicks Ok..
					dialog.cancel();
				}
			});
	}
	
    // Holds the list of capabilities of the device.
    // Called at the start of the webView.
    private class Capabilities {
    	public boolean hasNetwork = false;
    	public boolean hasBackCamera = false;
    	public boolean hasTwitterInstalled = false;
    	public boolean hasFacebookInstalled = false;
    } Capabilities capabilities = new Capabilities();
    
    // Checks the capabilities of the device.
    @JavascriptInterface
    public void checkCapabilities(String callback) {
    	capabilities.hasNetwork = isNetworkAvailable();
    	capabilities.hasBackCamera = isCameraAvailable();
    	capabilities.hasTwitterInstalled = isTwitterAvailable();
    	capabilities.hasFacebookInstalled = isFacebookAvailable();
    	
    	// Call javascript callback when done.
    	runCallback(callback, "");
    }
    
    // Checks if the network is available.
	private boolean isNetworkAvailable() {
		// Ask the SystemService if the active network is connected.
	    ConnectivityManager connectivityManager
	          = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    if (activeNetworkInfo == null)
	    	return false;
	    else
	    	return activeNetworkInfo.isConnected();
	}
	
	// Checks if there's a camera.
	private boolean isCameraAvailable() {
		Log.d(TAG, "starting camera check");
	    
		PackageManager packageManager = mContext.getPackageManager();
		
		// if device support camera?
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isTwitterAvailable() {
		return true;
	}
	
	private boolean isFacebookAvailable() {
		return true;
	}
	
}
