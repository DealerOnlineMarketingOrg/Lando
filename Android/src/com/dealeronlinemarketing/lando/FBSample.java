package com.dealeronlinemarketing.lando;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import com.facebook.android.Facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FBSample extends Activity {

	private static final String FACEBOOK_APPID = "PUT YOUR FACEBOOK APP ID HERE";
	private static final String FACEBOOK_PERMISSION = "publish_stream";
	private static final String TAG = "FacebookSample";
	private static final String MSG = "Message from FacebookSample";
	
	private final Handler mFacebookHandler = new Handler();
	private TextView loginStatus;
	private FBConnector facebookConnector;
	
    final Runnable mUpdateFacebookNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Facebook updated !", Toast.LENGTH_LONG).show();
        }
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_test);
        this.facebookConnector = new FBConnector(FACEBOOK_APPID, this, getApplicationContext(), new String[] {FACEBOOK_PERMISSION});
        
        
        loginStatus = (TextView)findViewById(R.id.login_status);
        Button post = (Button) findViewById(R.id.btn_post);
        Button clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);
        
        
        post.setOnClickListener(new View.OnClickListener() {
        	/**
        	 * Send a post. If the user hasn't authenticated to Facebook yet, he'll be redirected via a browser
        	 * to the Facebook login page. Once the user authenticated, he'll authorize the Android application to send
        	 * posts on the users behalf.
        	 */
            public void onClick(View v) {
        		postMessage();
            }
        });

        clearCredentials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	clearCredentials();
            	updateLoginStatus();
            }
        });
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.facebookConnector.getFacebook().authorizeCallback(requestCode, resultCode, data);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
	}
	
	public void updateLoginStatus() {
		loginStatus.setText("Logged into Twitter : " + facebookConnector.getFacebook().isSessionValid());
	}
	

	private String getFacebookMsg() {
		return MSG + " at " + new Date().toLocaleString();
	}	
	
	public void postMessage() {
		
		if (facebookConnector.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			FBSessionEvents.AuthListener listener = new FBSessionEvents.AuthListener() {
				
				@Override
				public void onAuthSucceed() {
					postMessageInThread();
				}
				
				@Override
				public void onAuthFail(String error) {
					
				}
			};
			FBSessionEvents.addAuthListener(listener);
			facebookConnector.login();
		}
	}

	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {
		    	
		    	try {
		    		facebookConnector.postMessageOnWall(getFacebookMsg());
					mFacebookHandler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e(TAG, "Error sending msg",ex);
				}
		    }
		};
		t.start();
	}

	private void clearCredentials() {
		try {
			facebookConnector.getFacebook().logout(getApplicationContext());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}