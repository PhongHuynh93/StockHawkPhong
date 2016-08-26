package dhbk.android.stockhawk.data.remote;

/**
 * Created by huynhducthanhphong on 8/26/16.
 */

import dhbk.android.stockhawk.data.ApiEndPoint;
import dhbk.android.stockhawk.data.model.multiple.Stocks;
import dhbk.android.stockhawk.data.model.single.Stock;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * declare endpoint for retrofit to connect to server
 */
public interface StocksService {
    //
    String ENDPOINT = "https://query.yahooapis.com/v1/public/";


    // 1 - declare endpoint
    @GET(ApiEndPoint.YAHOO_QUERY_LANGUAGE + ApiEndPoint.RESPONSE_FORMAT)
    Observable<Stocks> getStocks(@Query(value="q", encoded=true) String q);

    @GET(ApiEndPoint.YAHOO_QUERY_LANGUAGE + ApiEndPoint.RESPONSE_FORMAT)
    Observable<Stock> getStock(@Query(value="q", encoded=true) String q);

}
