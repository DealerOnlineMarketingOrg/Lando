package com.dealeronlinemarketing.lando;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CameraWebActivity extends Activity implements SurfaceHolder.Callback {
    WebView mWebView;
    WebPunchThru webPunchThru;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://www.google.com");
        webPunchThru = (WebPunchThru) findViewById(R.id.web_punch);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
    }
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