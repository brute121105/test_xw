package hyj.xw.hook.newHook;

import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class sCYDTepZuATpaFuMmbHk extends DHHdslt4SqYQ1hSj1a4Y
{
    public sCYDTepZuATpaFuMmbHk(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o(TelephonyManager.class.getName(), "getDeviceId", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getSimSerialNumber", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getSubscriberId", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getLine1Number", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getSimCountryIso", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getSimOperator", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getSimOperatorName", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getNetworkCountryIso", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getNetworkOperator", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getNetworkOperatorName", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getNetworkType", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getPhoneType", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "hasIccCard", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getNetworkClass", new Object[] { Integer.TYPE.getName() });
        O000000o(TelephonyManager.class.getName(), "getNetworkTypeName", new Object[] { Integer.TYPE.getName() });
        O000000o(TelephonyManager.class.getName(), "isSmsCapable", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getDataState", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getCallState", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getIsimImpi", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getIsimDomain", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getIsimImpu", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getLine1AlphaTag", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getMsisdn", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getSimState", new Object[0]);
        O000000o(TelephonyManager.class.getName(), "getVoiceMailAlphaTag");
        O000000o(TelephonyManager.class.getName(), "getMmsUserAgent");
        O000000o(TelephonyManager.class.getName(), "getMmsUAProfUrl");
        O000000o(TelephonyManager.class.getName(), "getDeviceSoftwareVersion");
        O000000o(TelephonyManager.class.getName(), "getProccmdline");
        O000000o("com.android.internal.telephony.PhoneSubInfo", "getDeviceId");
        O000000o("com.android.internal.telephony.PhoneSubInfo", "getImei");
        O000000o("com.android.internal.telephony.PhoneSubInfo", "getDeviceSvn");
        O000000o("com.android.internal.telephony.PhoneSubInfo", "getIccSerialNumber");
        O000000o("com.android.internal.telephony.PhoneSubInfo", "getLine1Number");
        O000000o("com.android.internal.telephony.PhoneSubInfo", "getSubscriberId");
        O000000o("com.android.internal.telephony.PhoneSubInfo", "getLine1AlphaTag");
        O000000o("android.telephony.MSimTelephonyManager", "getDeviceId");
        O000000o("android.telephony.MSimTelephonyManager", "getSubscriberId");
        O000000o(ServiceState.class.getName(), "getState", new Object[0]);
        O000000o(ServiceState.class.getName(), "getNetworkType", new Object[0]);
    }

    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        String str1 = paramMethodHookParam.method.getName();
        String str2 = paramMethodHookParam.method.getDeclaringClass().getName();
        if ("hasIccCard".equals(str1)){
            paramMethodHookParam.setResult(Boolean.valueOf(true));
        }else if ("isSmsCapable".equals(str1))
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
        else if ("getIsimImpi".equals(str1))
        {
            //FVKjWjKo1YaG6p5uD2qz.O00000Oo("TelManagerHook", "getIsimImpi --> " + paramMethodHookParam.getResult());
        }
        else if ("getIsimDomain".equals(str1))
        {
            /// FVKjWjKo1YaG6p5uD2qz.O00000Oo("TelManagerHook", "getIsimDomain --> " + paramMethodHookParam.getResult());
        }
        else if ("getIsimImpu".equals(str1))
        {
            // FVKjWjKo1YaG6p5uD2qz.O00000Oo("TelManagerHook", "getIsimImpu --> " + paramMethodHookParam.getResult());
        }
        else if (("getLine1AlphaTag".equals(str1)) || ("getMsisdn".equals(str1)))
        {
            paramMethodHookParam.setResult(this.O00000o0.getLine1Number());
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
            paramMethodHookParam.setResult(this.O00000o0.getLine1Number());
        }
        else if (("getMmsUserAgent".equals(str1)) || ("getMmsUAProfUrl".equals(str1)))
        {
            // paramMethodHookParam.setResult(com.money.O00000Oo.Edbw69C30UgVp2ocKByJ.O0000Oo0(10));
        }
        else if ("getDeviceSoftwareVersion".equals(str1))
        {
            paramMethodHookParam.setResult("00");
        }
        else if ("getProccmdline".equals(str1))
        {
            //paramMethodHookParam.setResult(com.money.utils.Edbw69C30UgVp2ocKByJ.O000000o("proccmdline"));
        }
        else if ((ServiceState.class.getName().equals(str2)) && ("getNetworkType".equals(str1)))
        {
            paramMethodHookParam.setResult(Integer.valueOf(this.O00000o0.getNetworkType()));
        }
        else if ("getImei".equals(str1))
        {
            paramMethodHookParam.setResult(this.O00000o0.getDeviceId());
        }
        else if ("getIccSerialNumber".equals(str1))
        {
            paramMethodHookParam.setResult(this.O00000o0.getSimSerialNumber());
        }
        else
        {
            Object localObject = this.O00000o0.getClass().getMethod(str1, new Class[0]).invoke(this.O00000o0, new Object[0]);
            if (localObject != null)
                paramMethodHookParam.setResult(localObject);
        }
        super.afterHookedMethod(paramMethodHookParam);
    }
}
