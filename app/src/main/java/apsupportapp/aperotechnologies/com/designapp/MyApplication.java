package apsupportapp.aperotechnologies.com.designapp;

import android.app.Application;
import android.util.Log;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        Log.e("TaG", "onCreate: Main Application" );
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MyLifecycleHandler
        // with just a few lines of code. Now *that's* nice.
        //registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        registerActivityLifecycleCallbacks(new BaseLifeCycleCallbacks());




    }


}
