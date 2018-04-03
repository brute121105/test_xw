package hyj.xw.hook.newHook;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2018/4/2.
 */

public class yKttB8l7Lvtb88ABEzqS extends DHHdslt4SqYQ1hSj1a4Y
{
    private Inet4Address O00000o;

    public yKttB8l7Lvtb88ABEzqS(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o(NetworkInfo.class.getName(), "getDetailedState", new Object[0]);
        O000000o(NetworkInfo.class.getName(), "getExtraInfo", new Object[0]);
        O000000o(NetworkInfo.class.getName(), "getTypeName", new Object[0]);
        O000000o(NetworkInfo.class.getName(), "getType", new Object[0]);
        O000000o(NetworkInfo.class.getName(), "isRoaming", new Object[0]);
        O000000o(NetworkInfo.class.getName(), "isConnected", new Object[0]);
        O000000o(NetworkInfo.class.getName(), "isAvailable", new Object[0]);
        O000000o(NetworkInfo.class.getName(), "isConnectedOrConnecting", new Object[0]);
        O000000o(NetworkInfo.class.getName(), "getSubtypeName");
        O000000o(NetworkInfo.class.getName(), "getSubtype");
        O000000o(ConnectivityManager.class.getName(), "getNetworkInfo", new Object[] { Integer.TYPE.getName() });
        O000000o(ConnectivityManager.class.getName(), "getMobileDataEnabled", new Object[0]);
        O000000o(ConnectivityManager.class.getName(), "getActiveNetworkInfo", new Object[0]);
        O000000o(NetworkInterface.class.getName(), "getHardwareAddress", new Object[0]);
        O000000o(NetworkInterface.class.getName(), "getInetAddresses", new Object[0]);
        O000000o(NetworkInterface.class.getName(), "getInterfaceAddresses", new Object[0]);
        O000000o(NetworkInterface.class.getName(), "getNetworkInterfaces");
        O000000o(NetworkInterface.class.getName(), "getName");
        O000000o(InetAddress.class.getName(), "getHostAddress", new Object[0]);
        O000000o("android.net.wifi.WifiInfo", "getLinkSpeed");
        O000000o("android.net.wifi.WifiInfo", "getNetworkId");
        O000000o("android.net.wifi.WifiInfo", "getRssi");
        O000000o("android.net.wifi.WifiInfo", "getBSSID");
        O000000o("android.net.wifi.WifiInfo", "getMacAddress");
        O000000o("android.net.wifi.WifiInfo", "getSSID");
        O000000o("android.net.wifi.WifiInfo", "getIpAddress");
        O000000o("android.net.wifi.WifiManager", "getWifiState");
        O000000o("android.net.wifi.WifiManager", "isWifiEnabled");
        O000000o("android.net.wifi.WifiManager", "getDhcpInfo");
        if (!TextUtils.isEmpty(this.O00000o0.getIpAddress()))
            this.O00000o = ((Inet4Address)O00000o(this.O00000o0.getIpAddress()));
    }

    public static int O000000o(Inet4Address paramInet4Address)
    {
        byte[] arrayOfByte = paramInet4Address.getAddress();
        return (0xFF & arrayOfByte[3]) << 24 | (0xFF & arrayOfByte[2]) << 16 | (0xFF & arrayOfByte[1]) << 8 | 0xFF & arrayOfByte[0];
    }

    public static int O00000Oo(String paramString)
    {
        String[] arrayOfString = paramString.split("\\.");
        if (arrayOfString.length != 4)
            return 0;
        return 0 + Integer.parseInt(arrayOfString[0]) + (Integer.parseInt(arrayOfString[1]) << 8) + (Integer.parseInt(arrayOfString[2]) << 16) + (Integer.parseInt(arrayOfString[3]) << 24);
    }

    private static InetAddress O00000o(String s) {
    final int[] array = new int[4];
    final int[] array2 = new int[4];
    final String[] array3 = new String[4];
    final byte[] array4 = new byte[4];
    final String[] split = s.split("\\.");
    for (int i = 0; i < 4; ++i) {
        array[i] = Integer.valueOf(split[i]);
        if (array[i] <= 127 && array[i] >= -127) {
            array2[i] = array[i];
        }
        else {
            array2[i] = -256 + array[i];
        }
        array3[i] = String.valueOf(array2[i]);
        array4[i] = Byte.parseByte(array3[i]);
    }
    try {
        return InetAddress.getByAddress(array4);
    }
    catch (UnknownHostException ex) {
        ex.printStackTrace();
        return null;
    }
}

