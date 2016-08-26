package dhbk.android.stockhawk.util;

import java.util.Locale;

import dhbk.android.stockhawk.data.remote.UrlBuilder;


/**
 * Created by Rajan Maurya on 25/08/16.
 */
public class Utils {

    /**
     * get uri for 4 string stock
     * @return
     */
    public static String getYahooStocksQuery() {
        return UrlBuilder.queryBuilder(
                Constants.YAHOO_STOCK_SYMBOL,
                Constants.APPLE_STOCK_SYMBOL,
                Constants.GOOGLE_STOCK_SYMBOL,
                Constants.MICROSOFT_STOCK_SYMBOL);
    }

    /**
     *
     * @param query
     * @return
     */
    public static String getSingleStockQuery(String query) {
        return UrlBuilder.queryBuilder(String.format(Locale.ENGLISH, "\"%1$s\"", query));
    }
}
