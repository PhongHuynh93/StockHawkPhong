package dhbk.android.stockhawk.injection.component;

/**
 * Created by huynhducthanhphong on 8/26/16.
 */

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dhbk.android.stockhawk.SyncService;
import dhbk.android.stockhawk.injection.ApplicationContext;

/**
 * todo 6 - declare component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    // 1 - inject to this service
    void inject(SyncService syncService);

    //    2 - declare obj that dependance component use
    @ApplicationContext
    Context context();
    Application application();
    StocksService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}