    public byte[] O000000o(String paramString)
    {
        byte[] arrayOfByte = new byte[6];
        String[] arrayOfString = paramString.split(":");
        for (int i = 0; i < arrayOfString.length; i++)
            arrayOfByte[i] = ((byte)Integer.parseInt(arrayOfString[i], 16));
        return arrayOfByte;
    }

    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam xc_MethodHook$MethodHookParam) throws NoSuchFieldException, IllegalAccessException {
        int accessible = 1;
        final String name = xc_MethodHook$MethodHookParam.method.getName();
        if ("getDetailedState".equals(name)) {
            xc_MethodHook$MethodHookParam.setResult(NetworkInfo.DetailedState.CONNECTED);
        }
        else {
            if ("isRoaming".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)false);
                return;
            }
            if ("isConnected".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)(boolean)(accessible != 0));
                return;
            }
            if ("isAvailable".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)(boolean)(accessible != 0));
                return;
            }
            if ("isConnectedOrConnecting".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)(boolean)(accessible != 0));
                return;
            }
            if ("getExtraInfo".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getExtraInfo());
                return;
            }
            if ("getTypeName".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getTypeName());
                return;
            }
            if ("getType".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getType());
                return;
            }
            if ("getNetworkInfo".equals(name)) {
              /*  final NetworkInfo result = (NetworkInfo)xc_MethodHook$MethodHookParam.getResult();
                if (result != null) {
                    final Field declaredField = NetworkInfo.class.getDeclaredField("mState");
                    final Field declaredField2 = NetworkInfo.class.getDeclaredField("mIsAvailable");
                    declaredField.setAccessible(accessible != 0);
                    declaredField2.setAccessible(accessible != 0);
                    final int intValue = (int)xc_MethodHook$MethodHookParam.args[0];
                    final int type = this.O00000o0.getType();
                    declaredField2.set(result, intValue == type && accessible);
                    String s;
                    if (intValue == type) {
                        s = "CONNECTED";
                    }
                    else {
                        s = "DISCONNECTED";
                    }
                    declaredField.set(result, NetworkInfo.State.valueOf(s));
                    xc_MethodHook$MethodHookParam.setResult((Object)result);
                }*/
            }
            else {
                if ("getMobileDataEnabled".equals(name)) {
                    if (this.O00000o0.getType() != 0) {
                        accessible = 0;
                    }
                    xc_MethodHook$MethodHookParam.setResult((Object)(boolean)(accessible != 0));
                    return;
                }
                if ("getLinkSpeed".equals(name)) {
                    if (this.O00000o0.getType() == accessible) {
                        xc_MethodHook$MethodHookParam.setResult((Object)(100 + new Random().nextInt(40)));
                    }
                }
                else {
                    if ("getNetworkId".equals(name)) {
                        xc_MethodHook$MethodHookParam.setResult((Object)(1 + new Random().nextInt(10)));
                        return;
                    }
                    if ("getRssi".equals(name)) {
                        if (this.O00000o0.getType() == accessible) {
                            xc_MethodHook$MethodHookParam.setResult((Object)(-70 + new Random().nextInt(10)));
                            return;
                        }
                        xc_MethodHook$MethodHookParam.setResult((Object)(-200));
                    }
                    else if ("getIpAddress".equals(name)) {
                        if (this.O00000o != null && this.O00000o0.getType() == accessible) {
                            xc_MethodHook$MethodHookParam.setResult((Object)O000000o(this.O00000o));
                        }
                    }
                    else {
                        if ("getBSSID".equals(name)) {
                            String bssid;
                            if (this.O00000o0.getType() == accessible) {
                                bssid = this.O00000o0.getBSSID();
                            }
                            else {
                                bssid = null;
                            }
                            xc_MethodHook$MethodHookParam.setResult((Object)bssid);
                            return;
                        }
                        if ("getMacAddress".equals(name)) {
                            xc_MethodHook$MethodHookParam.setResult((Object)"02:00:00:00:00:00");
                            return;
                        }
                        if ("getSSID".equals(name)) {
                            String ssid;
                            if (this.O00000o0.getType() == accessible) {
                                ssid = this.O00000o0.getSSID();
                            }
                            else {
                                ssid = "0x";
                            }
                            xc_MethodHook$MethodHookParam.setResult((Object)ssid);
                            return;
                        }
                        if ("getWifiState".equals(name)) {
                            xc_MethodHook$MethodHookParam.setResult((Object)3);
                            return;
                        }
                        if ("isWifiEnabled".equals(name)) {
                            if (this.O00000o0.getType() != accessible) {
                                accessible = 0;
                            }
                            xc_MethodHook$MethodHookParam.setResult((Object)(boolean)(accessible != 0));
                            return;
                        }
                        if ("getHardwareAddress".equals(name)) {
                            xc_MethodHook$MethodHookParam.setResult((Object)this.O000000o(this.O00000o0.getMacAddress()));
                            return;
                        }
                        if ("getInetAddresses".equals(name)) {
                            final ArrayList<Object> list = new ArrayList<Object>();
                            final Enumeration enumeration = (Enumeration)xc_MethodHook$MethodHookParam.getResult();
                            while (enumeration.hasMoreElements()) {
                                InetAddress inetAddress = (InetAddress)enumeration.nextElement();
                                if (inetAddress.isAnyLocalAddress() && !inetAddress.isLoopbackAddress()) {
                                    if (this.O00000o0.getType() == accessible && this.O00000o != null) {
                                        list.add(this.O00000o);
                                    }
                                    else {
                                        list.add(inetAddress);
                                    }
                                }
                            }
                            xc_MethodHook$MethodHookParam.setResult((Object)Collections.enumeration(list));
                            return;
                        }
                        if ("getHostAddress".equals(name)) {
                            final InetAddress inetAddress2 = (InetAddress)xc_MethodHook$MethodHookParam.thisObject;
                            if (inetAddress2 != null && !inetAddress2.isLoopbackAddress() && !inetAddress2.isLinkLocalAddress()) {
                                Log.d("NetWorkHook", "getHostAddress --> " + this.O00000o0.getIpAddress() + " -> " + xc_MethodHook$MethodHookParam.getResult());
                            }
                        }
                        else if ("getSubtype".equals(name)) {
                            if (this.O00000o0.getType() == 0) {
                                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getNetworkType());
                                return;
                            }
                            xc_MethodHook$MethodHookParam.setResult((Object)0);
                        }
                        else if ("getSubtypeName".equals(name)) {
                            if (this.O00000o0.getType() == 0) {
                                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getNetworkTypeName());
                                return;
                            }
                            xc_MethodHook$MethodHookParam.setResult((Object)"");
                        }
                        else if ("getName".equals(name)) {
                            final String s2 = (String)xc_MethodHook$MethodHookParam.getResult();
                            if ("tun0".equals(s2) || "ppp0".equals(s2)) {
                                xc_MethodHook$MethodHookParam.setResult((Object)"sit0");
                            }
                        }
                        else if ("getNetworkInterfaces".equals(name)) {
                            final Enumeration enumeration2 = (Enumeration)xc_MethodHook$MethodHookParam.getResult();
                            final ArrayList<Object> list2 = new ArrayList<Object>();
                            if (enumeration2 != null) {
                                for (final NetworkInterface networkInterface : Collections.list((Enumeration<NetworkInterface>)enumeration2)) {
                                    if (!"tun0".equals(networkInterface.getName()) && !"ppp0".equals(networkInterface.getName())) {
                                        list2.add(networkInterface);
                                    }
                                }
                                xc_MethodHook$MethodHookParam.setResult((Object)Collections.enumeration(list2));
                            }
                        }
                        else if ("getActiveNetworkInfo".equals(name)) {
                            final NetworkInfo result2 = (NetworkInfo)xc_MethodHook$MethodHookParam.getResult();
                            if (result2 != null) {
                                final Field declaredField3 = result2.getClass().getDeclaredField("mState");
                                declaredField3.setAccessible(accessible != 0);
                                declaredField3.set(result2, NetworkInfo.State.valueOf("CONNECTED"));
                                final Field declaredField4 = result2.getClass().getDeclaredField("mIsAvailable");
                                declaredField4.setAccessible(accessible != 0);
                                declaredField4.set(result2, (boolean)(accessible != 0));
                                if (this.O00000o0.getType() == accessible) {
                                    final Field declaredField5 = result2.getClass().getDeclaredField("mExtraInfo");
                                    declaredField5.setAccessible(accessible != 0);
                                    declaredField5.set(result2, this.O00000o0.getSSID());
                                }
                                xc_MethodHook$MethodHookParam.setResult((Object)result2);
                            }
                        }
                        else if ("getDhcpInfo".equals(name)) {
                           /* if (this.O00000o0.getType() == accessible) {
                                final DhcpInfo dhcpInfo = (DhcpInfo)xc_MethodHook$MethodHookParam.getResult();
                                if (dhcpInfo != null) {
                                    final int \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c21 = \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c21(\u93c9\u30e5\u60c2.\u93b4\u621c\u7bc3\u93c4(accessible));
                                    final int \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c212 = \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c21(\u93c9\u30e5\u60c2.\u93b4\u621c\u7bc3\u93c4(accessible));
                                    final int \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c213 = \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c21(\u93c9\u30e5\u60c2.\u93b4\u621c\u7bc3\u93c4(accessible));
                                    final int \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c214 = \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c21(\u93c9\u30e5\u60c2.\u93b4\u621c\u7bc3\u93c4(accessible));
                                    dhcpInfo.dns1 = \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c21;
                                    dhcpInfo.dns2 = \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c212;
                                    dhcpInfo.gateway = \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c213;
                                    dhcpInfo.serverAddress = \u93b4\u621c\u7b09\u9a9e\u8e6d\u7c214;
                                }
                            }*/
                        }
                        else if ("getInterfaceAddresses".equals(name)) {
                            Log.d("NetWorkHook", " who who who getInterfaceAddresses");
                        }
                    }
                }
            }
        }
    }

}
