package com.dealeronlinemarketing.lando;

import winterwell.jtwitter.OAuthSignpostClient;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Menu;
import android.net.ConnectivityManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
	WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.getSettings().setJavaScriptEnabled(true);  
	    mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
	    mWebView.getSettings().setDomStorageEnabled(true);
	    mWebView.loadUrl("file:///android_asset/index.html");
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event) {  
	   if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {  
	     mWebView.goBack();  
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
	
}