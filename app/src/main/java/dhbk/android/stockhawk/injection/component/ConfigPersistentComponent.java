package dhbk.android.stockhawk.injection.component;

import dagger.Component;
import dhbk.android.stockhawk.injection.ConfigPersistent;

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check {@link BaseActivity} to see how this components
 * survives configuration changes.
 * Use the {@link ConfigPersistent} scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */

/**
 * todo 11 - declare  dependence component
 *
 * anno này chỉ là component này tồn tại trong activity, destroy thì mất nhưng ko mất khi condif change
 *
 * this is a dependence component
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

//    1 - declare subcomponent
    ActivityComponent activityComponent(ActivityModule activityModule);

}