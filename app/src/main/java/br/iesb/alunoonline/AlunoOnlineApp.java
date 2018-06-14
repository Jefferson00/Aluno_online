package br.iesb.alunoonline;

import android.app.Application;
import android.content.Intent;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by jefferson on 17/03/2018.
 */

public class AlunoOnlineApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


    }
}
