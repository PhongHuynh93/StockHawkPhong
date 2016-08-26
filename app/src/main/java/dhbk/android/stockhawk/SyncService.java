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

public class SyncService extends Service {
    public SyncService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *  5 - declare broadcastreceiver
     */
    public static class SyncOnConnectionAvailable extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Timber.i("Connection is now available, triggering sync...");
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }
}
