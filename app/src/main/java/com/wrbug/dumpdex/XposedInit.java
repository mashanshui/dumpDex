package com.wrbug.dumpdex;

import android.os.Build;
import android.util.Log;

import com.wrbug.dumpdex.dump.LowSdkDump;
import com.wrbug.dumpdex.dump.OreoDump;
import com.wrbug.dumpdex.util.DeviceUtils;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * XposedInit
 *
 * @author wrbug
 * @since 2018/3/20
 */
public class XposedInit implements IXposedHookLoadPackage {
    private static final String TAG = "XposedInit";

    public static void log(String txt) {
        Log.e(TAG, txt);
    }

    public static void log(Throwable t) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        XposedBridge.log(t);
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        PackerInfo.Type type = PackerInfo.find(lpparam);
//        if (type == null) {
//            return;
//        }
        final String packageName = lpparam.packageName;
        Log.e(TAG, "handleLoadPackage: " + packageName);
        if ("com.broccoli.bh".equals(packageName)) {
            String path = "/data/data/" + packageName + "/dump";
            File parent = new File(path);
            if (!parent.exists() || !parent.isDirectory()) {
                parent.mkdirs();
            }
            log("sdk version:" + Build.VERSION.SDK_INT);
            if (DeviceUtils.isOreo() || DeviceUtils.isPie() || DeviceUtils.isAndroid10()) {
                OreoDump.init(lpparam);
            } else {
                LowSdkDump.init(lpparam,type);
            }

        }
    }
}
