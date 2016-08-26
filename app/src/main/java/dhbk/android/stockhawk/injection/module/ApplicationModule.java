package dhbk.android.stockhawk.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dhbk.android.stockhawk.data.remote.StocksService;
import dhbk.android.stockhawk.injection.ApplicationContext;

/**
 * Provide application-level dependencies.
 *  7 - declare module
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    // 1 - provide app
    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    // provice api endpoint
    @Provides
    @Singleton
    StocksService provideRibotsService() {
        return StocksService.Creator.newStocksService();
    }

}
