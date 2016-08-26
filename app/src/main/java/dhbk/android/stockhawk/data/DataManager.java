package dhbk.android.stockhawk.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import dhbk.android.stockhawk.data.local.DatabaseHelper;
import dhbk.android.stockhawk.data.local.PreferencesHelper;
import dhbk.android.stockhawk.data.model.multiple.Stocks;
import dhbk.android.stockhawk.data.model.single.Stock;
import dhbk.android.stockhawk.data.remote.StocksService;
import rx.Observable;

@Singleton
public class DataManager {

    private final StocksService mStocksService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    /**
     * constructor injection: add DataManager to dagger
     * @param stocksService retrofit endpoints
     * @param preferencesHelper pref
     * @param databaseHelper get/ set the stock from db
     */
    @Inject
    public DataManager(StocksService stocksService,
                       PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mStocksService = stocksService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    /**
     * get pref
     * @return pref
     */
    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    /**
     * fixme get stocks gain by getting stock from network and save it to db again
     * @param query
     * @return
     */
    public Observable<Stocks> syncStocks(String query) {
        return mStocksService.getStocks(query)
                .concatMap(mDatabaseHelper::setStocks);
    }

    /**
     * get a stock from network
     * @param query
     * @return
     */
    public Observable<Stock> syncStock(String query) {
        return mStocksService.getStock(query)
                .concatMap(stock -> mDatabaseHelper.setStock(stock));
    }

    /**
     * get stocks
     * @return
     */
    public Observable<Stocks> getStocks() {
        return mDatabaseHelper.getStocks();
    }

    /**
     * delete stock in db and get stock
     * @param symbol
     * @return
     */
    public Observable<Stocks> deleteStock(String symbol) {
        return mDatabaseHelper.deleteStock(symbol);
    }
}
