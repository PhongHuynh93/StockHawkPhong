package dhbk.android.stockhawk;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by huynhducthanhphong on 8/26/16.
 */
public class StockHawkApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

//        1 declare library
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }


    //    2 get this application

    /**
     *
     * @param context a class call this method must have a context
     * @return
     */
    public static StockHawkApplication get(Context context) {
        return (StockHawkApplication) context.getApplicationContext();
    }
}
