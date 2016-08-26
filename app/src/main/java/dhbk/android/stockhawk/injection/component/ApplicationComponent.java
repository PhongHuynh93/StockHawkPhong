package dhbk.android.stockhawk.injection.component;

/**
 * Created by huynhducthanhphong on 8/26/16.
 */

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dhbk.android.stockhawk.SyncService;
import dhbk.android.stockhawk.data.DataManager;
import dhbk.android.stockhawk.data.local.DatabaseHelper;
import dhbk.android.stockhawk.data.local.PreferencesHelper;
import dhbk.android.stockhawk.data.remote.StocksService;
import dhbk.android.stockhawk.injection.ApplicationContext;
import dhbk.android.stockhawk.injection.module.ApplicationModule;
import dhbk.android.stockhawk.util.RxEventBus;

/**
 *  6 - declare component
 *  provide singleton objects to sub component
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