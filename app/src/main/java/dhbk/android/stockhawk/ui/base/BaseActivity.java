package dhbk.android.stockhawk.ui.base;

import android.os.Bundle;

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
 *
 *
 * base for activity reuse
 */
public class BaseActivity {
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

//        2 - get the share preference
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(StockHawkApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }

//        3 - get activity component
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }
}
