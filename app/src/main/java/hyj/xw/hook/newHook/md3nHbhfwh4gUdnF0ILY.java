package hyj.xw.hook.newHook;

import android.app.PendingIntent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2018/4/2.
 */

public class md3nHbhfwh4gUdnF0ILY extends DHHdslt4SqYQ1hSj1a4Y
{
    private static NewPhoneInfo O00000oo;
    private static double O0000O0o = 686.0D * (2.0D * new Random().nextDouble());
    private CellLocation O00000o = O00000Oo();
    private ArrayList<CellInfo> O00000oO = O00000o0();

    public md3nHbhfwh4gUdnF0ILY(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O00000oo = paramPhoneInfo;
        O000000o(GsmCellLocation.class.getName(), "getLac", new Object[0]);
        O000000o(GsmCellLocation.class.getName(), "getCid", new Object[0]);
        O000000o(CdmaCellLocation.class.getName(), "getBaseStationLongitude", new Object[0]);
        O000000o(CdmaCellLocation.class.getName(), "getBaseStationLatitude", new Object[0]);
        O000000o(CdmaCellLocation.class.getName(), "getSystemId", new Object[0]);
        O000000o(CdmaCellLocation.class.getName(), "getNetworkId", new Object[0]);
        O000000o(CdmaCellLocation.class.getName(), "getBaseStationId", new Object[0]);
        O000000o(Location.class.getName(), "getLatitude", new Object[0]);
        O000000o(Location.class.getName(), "getLongitude", new Object[0]);
        String str1 = GsmCellLocation.class.getName();
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Bundle.class.getName();
        O000000o(str1, arrayOfObject1);
        O000000o(TelephonyManager.class.getName(), "getCellLocation", new Object[0]);
        String str2 = PhoneStateListener.class.getName();
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = CellLocation.class.getName();
        O000000o(str2, "onCellLocationChanged", arrayOfObject2);
        O000000o(TelephonyManager.class.getName(), "getAllCellInfo", new Object[0]);
        String str3 = PhoneStateListener.class.getName();
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = List.class.getName();
        O000000o(str3, "onCellInfoChanged", arrayOfObject3);
        O000000o(CellInfo.class.getName(), "isRegistered", new Object[0]);
        O000000o(LocationManager.class.getName(), "isProviderEnabled", new Object[] { String.class });
        O000000o(LocationManager.class.getName(), "getAllProviders", new Object[0]);
        String str4 = LocationManager.class.getName();
        Object[] arrayOfObject4 = new Object[2];
        arrayOfObject4[0] = Criteria.class.getName();
        arrayOfObject4[1] = Boolean.TYPE.getName();
        O000000o(str4, "getBestProvider", arrayOfObject4);
        O000000o(LocationManager.class.getName(), "getLastLocation", new Object[0]);
        String str5 = LocationManager.class.getName();
        Object[] arrayOfObject5 = new Object[1];
        arrayOfObject5[0] = String.class.getName();
        O000000o(str5, "getLastKnownLocation", arrayOfObject5);
        String str6 = LocationManager.class.getName();
        Object[] arrayOfObject6 = new Object[2];
        arrayOfObject6[0] = Criteria.class.getName();
        arrayOfObject6[1] = Boolean.TYPE.getName();
        O000000o(str6, "getProviders", arrayOfObject6);
        String str7 = LocationManager.class.getName();
        Object[] arrayOfObject7 = new Object[1];
        arrayOfObject7[0] = Boolean.TYPE.getName();
        O000000o(str7, "getProviders", arrayOfObject7);
        String str8 = LocationManager.class.getName();
        Object[] arrayOfObject8 = new Object[1];
        arrayOfObject8[0] = GpsStatus.Listener.class.getName();
        O000000o(str8, "addGpsStatusListener", arrayOfObject8);
        String str9 = LocationManager.class.getName();
        Object[] arrayOfObject9 = new Object[1];
        arrayOfObject9[0] = GpsStatus.NmeaListener.class.getName();
        O000000o(str9, "addNmeaListener", arrayOfObject9);
        String str10 = LocationManager.class.getName();
        Object[] arrayOfObject10 = new Object[1];
        arrayOfObject10[0] = GpsStatus.class.getName();
        O000000o(str10, "getGpsStatus", arrayOfObject10);
        String str11 = LocationManager.class.getName();
        Object[] arrayOfObject11 = new Object[4];
        arrayOfObject11[0] = "android.location.LocationRequest";
        arrayOfObject11[1] = LocationListener.class.getName();
        arrayOfObject11[2] = Looper.class.getName();
        arrayOfObject11[3] = PendingIntent.class.getName();
        O000000o(str11, "requestLocationUpdates", arrayOfObject11);
        if (Build.VERSION.SDK_INT > 22)
            O000000o(TelephonyManager.class.getName(), "getPhoneCount", new Object[0]);
        if (Build.VERSION.SDK_INT < 23)
            O000000o(TelephonyManager.class.getName(), "getNeighboringCellInfo", new Object[0]);
    }

