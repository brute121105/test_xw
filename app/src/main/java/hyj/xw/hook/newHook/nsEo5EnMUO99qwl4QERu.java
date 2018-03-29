package hyj.xw.hook.newHook;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class nsEo5EnMUO99qwl4QERu
{
    public static void O000000o(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, final PhoneInfo paramPhoneInfo)
    {

        new GoicqOVehRY68RCP7lXb(paramLoadPackageParam, paramPhoneInfo);//ro.product.model
        new iJzwYzrQSz7shvm8fphs(paramLoadPackageParam, paramPhoneInfo);//android.os.SystemProperties
        Log.i("hookaw fphs1","11");
        new qgZSyjGOVNPGbBZt5NUv(paramLoadPackageParam, paramPhoneInfo);//android.os.BatteryManager
        Log.i("hookaw fphs1","22");
        new v9d7UHGMLCSrB5nW5P5A(paramLoadPackageParam, paramPhoneInfo);//android.bluetooth.BluetoothAdapter
        Log.i("hookaw fphs1","33");
        new EofATSK3OFqqbRjuc8sf(paramLoadPackageParam, paramPhoneInfo);//android.app.ApplicationPackageManager
        Log.i("hookaw fphs1","44");
        new uI9oCr52KBbqPYj1dkTi(paramLoadPackageParam, paramPhoneInfo);//android.hardware.SensorManager
        Log.i("hookaw fphs1","55");
        new sCYDTepZuATpaFuMmbHk(paramLoadPackageParam, paramPhoneInfo);//com.android.internal.telephony.PhoneSubInfo
        Log.i("hookaw fphs1","66");
        new FVKjWjKo1YaG6p5uD2qz(paramLoadPackageParam, paramPhoneInfo);//android.telephony.SignalStrength
        Log.i("hookaw fphs1","77");
        new Zv4WpY0X7HBbj6dlrG23(paramLoadPackageParam, paramPhoneInfo);//android.telephony.CellIdentityGsm
        Log.i("hookaw fphs1","88");

        try {
            XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", paramLoadPackageParam.classLoader, "getString",ContentResolver.class, String.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                    System.out.println("watch0 method--->android.os.Build android.provider.Settings.Secure.getString");
                    if (param.args[1].equals(Settings.Secure.ANDROID_ID)) {
                        param.setResult(paramPhoneInfo.getAndroidId());
                    }
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" Android ID 错误: " + ex.getMessage());
        }

        //以下代码 完全复制有问题
        //new B3ZVrvfLQhtD1BGEtWg(paramLoadPackageParam, paramPhoneInfo);屏幕大小
        // new pU9KwKC2ppIRCaiFhaek(paramLoadPackageParam, paramPhoneInfo);系统底层配置文件
        //new igA4LVuh1bDai6C7MDC3(paramLoadPackageParam, paramPhoneInfo);
        //new md3nHbhfwh4gUdnF0ILY(paramLoadPackageParam, paramPhoneInfo);
        // new yKttB8l7Lvtb88ABEzqS(paramLoadPackageParam, paramPhoneInfo);//wifi网络
        //new Edbw69C30UgVp2ocKByJ(paramLoadPackageParam, paramPhoneInfo);//开机时间，网速 点量
        //new p3qsLWuGbzc0VpuSX7fl(paramLoadPackageParam, paramPhoneInfo);安装包空间大小
    }
}
