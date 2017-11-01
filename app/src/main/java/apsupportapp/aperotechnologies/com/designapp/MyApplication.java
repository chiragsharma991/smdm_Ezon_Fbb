package apsupportapp.aperotechnologies.com.designapp;

import android.app.Application;
import android.util.Log;




public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MyLifecycleHandler
        // with just a few lines of code. Now *that's* nice.
        //registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        registerActivityLifecycleCallbacks(new BaseLifeCycleCallbacks());

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);


    }


}
