package dhbk.android.stockhawk.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by huynhducthanhphong on 8/26/16.
 */
public class AndroidComponentUtil {

    /**
     * fixme tự ta phải bật hay tắt 1 component (vd như ta tự bật broadcast receiver, thích hợp khi ta phải đợi 1 cái gì đó xảy ra ta mới bật đươc chứ ko phải đợi onResume rồi bật...)
     * @param context
     * @param componentClass class muốn cho nó chạy hay dừng
     * @param enable cho phép component chạy hay dừng nó
     */
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
