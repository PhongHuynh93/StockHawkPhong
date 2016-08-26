package dhbk.android.stockhawk.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import dhbk.android.stockhawk.data.local.DatabaseHelper;
import dhbk.android.stockhawk.data.local.PreferencesHelper;
import dhbk.android.stockhawk.data.model.multiple.Stocks;
import dhbk.android.stockhawk.data.model.single.Stock;
import dhbk.android.stockhawk.data.remote.StocksService;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final StocksService mStocksService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(StocksService stocksService,
                       PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mStocksService = stocksService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Stocks> syncStocks(String query) {
        return mStocksService.getStocks(query)
                .concatMap(new Func1<Stocks, Observable<? extends Stocks>>() {
                    @Override
                    public Observable<? extends Stocks> call(Stocks stock) {
                        return mDatabaseHelper.setStocks(stock);
                    }
                });
    }

    public Observable<Stock> syncStock(String query) {
        return mStocksService.getStock(query)
                .concatMap(new Func1<Stock, Observable<? extends Stock>>() {
                    @Override
                    public Observable<? extends Stock> call(Stock stock) {
                        return mDatabaseHelper.setStock(stock);
                    }
                });
    }

    public Observable<Stocks> getStocks() {
        return mDatabaseHelper.getStocks();
    }

    public Observable<Stocks> deleteStock(String symbol) {
        return mDatabaseHelper.deleteStock(symbol);
    }
}
