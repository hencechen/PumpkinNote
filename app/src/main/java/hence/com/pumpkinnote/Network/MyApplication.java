package hence.com.pumpkinnote.Network;

import android.app.Application;
import android.content.Context;

/**
 * Created by Hence on 2016/8/25.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;


    @Override
    public void onCreate(){
        super.onCreate();
        mInstance=this;

    }

    public static MyApplication getInstance(){
        return mInstance;
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }
}