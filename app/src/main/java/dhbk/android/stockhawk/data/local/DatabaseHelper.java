package dhbk.android.stockhawk.data.local;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dhbk.android.stockhawk.data.model.Quote;
import dhbk.android.stockhawk.data.model.Quote_Table;
import dhbk.android.stockhawk.data.model.multiple.Stocks;
import dhbk.android.stockhawk.data.model.single.Stock;
import rx.Observable;

/**
 * make table using DBFlow
 */
@Singleton
public class DatabaseHelper {

    /**
     * constructor injection
     */
    @Inject
    public DatabaseHelper() {
    }

    /**
     * save stock to db
     *
     * @param stock
     * @return
     */
    public Observable<Stocks> setStocks(final Stocks stock) {
        /**
         * do not create the Observable until the observer subscribes, and create a fresh Observable for each observer
         * @see <a href="http://reactivex.io/documentation/operators/defer.html"></a>
         *
         * do not save quote immediately, but wait when the observer attach to this
         */
        return Observable.defer(() -> {
            List<Quote> quote = stock.getQuery().getResult().getQuote();

            // fixme need only onNext method, save a list of stock to db
            // to save table, simply save()
            Observable.from(quote)
                    .subscribe(BaseModel::save);

            return Observable.just(stock);
        });
    }

    /**
     *
     * fixme ssave one stock to db
     * @param stock
     * @return
     */
    public Observable<Stock> setStock(final Stock stock) {
        return Observable.defer(() -> {
            Quote quote = stock.getQuery().getResult().getQuote();
            quote.save();
            return Observable.just(stock);
        });
    }

    /**
     *
     * get stock from db
     * @return
     */
    public Observable<Stocks> getStocks() {
        return Observable.defer(() -> {
            /**
             * fixme - read all contains in queryList
             */
            List<Quote> quotes = SQLite.select()
                    .from(Quote.class)
                    .queryList();

            Stocks stock = new Stocks();
            stock.getQuery().getResult().setQuote(quotes);

            return Observable.just(stock);
        });
    }

    /**
     * @param symbol
     * @return
     */
    public Observable<Stocks> deleteStock(final String symbol) {
        return Observable.defer(() -> {

            // FIXME: 8/26/16 delete the table quote
            Delete.table(Quote.class,
                    Quote_Table.msymbol.eq(symbol));

            return getStocks();
        });
    }

}
