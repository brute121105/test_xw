package hyj.xw.hook.newHook;

import android.content.ContentResolver;
import android.provider.Settings;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class nsEo5EnMUO99qwl4QERu
{
    public static void O000000o(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, final NewPhoneInfo paramPhoneInfo)
    {

        new GoicqOVehRY68RCP7lXb(paramLoadPackageParam, paramPhoneInfo);//ro.product.model
        new iJzwYzrQSz7shvm8fphs(paramLoadPackageParam, paramPhoneInfo);//android.os.SystemProperties
        new qgZSyjGOVNPGbBZt5NUv(paramLoadPackageParam, paramPhoneInfo);//android.os.BatteryManager
        new v9d7UHGMLCSrB5nW5P5A(paramLoadPackageParam, paramPhoneInfo);//android.bluetooth.BluetoothAdapter
        new EofATSK3OFqqbRjuc8sf(paramLoadPackageParam, paramPhoneInfo);//android.app.ApplicationPackageManager
        new uI9oCr52KBbqPYj1dkTi(paramLoadPackageParam, paramPhoneInfo);//android.hardware.SensorManager
        new sCYDTepZuATpaFuMmbHk(paramLoadPackageParam, paramPhoneInfo);//com.android.internal.telephony.PhoneSubInfo
        new FVKjWjKo1YaG6p5uD2qz(paramLoadPackageParam, paramPhoneInfo);//android.telephony.SignalStrength
        new Zv4WpY0X7HBbj6dlrG23(paramLoadPackageParam, paramPhoneInfo);//android.telephony.CellIdentityGsm

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
