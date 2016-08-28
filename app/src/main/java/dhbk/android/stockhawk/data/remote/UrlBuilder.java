package dhbk.android.stockhawk.data.remote;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rajan Maurya on 15/08/16.
 */
public class UrlBuilder {

    public static StringBuilder urlBuilder = new StringBuilder();

    /**
     * This Method Add the Select Yahoo query to Request URl
     *
     * fixme - note to encode the string before query the server
     */
    public static void addYahooSelectQuotesQuery() {
        /**
         * fixme - encode string by replace space with other char
         * @see <a href="http://stackoverflow.com/questions/3286067/url-encoding-in-android"></a>
         */
        try {
            urlBuilder.append(URLEncoder.encode("select * from yahoo.finance.quotes where symbol " + "in (",
                    "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * This Method Add the Symbol to the Query. This Method Takes the Array of Symbol and
     * encode with URLEncoder.encode and adding to the Query.
     *
     * @param symbol Symbol of the Stocks
     */
    public static void addStockSymbol(String... symbol) {
        List<String> stocksSymbols = Arrays.asList(symbol);
        if (stocksSymbols.size() != 0) {
            for (int i = 0; i < stocksSymbols.size(); ++i) {
                if (i - (stocksSymbols.size() - 1) == 0) {
                    try {
                        urlBuilder.append(URLEncoder.encode(stocksSymbols.get(i) + ")", "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        urlBuilder.append(URLEncoder.encode(stocksSymbols.get(i) + ",", "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    /**
     * This Method Building the Symbol Url Query
     *
     * @param symbols Stocks Symbol
     * @return String Query
     */
    public static String queryBuilder(String... symbols) {
        urlBuilder.setLength(0);
        addYahooSelectQuotesQuery();
        addStockSymbol(symbols);
        return urlBuilder.toString();
    }
}
