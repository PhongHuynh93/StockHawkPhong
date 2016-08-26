package dhbk.android.stockhawk;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import dhbk.android.stockhawk.injection.component.ApplicationComponent;
import dhbk.android.stockhawk.injection.module.ApplicationModule;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by huynhducthanhphong on 8/26/16.
 * <p/>
 * declare appliation
 */
public class StockHawkApplication extends Application {
    ApplicationComponent mApplicationComponent;

    /**
     * initialize the library use in application
     */
    @Override
    public void onCreate() {
        super.onCreate();

//        1 declare library
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Fabric.with(this, new Crashlytics());
        }

//        instantiate DBFlow in our main application.
        FlowManager.init(new FlowConfig.Builder(this).build());
        Stetho.initializeWithDefaults(this);
    }

    /**
     * 2 get this application
     *
     * @param context a class call this method must have a context
     * @return
     */
    public static StockHawkApplication get(Context context) {
        return (StockHawkApplication) context.getApplicationContext();
    }

    /**
     * get application component
     *
     * @return
     */
    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
