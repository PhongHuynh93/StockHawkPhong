package dhbk.android.stockhawk.injection.component;

import dagger.Subcomponent;
import dhbk.android.stockhawk.injection.module.ActivityModule;
import dhbk.android.stockhawk.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 *
 *
 * this is a subcomponent
 */
// TODO: 8/26/16 12 declare activity component to inject to mainactivity
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
}
