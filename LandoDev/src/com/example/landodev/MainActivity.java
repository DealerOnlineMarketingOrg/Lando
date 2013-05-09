package com.example.landodev;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Files.FileColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	// The tag for logging.
	private static final String TAG = "Main";
	// Holds the main WebView.
    WebView mWebView;
    // Holds the current active instance of the camera for taking pictures.
    private Camera mCamera = null;
    // Holds the instance of the live-preview view for the camera.
	private CameraPreviewActivity mCameraPreview = null;
	// Holds the thumbnail image view that was taken from the camera.
	private ImageView imageViewThumb = null;
	// Used for javascript callback, when javascript calls the WebAppInterface function takePicture.
	String takePictureCallback = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the main layout.
		setContentView(R.layout.activity_main);
        
		// Ready the webView and load into the layout.
	    mWebView = (WebView) findViewById(R.id.web_view);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    // Load Web Application Interface, so the web app can
	    //  call our public java functions.
	    mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
	    mWebView.getSettings().setDomStorageEnabled(true);
	    // Load jquery-mobile web page into webview.
	    mWebView.loadUrl("file:///android_asset/index.html");
	}
	
	// Attempts to get an open instance of the camera.
	// Returns camera instance if success,
	//  else returns null.
	private Camera getCameraInstance() {
		Camera c = null;
		
		try {
			// Attempt to get an instance of the camera by opening it.
			c = Camera.open();
		} catch (Exception e) {
			// Something went wrong. The camera wouldn't open for us.
			// It doesn't exist, it isn't on, or it's currently in use.
	    	// Let our log know we couldn't get the camera.
	    	Log.d(TAG, "Error getting camera" + e.getMessage());
			return null;
		}

		if (c == null) {
			// There was no error, but we didn't get any camera, either.
			return null;
		}
		
		// We were successful.
		return c;
	}
	
	// Called when the user requests that the camera be used.
	// Returns true if we have a preview, else false.
	public boolean openCamera(Rect clientRect) {
	    // Ready the camera.
		mCamera = getCameraInstance();
		
		if (mCamera != null) {
			// Add the camera preview View to the current webView.
			addCameraView(this, clientRect);
			return true;
	    } else {
			Log.d(TAG, "Didn't get camera.");
	    	return false;
	    }
	}
	
	// Creates the camera preview view and adds it to the current activity in the preview pane.
	// This operation has to run on the UI thread.
	private void addCameraView(final Context context, final Rect clientRect) {
	    runOnUiThread(new Runnable() {
	        public void run() {
	    		// Our dimensions are from the webView, so they're in pixels.
	    		// Convert to DIPs so the layout can show it correctly.
	    		Rect dipRect = convertClientPixelsToDIPs(clientRect);
	    		
				// Get instance of our view.
				// We need to inflate the activity_camera_preview layout, then inflate the camera_preview FrameLayout,
				//  from the resources.
				LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				RelativeLayout layoutMain = (RelativeLayout) findViewById(R.id.main_layout);
				//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dipRect.width(), dipRect.height());
				params.leftMargin = dipRect.left;
				params.topMargin = dipRect.top;
		    	

			    // Create an instance of our camera preview and add it
			    //  to this activity.
				mCameraPreview = new CameraPreviewActivity(context, mCamera);
				// Make sure camera is oriented correctly.
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.set("orienation", "portrait");
				mCamera.setParameters(parameters);
				
				View layoutPreview = mInflater.inflate(R.layout.activity_camera_preview, null);
				layoutPreview.setBackgroundColor(getResources().getColor(android.R.color.white));
				Log.d(TAG, "mCameraPreview: " + mCameraPreview);
				
				Log.d(TAG, "layoutPreview: " + layoutPreview);
				FrameLayout preview = (FrameLayout) layoutPreview.findViewById(R.id.camera_preview);
				Log.d(TAG, "preview: " + preview);
				layoutMain.addView(mCameraPreview, params);
		    	
				//TextView textView=new TextView(context);
		        //textView.setText("test");
		        //textView.setBackgroundColor(getResources().getColor(android.R.color.white));
				//layoutMain.addView(textView, params);
		    	
				//View.inflate(this, R.layout.activity_camera_preview, previewMain);
			}
	    });
	}
	
	// Removes the camera preview view from the activity and deletes from the resources.
	// This operation has to run on the UI thread.
	public void removeCameraView(final Context context) {
	    runOnUiThread(new Runnable() {
	        public void run() {
	        	ViewGroup vg = (ViewGroup)(mCameraPreview.getParent());
	        	vg.removeView(mCameraPreview);
	        	mCameraPreview = null;
	        }
	    });
	}
	
	// Convert a pixel into a DIP.
	private int convertPixelToDIP(int pixel) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		// .density gives the density conversion, not the actual density.
		// Multiply by 160 to get the actual density.
		float density = metrics.density;
		int dip = (int)(pixel*density);
		return dip;
	}
	
	// Converts rectangle's pixels into DIPs.
	private Rect convertClientPixelsToDIPs(Rect rect) {
		Rect dipRect = new Rect();
		dipRect.left = convertPixelToDIP(rect.left);
		dipRect.top = convertPixelToDIP(rect.top);
		dipRect.right = convertPixelToDIP(rect.right);
		dipRect.bottom = convertPixelToDIP(rect.bottom);
		return dipRect;
	}
	
	// Opens the camera's image thumb onto the page.
	// Saves the image view in imageViewThumb for the thumbnail view.
	// This operation has to run on the UI thread.
	public void addImageThumb(final Context context, final String filePath, final Rect clientRect) {
	    runOnUiThread(new Runnable() {
	    	public void run() {
	    		// Our dimensions are from the webView, so they're in pixels.
	    		// Convert to DIPs so the layout can show it correctly.
	    		Rect dipRect = convertClientPixelsToDIPs(clientRect);
	    		// Get the bitmap from the file and set it to the thumb view.
	    		imageViewThumb = new ImageView(context); 
	    		Bitmap bmp = BitmapFactory.decodeFile(filePath);
	    		// Scale image to thumb view.
	    		imageViewThumb.setImageBitmap(Bitmap.createScaledBitmap(bmp, dipRect.width(), dipRect.height(), false));
	    		
				RelativeLayout layoutMain = (RelativeLayout) findViewById(R.id.main_layout);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dipRect.width(), dipRect.height());
				params.leftMargin = dipRect.left;
				params.topMargin = dipRect.top;
				
				layoutMain.addView(imageViewThumb, params);
	    	}
	    });
	}
	
	// Removes the imageView thumb from the activity and deletes from the resources.
	// This operation has to run on the UI thread.
	public void removeImageThumb(final Context context) {
	    runOnUiThread(new Runnable() {
	        public void run() {
	        	ViewGroup vg = (ViewGroup)(imageViewThumb.getParent());
	        	vg.removeView(imageViewThumb);
	        	imageViewThumb = null;
	        }
	    });
	}
	
	// callJS calls javascript functions from the webView.
	// This function is required because the function has to be
	//  called inside the UI thread.
	// context needs to be set to the calling activity (this in the activity).
	// jsFuncName is the name of the javascript function to call.
	// argList is a comma-delimited argument list to send to the js function.
	//  strings in argList need to be wrapped in single quotes.
	//  If there are no arguments, set argList to "".
	public void callJS(final Context context, final String jsFuncName, final String argList) {
		runOnUiThread(new Runnable() {
	        public void run() {
	    		Log.d(TAG, "callJS: " + jsFuncName+"("+argList+")");
	        	((MainActivity)context).mWebView.loadUrl("javascript:"+jsFuncName+"("+argList+")");
	        }
		});
	}
	
	// Takes a picture using the active camera object (in the camera preview).
	public void takePicture(String callback) {
		// We'll save the callback for when it's called from takePictureCompleted.
		takePictureCallback = callback;
		Log.d(TAG, "takePicture: " + callback);
		// Tell the camera to take the picture.
		mCamera.takePicture(null,  null,  mPicture);
	}
	
	// This function needs to be called by the mCamera.takePicture callback
	//  when completed. It will call the javascript callback function for
	//  takePicture.
	public void takePictureCompleted(String filePath) {
		if (takePictureCallback != "")
			// Javascript callback. Since filePath is a string, surround
			//  it in single quotes.
			callJS(this, takePictureCallback, "'"+filePath+"'");
	}
	
	// Closes the preview and the camera.
	public void closeCamera() {
		// Remove the preview view from the activity.
		// This will remove it from view, and delete the resource.
		removeCameraView(this);
		// Close camera and release it so other apps can use it.
		// (Releasing it also allows this app to use the camera again)
		mCamera.stopPreview();
		mCamera.release();
	}
	
	// The onPause and releaseCamera routines are used
	//  when the activity stops using the camera for some reason. We need
	//  to release the camera object so other applications can use it.
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    // Releases and removes the camera.
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;			  // Clear resource mCamera.
        }
    }

	// Used for navigation
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	   if (keyCode == KeyEvent.KEYCODE_BACK) {
		   // We would normally used mWebView.goBack() here, but since
		   //  the page navigation is based on the webview page itself,
		   //  that won't work.
		   // Call the javascript function goBack on the WebView instead.
		   callJS(this, "goBack", "");
		   // Stop the default behavior of the back button.
		   return true;
	   }
	   return super.onKeyDown(keyCode, event);  
	 }
	
    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
          return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
        	Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == FileColumns.MEDIA_TYPE_IMAGE){
        	// This is a picture.
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == FileColumns.MEDIA_TYPE_VIDEO) {
        	// This is a video.
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    
    // This is the callback used by the camera when a picture is taken.
    // It attempts to save the picture in a file for future use.
    private PictureCallback mPicture = new PictureCallback() {
    	
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        	String filePath = "";
        	// Get an available file for saving the image.
            File pictureFile = getOutputMediaFile(FileColumns.MEDIA_TYPE_IMAGE);
            Log.d(TAG, "pictureFile: " + pictureFile);
            if (pictureFile == null) {
            	// We didn't get a file.
                Log.d(TAG, "Error creating media file, check storage permissions");
                filePath = "";
            } else {
	            try {
	            	// We got an available file. Save the image and close the file stream.
	                FileOutputStream fos = new FileOutputStream(pictureFile);
	                fos.write(data);
	                fos.close();
	                // Get the path, so we can send it to the takePicture javascript callback.
	                // The callback will use it to open the image in the picture thumbnail pane.
	                filePath = pictureFile.getPath();
	                Log.d(TAG, "filePath: " + filePath);
	            } catch (FileNotFoundException e) {
	            	// File isn't found.
	                Log.d(TAG, "File not found: " + e.getMessage());
	                filePath = "";
	            } catch (IOException e) {
	            	// We can't access the files.
	                Log.d(TAG, "Error accessing file: " + e.getMessage());
	                filePath = "";	                
	            }
            }
            
            // Call this function when we're done taking the picture, so that
            //  javascript function callback can be called.
            // Argument passed needs to be the filepath of the saved image.
            takePictureCompleted(filePath);
        }
    };
}