package com.dealeronlinemarketing.lando;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceView;
import android.net.ConnectivityManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.model.*;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends Activity {
	WebView mWebView;
	SurfaceView mSurfaceCamera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the main view. 
		setContentView(R.layout.activity_main);
		// Get instance pointer for WebView,
		//  and load settings.
	    mWebView = (WebView) findViewById(R.id.web_view);
	    mWebView.getSettings().setJavaScriptEnabled(true);  
	    mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
	    mWebView.getSettings().setDomStorageEnabled(true);
	    // Load jquery-mobile web page into webview.
	    mWebView.loadUrl("file:///android_asset/index.html");
	    
	    // SurfaceCamera will be called later by fragment.
	    
	    MessageBox("loaded!");
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
	   if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
		   // We would normally used mWebView.goBack() here, but since
		   //  the page navigation is based on the webview page itself,
		   //  that won't work.
		   // Call the javascript function goBack on the WebView instead.
		   mWebView.loadUrl("javascript:goBack()");
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
	
}