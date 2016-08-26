package dhbk.android.stockhawk.ui.main;

import java.util.List;

import javax.inject.Inject;

import dhbk.android.stockhawk.data.DataManager;
import dhbk.android.stockhawk.data.model.Quote;
import dhbk.android.stockhawk.data.model.multiple.Stocks;
import dhbk.android.stockhawk.data.model.single.Stock;
import dhbk.android.stockhawk.injection.ConfigPersistent;
import dhbk.android.stockhawk.ui.base.BasePresenter;
import dhbk.android.stockhawk.util.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by huynhducthanhphong on 8/26/16.
 */
@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {
    private final DataManager mDataManager;
    private CompositeSubscription mSubscriptions;

    private Boolean stockExist = false;

    /**
     * make reposition (local + remote repo)
     * subcription that contains all subcription
     *
     * @param dataManager local + remote repo
     */
    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.unsubscribe();
    }

    /**
     *
     */
    public void loadStocks() {
        // fixme - presenter will check view before doing anything on view
        checkViewAttached();

        // get stock form db in new thread and show stock in view
        mSubscriptions.add(mDataManager.getStocks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Stocks>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the stocks.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(Stocks stocks) {
                        showStocks(stocks);
                    }
                }));
    }

    public void deleteStock(String symbol) {
        checkViewAttached();
        mSubscriptions.add(mDataManager.deleteStock(symbol)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Stocks>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error deleting the stock");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(Stocks stock) {
                        showStocks(stock);
                    }
                })
        );
    }

    public void loadStock(String symbol) {
        checkViewAttached();
        mSubscriptions.add(mDataManager.syncStock(Utils.getSingleStockQuery(symbol))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Stock>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to load single stock");
                    }

                    @Override
                    public void onNext(Stock stock) {
                        showStock(stock.getQuery().getResult().getQuote());
                    }
                }));

    }

    public void showStocks(Stocks stocks) {
        if (stocks == null) {
            getMvpView().showStocksEmpty();
        } else {
            getMvpView().showStocks(stocks.getQuery().getResult().getQuote());
        }
    }

    public void showStock(Quote quote) {
        if (quote.getAsk() == null) {
            getMvpView().showStockDoesNotExist();
        } else {
            getMvpView().showStock(quote);
        }
    }

    public Boolean checkStocksExistOrNot(final String symbol, List<Quote> quoteList) {
        stockExist = false;
        Observable.from(quoteList)
                .filter(quote -> (quote.getMsymbol().equals(symbol)))
                .subscribe(quote -> {
                    stockExist = true;
                });
        return stockExist;
    }

}