    public static Location O000000o()
    {
        NewPhoneInfo localPhoneInfo = O00000oo;
        Location localLocation = null;
        if (localPhoneInfo != null)
            localLocation = O000000o(O00000oo);
        return localLocation;
    }

    public static Location O000000o(NewPhoneInfo paramPhoneInfo)
    {
        Location localLocation = new Location("gps");
        localLocation.setLatitude(paramPhoneInfo.getLatitude());
        localLocation.setLongitude(paramPhoneInfo.getLongitude());
        localLocation.setAccuracy(15.0F + 20.0F * new Random().nextFloat());
        localLocation.setAltitude(O0000O0o);
        localLocation.setTime(System.currentTimeMillis());
        localLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        return localLocation;
    }

    private CellLocation O00000Oo()
    {
        GsmCellLocation localGsmCellLocation = new GsmCellLocation();
        localGsmCellLocation.setLacAndCid(this.O00000o0.getLac(), this.O00000o0.getCi());
        return localGsmCellLocation;
    }

    private ArrayList<NeighboringCellInfo> O00000o()
    {
        ArrayList localArrayList = new ArrayList();
        List localList = this.O00000o0.getCellScan();
        try
        {
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext())
            {
                String str = (String)localIterator.next();
                NeighboringCellInfo localNeighboringCellInfo = new NeighboringCellInfo();
                Field localField1 = localNeighboringCellInfo.getClass().getDeclaredField("mRssi");
                Field localField2 = localNeighboringCellInfo.getClass().getDeclaredField("mLac");
                Field localField3 = localNeighboringCellInfo.getClass().getDeclaredField("mCid");
                Field localField4 = localNeighboringCellInfo.getClass().getDeclaredField("mPsc");
                Field localField5 = localNeighboringCellInfo.getClass().getDeclaredField("mNetworkType");
                localField1.setAccessible(true);
                localField2.setAccessible(true);
                localField3.setAccessible(true);
                localField4.setAccessible(true);
                localField5.setAccessible(true);
                if (this.O00000o0.getPhoneType() != 2)
                {
                    localField5.set(localNeighboringCellInfo, Integer.valueOf(this.O00000o0.getNetworkType()));
                    localField4.set(localNeighboringCellInfo, Integer.valueOf(-1));
                    localField3.set(localNeighboringCellInfo, str.split(",")[0]);
                    localField2.set(localNeighboringCellInfo, str.split(",")[1]);
                    localField1.set(localNeighboringCellInfo, Integer.valueOf(10 + new Random().nextInt(20)));
                    localArrayList.add(localNeighboringCellInfo);
                }
            }
        }
        catch (Exception localException)
        {
            Log.e("LocationHook", "getNeighorCellInfo error ", localException);
        }
        return localArrayList;
    }

    private ArrayList<CellInfo> O00000o0()
    {
        ArrayList localArrayList = new ArrayList();
        String str = this.O00000o0.getMnc();
        int i = this.O00000o0.getLac();
        int j = this.O00000o0.getCi();
        CellInfoGsm localCellInfoGsm = (CellInfoGsm) XposedHelpers.newInstance(CellInfoGsm.class, new Object[0]);
        Object[] arrayOfObject1 = new Object[1];
        Object[] arrayOfObject2 = new Object[4];
        arrayOfObject2[0] = Integer.valueOf(460);
        arrayOfObject2[1] = Integer.valueOf(str);
        arrayOfObject2[2] = Integer.valueOf(i);
        arrayOfObject2[3] = Integer.valueOf(j);
        arrayOfObject1[0] = XposedHelpers.newInstance(CellIdentityGsm.class, arrayOfObject2);
        XposedHelpers.callMethod(localCellInfoGsm, "setCellIdentity", arrayOfObject1);
        localArrayList.add(localCellInfoGsm);
        return localArrayList;
    }



    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam xc_MethodHook$MethodHookParam) throws Throwable {
        final String name = xc_MethodHook$MethodHookParam.method.getName();
        xc_MethodHook$MethodHookParam.method.getDeclaringClass().getName();
        Label_0056: {
            if ("getCellLocation".equals(name) || "onCellLocationChanged".equals(name)) {
                if (this.O00000o != null) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o);
                }
            }
            else if ("getAllCellInfo".equals(name)) {
                if (this.O00000oO != null) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000oO);
                }
            }
            else if ("isRegistered".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)true);
            }
            else if ("getLastLocation".equals(name) || "getLastKnownLocation".equals(name)) {
               /* Location O00000o = O00000o(this.O00000o0);
                if (O00000o != null) {
                    xc_MethodHook$MethodHookParam.setResult((Object)O00000o);
                }
               // \u69fe.O00000o("LocationHook", "getLastKnownLocation --> location = " + O00000o);*/
            }
            else if ("getProviders".equals(name)) {
                final ArrayList<String> result = new ArrayList<String>();
                result.add("gps");
                result.add("network");
                xc_MethodHook$MethodHookParam.setResult((Object)result);
            }
            else if ("getBestProvider".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)"gps");
            }
            else if ("addGpsStatusListener".equals(name)) {
                if (xc_MethodHook$MethodHookParam.args[0] != null) {
                    XposedHelpers.callMethod(xc_MethodHook$MethodHookParam.args[0], "onGpsStatusChanged", new Object[] { 1 });
                    XposedHelpers.callMethod(xc_MethodHook$MethodHookParam.args[0], "onGpsStatusChanged", new Object[] { 4 });
                }
            }
            else if ("addNmeaListener".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)false);
            }
            else if ("getGpsStatus".equals(name)) {
                final GpsStatus result2 = (GpsStatus)xc_MethodHook$MethodHookParam.getResult();
                if (result2 != null) {
                    final Method[] declaredMethods = GpsStatus.class.getDeclaredMethods();
                    final int length = declaredMethods.length;
                    int i = 0;
                    while (true) {
                        while (i < length) {
                            final Method method = declaredMethods[i];
                            if (method.getName().equals("setStatus") && method.getParameterTypes().length > 1) {
                                final int[] array = { 1, 2, 3, 4, 5 };
                                final float[] array2 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
                                final float[] array3 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
                                final float[] array4 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
                                XposedHelpers.callMethod((Object)result2, "setStatus", new Object[] { 5, array, array2, array3, array4, 31, 31, 31 });
                                xc_MethodHook$MethodHookParam.setResult(xc_MethodHook$MethodHookParam.args[0] = result2);
                                if (method != null) {
                                    try {
                                        method.setAccessible(true);
                                        method.invoke(result2, 5, array, array2, array3, array4, 31, 31, 31);
                                        xc_MethodHook$MethodHookParam.setResult((Object)result2);
                                    }
                                    catch (Exception ex) {
                                        //\u69fe.O00000o("LocationHook", "getGpsStatus hook error ", ex);
                                    }
                                }
                                break Label_0056;
                            }
                            else {
                                ++i;
                            }
                        }
                        final Method method = null;
                        continue;
                    }
                }
                return;
            }
            else if ("getPhoneCount".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)1);
            }
            else if ("getNeighboringCellInfo".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0());
            }
            else if ("getAllProviders".equals(name)) {
                final ArrayList<String> result3 = new ArrayList<String>();
                result3.add("network");
                result3.add("gps");
                xc_MethodHook$MethodHookParam.setResult((Object)result3);
            }
            else if ("getLac".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getLac());
            }
            else if ("getCid".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getCi());
            }
            else if ("getBaseStationLongitude".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)(int)this.O00000o0.getLongitude());
            }
            else if ("getBaseStationLatitude".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)(int)this.O00000o0.getLatitude());
            }
            else if ("getSystemId".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getMnc());
            }
            else if ("getNetworkId".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getLac());
            }
            else if ("getBaseStationId".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getCi());
            }
            else if ("getLongitude".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getLongitude());
            }
            else if ("getLatitude".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getLatitude());
            }
            else if ("isProviderEnabled".equals(name)) {
                final String s = (String)xc_MethodHook$MethodHookParam.args[0];
                if ("gps".equals(s) || "network".equals(s)) {
                    xc_MethodHook$MethodHookParam.setResult((Object)true);
                }
            }
            else if ("getScanResults".equals(name)) {
               /* final ArrayList<ScanResult> result4 = new ArrayList<ScanResult>();
                final List<String> wifiScan = this.O00000o0.getWifiScan();
                final Constructor<?> constructor = Class.forName("android.net.wifi.ScanResult").getConstructor(WifiSsid.class, String.class, String.class, Integer.TYPE, Integer.TYPE, Long.TYPE, Integer.TYPE, Integer.TYPE);
                constructor.setAccessible(true);
                final Iterator<String> iterator = wifiScan.iterator();
                while (iterator.hasNext()) {
                    final String[] split = iterator.next().split(",");
                    result4.add((ScanResult)constructor.newInstance(WifiSsid.createFromAsciiEncoded(split[0]), split[1], split[2], Integer.parseInt(split[3]), Integer.parseInt(split[4]), Long.parseLong(split[5]), -1, -1));
                }
                xc_MethodHook$MethodHookParam.setResult((Object)result4);*/
            }
        }
        super.afterHookedMethod(xc_MethodHook$MethodHookParam);
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam xc_MethodHook$MethodHookParam) throws Throwable {
        super.beforeHookedMethod(xc_MethodHook$MethodHookParam);
        final String name = xc_MethodHook$MethodHookParam.method.getName();
        final String name2 = xc_MethodHook$MethodHookParam.method.getDeclaringClass().getName();
        if ("onCellInfoChanged".equals(name)) {
            if (this.O00000oO != null) {
                xc_MethodHook$MethodHookParam.args[0] = this.O00000oO;
            }
        }
        else if ("requestLocationUpdates".equals(name)) {
            final LocationListener locationListener = (LocationListener)xc_MethodHook$MethodHookParam.args[1];
            if (locationListener != null) {
                final Method[] declaredMethods = LocationListener.class.getDeclaredMethods();
                final int length = declaredMethods.length;
                int n = 0;
                while (true) {
                    Label_0209: {
                        Method method;
                        if (n < length) {
                            method = declaredMethods[n];
                            if (!method.getName().equals("onLocationChanged") || Modifier.isAbstract(method.getModifiers())) {
                                break Label_0209;
                            }
                        }
                        else {
                            method = null;
                        }
                       /* try {
                            final Location O00000o = O00000o(this.O00000o0);
                            XposedHelpers.callMethod((Object)locationListener, "onLocationChanged", new Object[] { O00000o });
                            if (method == null) {
                                break;
                            }
                            method.setAccessible(true);
                            if (O00000o != null) {
                                method.invoke(locationListener, O00000o);
                                return;
                            }
                            break;
                        }
                        catch (Exception ex) {
                            //\u69fe.O00000o("LocationHook", "requestLocationUpdates error", ex);
                            return;
                        }*/
                    }
                    ++n;
                }
            }
        }
        else if (GsmCellLocation.class.getName().equals(name2) && xc_MethodHook$MethodHookParam.args != null && xc_MethodHook$MethodHookParam.args.length > 0) {
            final Bundle bundle = (Bundle)xc_MethodHook$MethodHookParam.args[0];
            bundle.putInt("lac", this.O00000o0.getLac());
            bundle.putInt("cid", this.O00000o0.getCi());
            bundle.putInt("psc", -1);
        }
    }
}
