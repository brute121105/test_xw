package hyj.xw.hook.newHook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class FVKjWjKo1YaG6p5uD2qz extends DHHdslt4SqYQ1hSj1a4Y
{
    public FVKjWjKo1YaG6p5uD2qz(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("android.telephony.SignalStrength", "getCdmaDbm");
        O000000o("android.telephony.SignalStrength", "getCdmaLevel");
        O000000o("android.telephony.SignalStrength", "getCdmaEcio");
        O000000o("android.telephony.SignalStrength", "getCdmaAsuLevel");
        O000000o("android.telephony.SignalStrength", "getEvdoDbm");
        O000000o("android.telephony.SignalStrength", "getEvdoEcio");
        O000000o("android.telephony.SignalStrength", "getEvdoSnr");
        O000000o("android.telephony.SignalStrength", "getEvdoLevel");
        O000000o("android.telephony.SignalStrength", "getEvdoAsuLevel");
        O000000o("android.telephony.SignalStrength", "getGsmBitErrorRate");
        O000000o("android.telephony.SignalStrength", "getGsmSignalStrength");
        O000000o("android.telephony.SignalStrength", "getGsmLevel");
        O000000o("android.telephony.SignalStrength", "getGsmAsuLevel");
        O000000o("android.telephony.SignalStrength", "getGsmDbm");
        O000000o("android.telephony.SignalStrength", "getAsuLevel");
        O000000o("android.telephony.SignalStrength", "getLteAsuLevel");
        O000000o("android.telephony.SignalStrength", "getLteLevel");
        O000000o("android.telephony.SignalStrength", "getLteDbm");
        O000000o("android.telephony.SignalStrength", "getLevel");
        O000000o("android.telephony.SignalStrength", "getDbm");
        O000000o("android.telephony.SignalStrength", "isGsm");
        O000000o("android.telephony.CellSignalStrengthCdma", "getCdmaDbm");
        O000000o("android.telephony.CellSignalStrengthCdma", "getCdmaEcio");
        O000000o("android.telephony.CellSignalStrengthCdma", "getEvdoDbm");
        O000000o("android.telephony.CellSignalStrengthCdma", "getEvdoEcio");
        O000000o("android.telephony.CellSignalStrengthCdma", "getEvdoSnr");
        O000000o("android.telephony.CellSignalStrengthCdma", "getAsuLevel");
        O000000o("android.telephony.CellSignalStrengthCdma", "getCdmaLevel");
        O000000o("android.telephony.CellSignalStrengthCdma", "getDbm");
        O000000o("android.telephony.CellSignalStrengthCdma", "getEvdoLevel");
        O000000o("android.telephony.CellSignalStrengthCdma", "getLevel");
        O000000o("android.telephony.CellSignalStrengthGsm", "getAsuLevel");
        O000000o("android.telephony.CellSignalStrengthGsm", "getDbm");
        O000000o("android.telephony.CellSignalStrengthGsm", "getLevel");
        O000000o("android.telephony.CellSignalStrengthLte", "getAsuLevel");
        O000000o("android.telephony.CellSignalStrengthLte", "getDbm");
        O000000o("android.telephony.CellSignalStrengthLte", "getLevel");
        O000000o("android.telephony.CellSignalStrengthWcdma", "getAsuLevel");
        O000000o("android.telephony.CellSignalStrengthWcdma", "getDbm");
        O000000o("android.telephony.CellSignalStrengthWcdma", "getLevel");
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        int i = 1;
        String str = paramMethodHookParam.method.getName();
        if ((str.equals("getCdmaDbm")) || (str.equals("getEvdoDbm")) || (str.equals("getCdmaEcio")) || (str.equals("getEvdoEcio")) || (str.equals("getGsmDbm")) || (str.equals("getLteDbm")) || (str.equals("getDbm")))
            paramMethodHookParam.setResult(Integer.valueOf(1 + (int)(-15.0D * Math.random())));
        if (str.equals("getEvdoSnr"))
            paramMethodHookParam.setResult(Integer.valueOf((int)(9.0D * Math.random())));
        if (str.equals("getGsmBitErrorRate"))
            paramMethodHookParam.setResult(Integer.valueOf((int)(7.0D * Math.random())));
        if (str.equals("getGsmSignalStrength"))
            paramMethodHookParam.setResult(Integer.valueOf((int)(31.0D * Math.random())));
        if ((str.equals("getCdmaLevel")) || (str.equals("getEvdoLevel")) || (str.equals("getLevel")) || (str.equals("getLteLevel")) || (str.equals("getGsmLevel")))
            paramMethodHookParam.setResult(Integer.valueOf(1 + (int)(3.0D * Math.random())));
        if ((str.equals("getAsuLevel")) || (str.equals("getLteAsuLevel")) || (str.equals("getCdmaAsuLevel")) || (str.equals("getGsmAsuLevel")) || (str.equals("getEvdoAsuLevel")))
            paramMethodHookParam.setResult(Integer.valueOf(1 + (int)(29.0D * Math.random())));
        /*if (str.equals("isGsm")){
            if (this.O00000o0.getPhoneType() != i)
            {
                int j = 0;
                paramMethodHookParam.setResult(Boolean.valueOf(i));
            }
        }*/

    }
}
