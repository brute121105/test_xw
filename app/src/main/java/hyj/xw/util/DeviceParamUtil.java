package hyj.xw.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;

import hyj.xw.GlobalApplication;
import hyj.xw.model.DeviceInfo;

/**
 * Created by asus on 2017/12/11.
 */

public class DeviceParamUtil {

    public static DeviceInfo getDeviceInfo(){
        DeviceInfo info = new DeviceInfo();
        TelephonyManager tm = (TelephonyManager) GlobalApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        info.setDeviceId(tm.getDeviceId());
        info.setLine1Number(tm.getLine1Number());
        info.setSubscriberId(tm.getSubscriberId());
        info.setNetworkCountryIso(tm.getNetworkCountryIso());
        //tm.getDeviceSoftwareVersion();
        info.setNetworkOperator(tm.getNetworkOperator());
        info.setNetworkOperatorName(tm.getNetworkOperatorName());
        info.setNetworkType(String.valueOf(tm.getNetworkType()));
        info.setPhoneType(String.valueOf(tm.getPhoneType()));
        info.setSimCountryIso(tm.getSimCountryIso());
        info.setSimOperator(tm.getSimOperator());
        info.setSimOperatorName(tm.getSimOperatorName());
        info.setSimSerialNumber(tm.getSimSerialNumber());
        info.setSimState(String.valueOf(tm.getSimState()));
        System.out.println("deviceInfo--->"+ JSON.toJSONString(info));
        return info;
    }
}
