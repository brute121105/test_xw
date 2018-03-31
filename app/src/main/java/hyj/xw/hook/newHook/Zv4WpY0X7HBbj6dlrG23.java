package hyj.xw.hook.newHook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class Zv4WpY0X7HBbj6dlrG23 extends DHHdslt4SqYQ1hSj1a4Y
{
    public Zv4WpY0X7HBbj6dlrG23(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("android.telephony.CellIdentityCdma", "getSystemId");
        O000000o("android.telephony.CellIdentityCdma", "getNetworkId");
        O000000o("android.telephony.CellIdentityCdma", "getLongitude");
        O000000o("android.telephony.CellIdentityCdma", "getLatitude");
        O000000o("android.telephony.CellIdentityCdma", "getBaseStationId");
        O000000o("android.telephony.CellIdentityGsm", "getLac");
        O000000o("android.telephony.CellIdentityGsm", "getCid");
        O000000o("android.telephony.CellIdentityGsm", "getMcc");
        O000000o("android.telephony.CellIdentityGsm", "getMnc");
        O000000o("android.telephony.CellIdentityLte", "getTac");
        O000000o("android.telephony.CellIdentityLte", "getCi");
        O000000o("android.telephony.CellIdentityLte", "getMcc");
        O000000o("android.telephony.CellIdentityLte", "getMnc");
        O000000o("android.telephony.CellIdentityWcdma", "getLac");
        O000000o("android.telephony.CellIdentityWcdma", "getCid");
        O000000o("android.telephony.CellIdentityWcdma", "getMcc");
        O000000o("android.telephony.CellIdentityWcdma", "getMnc");
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        paramMethodHookParam.method.getDeclaringClass().getName();
        String str = paramMethodHookParam.method.getName();
        if ("getSystemId".equals(str))
            paramMethodHookParam.setResult(this.O00000o0.getMnc());

        if ("getNetworkId".equals(str))
        {
            paramMethodHookParam.setResult(Integer.valueOf(this.O00000o0.getLac()));
            return;
        }
        if ("getLongitude".equals(str))
        {
            paramMethodHookParam.setResult(Double.valueOf(this.O00000o0.getLongitude()));
            return;
        }
        if ("getLatitude".equals(str))
        {
            paramMethodHookParam.setResult(Double.valueOf(this.O00000o0.getLatitude()));
            return;
        }
        if ("getBaseStationId".equals(str))
        {
            paramMethodHookParam.setResult(Integer.valueOf(this.O00000o0.getCi()));
            return;
        }
        if (("getLac".equals(str)) || ("getTac".equals(str)))
        {
            paramMethodHookParam.setResult(Integer.valueOf(this.O00000o0.getLac()));
            return;
        }
        if (("getCid".equals(str)) || ("getCi".equals(str)))
        {
            paramMethodHookParam.setResult(Integer.valueOf(this.O00000o0.getCi()));
            return;
        }
        if ("getMcc".equals(str))
        {
            paramMethodHookParam.setResult(Integer.valueOf(460));
            return;
        }
        if (!"getMnc".equals(str));
            paramMethodHookParam.setResult(this.O00000o0.getMnc());
    }
}
