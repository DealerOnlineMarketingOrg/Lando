package com.dealeronlinemarketing.lando;

import java.io.IOException;

import android.hardware.Camera;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.net.ConnectivityManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.model.*;
import android.widget.TextView;
import android.content.Intent;
import android.hardware.Camera;

public class MainActivity extends Activity  implements SurfaceHolder.Callback {
	WebView webView;
    WebPunchThru webPunchThru;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the main view.
		setContentView(R.layout.activity_main);
		
		// Ready the surface view for later use.
		surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
		// Ready the webView and load.
	    webView = (WebView) findViewById(R.id.web_view);
	    webView.getSettings().setJavaScriptEnabled(true);  
	    webView.addJavascriptInterface(new WebAppInterface(this), "Android");
	    webView.getSettings().setDomStorageEnabled(true);
	    // Load jquery-mobile web page into webview.
	    webView.loadUrl("file:///android_asset/index.html");
	    
	    // SurfaceCamera will be called later from the webView.
	}
	
	public void CreatePunchView() {
        webPunchThru = (WebPunchThru) findViewById(R.id.web_punch);
	}
	
	public void DestroyPunchView() {
		// Destroy the punch view here.
	}
	
	public void MessageBox(String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Facebook");
		alert
			.setMessage("Got to Facebook")
			.setCancelable(false)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	   if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
		   // We would normally used mWebView.goBack() here, but since
		   //  the page navigation is based on the webview page itself,
		   //  that won't work.
		   // Call the javascript function goBack on the WebView instead.
		   webView.loadUrl("javascript:goBack()");
		   return true;
	   }
	   return super.onKeyDown(keyCode, event);  
	 }
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// Code for Facebook's session class.
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	// The following functions are part of the SurfaceHolder callback
    @Override
    public void onResume(){
        camera = Camera.open();
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
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
         
    }
 
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        
    }
  
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        
    }
    
}