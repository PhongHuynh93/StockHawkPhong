package dhbk.android.stockhawk.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import dhbk.android.stockhawk.StockHawkApplication;
import dhbk.android.stockhawk.injection.component.ActivityComponent;
import dhbk.android.stockhawk.injection.component.ConfigPersistentComponent;
import dhbk.android.stockhawk.injection.module.ActivityModule;
import timber.log.Timber;

/**
 * Created by huynhducthanhphong on 8/26/16.
 * <p>
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 * <p>
 * <p>
 * base for activity reuse
 */
public class BaseActivity extends AppCompatActivity {
    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.

//        1 - get the privious state
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

//        2 -
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            // start activity component and save it to hashmap if config change
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent
                    .builder()
                    .applicationComponent(StockHawkApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            // get the activity component if config change
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }

//        3 - get other activity component by getting it each time the config change
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }


    //    4 put the key after config change, so you can reuse the component
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    // 5 - remove the hashmap when destroy this activity, remove this component
    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    /**
     * get the ActivityComponent
     */
    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

}
