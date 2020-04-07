package com.example.ray.carbontracker_flame;

import android.app.Application;

/**
 * App allows the singleton class to access the Application Context
 */
//based of of: http://stackoverflow.com/questions/4391720/how-can-i-get-a-resource-content-from-a-static-context/4391811#4391811
public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
