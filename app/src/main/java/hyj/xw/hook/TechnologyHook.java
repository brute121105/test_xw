package hyj.xw.hook;

import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.model.PhoneInfo;

/**
 * Created by Administrator on 2018/1/4.
 */


public class TechnologyHook extends MyMethodHook
{
    public TechnologyHook(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        hoodMethodByParms(TelephonyManager.class.getName(), "getDeviceId", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getSimSerialNumber", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getSubscriberId", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getLine1Number", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getSimCountryIso", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getSimOperator", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getSimOperatorName", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getNetworkCountryIso", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getNetworkOperator", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getNetworkOperatorName", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getNetworkType", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getPhoneType", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "hasIccCard", new Object[0]);
        String str1 = TelephonyManager.class.getName();
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Integer.TYPE.getName();
        hoodMethodByParms(str1, "getNetworkClass", arrayOfObject1);
        String str2 = TelephonyManager.class.getName();
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Integer.TYPE.getName();
        hoodMethodByParms(str2, "getNetworkTypeName", arrayOfObject2);
        hoodMethodByParms(TelephonyManager.class.getName(), "isSmsCapable", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getDataState", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getCallState", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getIsimImpi", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getIsimDomain", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getIsimImpu", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getLine1AlphaTag", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getMsisdn", new Object[0]);
        hoodMethodByParms(TelephonyManager.class.getName(), "getSimState", new Object[0]);
        hookMethod(TelephonyManager.class.getName(), "getVoiceMailAlphaTag");
        hookMethod(TelephonyManager.class.getName(), "getMmsUserAgent");
        hookMethod(TelephonyManager.class.getName(), "getMmsUAProfUrl");
        hookMethod(TelephonyManager.class.getName(), "getDeviceSoftwareVersion");
        hookMethod(TelephonyManager.class.getName(), "getProccmdline");
        hookMethod("com.android.internal.telephony.PhoneSubInfo", "getDeviceId");
        hookMethod("com.android.internal.telephony.PhoneSubInfo", "getImei");
        hookMethod("com.android.internal.telephony.PhoneSubInfo", "getDeviceSvn");
        hookMethod("com.android.internal.telephony.PhoneSubInfo", "getIccSerialNumber");
        hookMethod("com.android.internal.telephony.PhoneSubInfo", "getLine1Number");
        hookMethod("com.android.internal.telephony.PhoneSubInfo", "getSubscriberId");
        hookMethod("com.android.internal.telephony.PhoneSubInfo", "getLine1AlphaTag");
        hookMethod("android.telephony.MSimTelephonyManager", "getDeviceId");
        hookMethod("android.telephony.MSimTelephonyManager", "getSubscriberId");
        hoodMethodByParms(ServiceState.class.getName(), "getState", new Object[0]);
        hoodMethodByParms(ServiceState.class.getName(), "getNetworkType", new Object[0]);
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        super.afterHookedMethod(paramMethodHookParam);
        String str1 = paramMethodHookParam.method.getName();
        String str2 = paramMethodHookParam.method.getDeclaringClass().getName();
        if ("hasIccCard".equals(str1)){
            paramMethodHookParam.setResult(Boolean.valueOf(true));
        }
        //while (true)
        //{
            if ("isSmsCapable".equals(str1))
            {
                paramMethodHookParam.setResult(Boolean.valueOf(true));
            }
            else if ("getDataState".equals(str1))
            {
                paramMethodHookParam.setResult(Integer.valueOf(2));
            }
            else if ("getCallState".equals(str1))
            {
                paramMethodHookParam.setResult(Integer.valueOf(0));
            }
            else if (("getLine1AlphaTag".equals(str1)) || ("getMsisdn".equals(str1)))
            {
                paramMethodHookParam.setResult(this.phoneInfo.getLineNumber());
            }
            else if ("getState".equals(str1))
            {
                paramMethodHookParam.setResult(Integer.valueOf(0));
            }
            else if ("getDataActivity".equals(str1))
            {
                paramMethodHookParam.setResult(Integer.valueOf(0));
            }
            else if (("getVoiceMailAlphaTag".equals(str1)) || ("getVoiceMailNumber".equals(str1)))
            {
                paramMethodHookParam.setResult(this.phoneInfo.getLineNumber());
            }

            else if ("getDeviceSoftwareVersion".equals(str1))
            {
                paramMethodHookParam.setResult("00");
            }

            else if ((ServiceState.class.getName().equals(str2)) && ("getNetworkType".equals(str1)))
            {
                paramMethodHookParam.setResult(Integer.valueOf(this.phoneInfo.getNetworkType()));
            }
            else if ("getImei".equals(str1))
            {
                paramMethodHookParam.setResult(this.phoneInfo.getDeviceId());
            }
            else if ("getIccSerialNumber".equals(str1))
            {
                paramMethodHookParam.setResult(this.phoneInfo.getSimSerialNumber());
            }
        //}
    }
}