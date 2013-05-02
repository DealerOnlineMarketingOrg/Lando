package com.dealeronlinemarketing.lando;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.dealeronlinemarketing.lando.FBSessionEvents.AuthListener;
import com.dealeronlinemarketing.lando.FBSessionEvents.LogoutListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FBConnector {

	private Facebook facebook = null;
	private Context context;
	private String[] permissions;
	private Handler mHandler;
	private Activity activity;
	private SessionListener mSessionListener = new SessionListener();;
	
	public FBConnector(String appId,Activity activity,Context context,String[] permissions) {
		this.facebook = new Facebook(appId);
		
		FBSessionStore.restore(facebook, context);
        FBSessionEvents.addAuthListener(mSessionListener);
        FBSessionEvents.addLogoutListener(mSessionListener);
        
		this.context=context;
		this.permissions=permissions;
		this.mHandler = new Handler();
		this.activity=activity;
	}
	
	public void login() {
        if (!facebook.isSessionValid()) {
            facebook.authorize(this.activity, this.permissions,new LoginDialogListener());
        }
    }
	
	public void logout() {
        FBSessionEvents.onLogoutBegin();
        AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(this.facebook);
        asyncRunner.logout(this.context, new LogoutRequestListener());
	}
	
	public void postMessageOnWall(String msg) {
		if (facebook.isSessionValid()) {
		    Bundle parameters = new Bundle();
		    parameters.putString("message", msg);
		    try {
				String response = facebook.request("me/feed", parameters,"POST");
				System.out.println(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			login();
		}
	}	

    private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            FBSessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) {
            FBSessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
            FBSessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            FBSessionEvents.onLoginError("Action Canceled");
        }
    }
    
    public class LogoutRequestListener extends FBBaseRequestListener {
        public void onComplete(String response, final Object state) {
            // callback should be run in the original thread, 
            // not the background thread
            mHandler.post(new Runnable() {
                public void run() {
                    FBSessionEvents.onLogoutFinish();
                }
            });
        }
    }
    
    private class SessionListener implements AuthListener, LogoutListener {
        
        public void onAuthSucceed() {
            FBSessionStore.save(facebook, context);
        }

        public void onAuthFail(String error) {
        }
        
        public void onLogoutBegin() {           
        }
        
        public void onLogoutFinish() {
            FBSessionStore.clear(context);
        }
    }

	public Facebook getFacebook() {
		return this.facebook;
	}
}
