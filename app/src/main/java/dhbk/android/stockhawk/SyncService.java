package dhbk.android.stockhawk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import dhbk.android.stockhawk.util.AndroidComponentUtil;
import dhbk.android.stockhawk.util.NetworkUtil;
import timber.log.Timber;

/**
 * create started service
 */
public class SyncService extends Service {
    public SyncService() {
    }

    /**
     * create intent to start this service
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
     * // TODO: 8/26/16
     */
    @Override
    public void onCreate() {
        super.onCreate();
        StockHawkApplication.get(this).getComponent().inject(this);
    }

    /**
     * // TODO: 8/26/16
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * // TODO: 8/26/16
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *  5 - declare broadcastreceiver, when
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
