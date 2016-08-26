package dhbk.android.stockhawk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import javax.inject.Inject;

import dhbk.android.stockhawk.data.DataManager;
import dhbk.android.stockhawk.data.model.multiple.Stocks;
import dhbk.android.stockhawk.util.AndroidComponentUtil;
import dhbk.android.stockhawk.util.NetworkUtil;
import dhbk.android.stockhawk.util.Utils;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * create started service
 */
public class SyncService extends Service {
    @Inject
    DataManager mDataManager;

    private Subscription mSubscription;

    public SyncService() {
    }

    /**
     * create intent to start this service
     *
     * @param context
     * @return
     */
    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    /**
     * because it is a started service (dont need to communicate with view), so dont need to bind to this
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * // : 8/26/16
     * <p>
     * create application component and inject this service
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // create component and inject to this service
        StockHawkApplication
                .get(this)
                .getComponent()
                .inject(this);
    }

    /**
     * // : 8/26/16
     * turn on the broadcast receiver to listen for state change
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.i("Starting sync...");

        // turn on broadcast to listen for network active if the network is not before
        // if the network is not turned on, not run the service
        if (!NetworkUtil.isNetworkConnected(this)) {
            Timber.i("Sync canceled, connection not available");
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        // if the network is on, remove the previous subscription (so it's not update with the old data)
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();

        // fixme download the new data and save to db, rememeber to unsubscribe it when it's not done yet
        mSubscription = mDataManager.syncStocks(Utils.getYahooStocksQuery())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Stocks>() {
                    @Override
                    public void onCompleted() {
                        // FIXME: 8/26/16 when service done it's job, stop itself
                        Timber.i("Synced successfully!");
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // FIXME: 8/26/16 when error, stop itself
                        Timber.w(e, "Error syncing.");
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Stocks stocks) {
                    }
                });

        return START_STICKY;
    }

    /**
     * // : 8/26/16 unsubscribe before destroy
     */
    @Override
    public void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    /**
     * 5 - declare broadcastreceiver, when it's receive network active, it's will not listen anymore
     */
    public static class SyncOnConnectionAvailable extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) && NetworkUtil.isNetworkConnected(context)) {
                // when connect to a network, start to sync
                Timber.i("Connection is now available, triggering sync...");
                // fixme - if state of network change, stop broadcast receiver
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                // start this service
                context.startService(getStartIntent(context));
            }
        }
    }
}
