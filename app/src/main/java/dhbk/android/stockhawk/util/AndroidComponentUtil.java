package dhbk.android.stockhawk.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by huynhducthanhphong on 8/26/16.
 */
public class AndroidComponentUtil {
    public static void toggleComponent(Context context, Class componentClass, boolean enable) {
        ComponentName componentName = new ComponentName(context, componentClass);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(
                componentName,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        );
    }

    /**
     * fixme - check servce in para whether running
     * return true if service in para is running in Android
     * @param context
     * @param serviceClass
     * @return
     */
    public static  boolean isServiceRunning(Context context, Class serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
