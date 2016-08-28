package dhbk.android.stockhawk.data.remote;

/**
 * Created by huynhducthanhphong on 8/26/16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dhbk.android.stockhawk.BuildConfig;
import dhbk.android.stockhawk.data.ApiEndPoint;
import dhbk.android.stockhawk.data.model.multiple.Stocks;
import dhbk.android.stockhawk.data.model.single.Stock;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * declare endpoint for retrofit to connect to server
 */
public interface StocksService {
    //
    String ENDPOINT = "https://query.yahooapis.com/v1/public/";


    // 1 - declare endpoint, fixme note that you can see the encoded = true,
    @GET(ApiEndPoint.YAHOO_QUERY_LANGUAGE + ApiEndPoint.RESPONSE_FORMAT)
    Observable<Stocks> getStocks(@Query(value="q", encoded=true) String q);

    @GET(ApiEndPoint.YAHOO_QUERY_LANGUAGE + ApiEndPoint.RESPONSE_FORMAT)
    Observable<Stock> getStock(@Query(value="q", encoded=true) String q);

    // 2 - create http client
    /******** Helper class that sets up a new services *******/
    class Creator {
        /**
         * fixme - create retrofit endpoint instance and retrofit client with log
         * @return the endpoint which is used to get the info from server
         *
         *
         * fixme - create the okhttp client and add it to retrofit instance
         */
        public static StocksService newStocksService() {
            // add library to log http response
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE);

            // build http client
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // gson format for date
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

//            build retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(StocksService.ENDPOINT)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            // return retrofit endpoint
            return retrofit.create(StocksService.class);
        }
    }
}
