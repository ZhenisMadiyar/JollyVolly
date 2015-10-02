package com.example.jv.jollyvolly;

/**
 * Created by Admin on 24.06.2015.
 */

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Add your initialization code here
        Parse.initialize(this, "g26iBOyGwlQKiEGlHkn3HFcpfKJfINRn2X2LmBAt", "MJmOR2owhnSeIguSqC2kpd4XL7CMEl3PrvAZFnhf");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

//        ParseACL.setDefaultACL(defaultACL, true);
    }
}
