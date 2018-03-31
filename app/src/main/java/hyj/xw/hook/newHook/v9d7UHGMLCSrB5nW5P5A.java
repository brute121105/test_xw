package hyj.xw.hook.newHook;

import android.text.TextUtils;

import java.util.HashSet;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class v9d7UHGMLCSrB5nW5P5A extends DHHdslt4SqYQ1hSj1a4Y
{
    private static final int[] O00000o = { 20, 20, 20, 21, 23 };
    private static final int[] O00000oO = { 12, 10, 10, 10, 11, 13 };

    public v9d7UHGMLCSrB5nW5P5A(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("android.bluetooth.BluetoothAdapter", "getName");
        O000000o("android.bluetooth.BluetoothAdapter", "getBondedDevices");
        O000000o("android.bluetooth.BluetoothAdapter", "getScanMode");
        O000000o("android.bluetooth.BluetoothAdapter", "getState");
        O000000o("android.bluetooth.BluetoothAdapter", "getAddress");
        O000000o("android.bluetooth.BluetoothDevice", "getName");
        O000000o("android.bluetooth.BluetoothDevice", "getAddress");
        O000000o("android.bluetooth.BluetoothDevice", "getBondedDevices");
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        String str = paramMethodHookParam.method.getName();
        if (str.equals("getName"))
            if (!TextUtils.isEmpty(this.O00000o0.getBtName()))
                paramMethodHookParam.setResult(this.O00000o0.getBtName());

        if (str.equals("getState"))
            paramMethodHookParam.setResult(Integer.valueOf(O00000oO[new java.util.Random().nextInt(O00000oO.length)]));
        else if (str.equals("getScanMode"))
            paramMethodHookParam.setResult(Integer.valueOf(O00000o[new java.util.Random().nextInt(O00000o.length)]));
        else if (str.equals("getBondedDevices"))
            paramMethodHookParam.setResult(new HashSet());
        else if ((str.equals("getAddress")) && (!TextUtils.isEmpty(this.O00000o0.getBtAddress())))
            paramMethodHookParam.setResult(this.O00000o0.getBtAddress());
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.beforeHookedMethod(paramMethodHookParam);
    }
}