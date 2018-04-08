package hyj.xw.aw.sysFileRp;


import android.content.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import hyj.xw.aw.util.ReadAssetsFileUtil;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.hook.newHook.PathFileUtil;
import hyj.xw.model.PhoneInfo;
import hyj.xw.util.FileUtil;

/**
 * Created by asus on 2018/4/8.
 */

public class GetpropRp {
    public static void O000000o(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = ReadAssetsFileUtil.readAssetFileList(context, "getprop");
        final StringBuilder sb = new StringBuilder();
        for (String s : o00000Oo) {
            if (s.split(":").length >= 2) {
                final String substring = s.substring(1 + s.indexOf(":"), s.length());
                final String substring2 = substring.substring(1 + substring.indexOf("["), substring.lastIndexOf("]"));
                if (s.contains("dhcp.wlan0.dns1") || s.contains("dhcp.wlan0.gateway") || s.contains("dhcp.wlan0.server") || s.contains("net.dns1")) {
                    s = s.replaceAll(substring2, "192.168.0.1");
                }
                else if (s.contains("dhcp.wlan0.ipaddress")) {
                    s = s.replaceAll(substring2, phoneInfo.getIpAddress());
                }
                else if (s.contains("gsm.apn.sim.operator.numeric") || s.contains("gsm.sim.operator.numeric")) {
                    s = s.replaceAll(substring2, phoneInfo.getSimOperator());
                }
                else if (s.contains("gsm.network.type")) {
                    s = s.replaceAll(substring2, phoneInfo.getNetworkTypeName());
                }
                else if (s.contains("gsm.operator.orig.alpha")) {
                    s = s.replaceAll(substring2, phoneInfo.getNetworkOperatorName());
                }
                else if (s.contains("gsm.sim.operator.alpha") || s.contains("gsm.operator.alpha") || s.contains("gsm.sim.operator.default-name")) {
                    s = s.replaceAll(substring2, phoneInfo.getSimOperatorName());
                }
                else if (s.contains("gsm.sim.operator.imsi")) {
                    s = s.replaceAll(substring2, phoneInfo.getSubscriberId());
                }
                else if (s.contains("gsm.version.baseband") || s.contains("ro.baseband") || s.contains("ro.boot.baseband")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildRadioVersion());
                }
                else if (s.contains("gsm.serial") || s.contains("ro.boot.serialno") || s.contains("ro.serialno")) {
                    s = s.replaceAll(substring2, phoneInfo.getSerialno());
                }
                else if (s.contains("net.hostname")) {
                    s = s.replaceAll(substring2, "android_" + phoneInfo.getBuildHost());
                }
                else if (s.contains("persist.radio.cfu.iccid.1") || s.contains("persist.radio.data.iccid") || s.contains("persist.radio.ia") || s.contains("persist.radio.simswitch.iccid") || s.contains("ril.ia.iccid") || s.contains("ril.iccid.sim2") || s.contains("ril.iccid.sim2")) {
                    s = s.replaceAll(substring2, phoneInfo.getSimSerialNumber());
                }
                else if (s.contains("persist.radio.imei")) {
                    s = s.replaceAll(substring2, phoneInfo.getDeviceId());
                }
                else if (s.contains("persist.service.bdroid.bdaddr")) {
                    s = s.replaceAll(substring2, phoneInfo.getMacAddress());
                }
                else if (s.contains("persist.sys.wifi_mac")) {
                    s = s.replaceAll(substring2, phoneInfo.getBSSID().replace(":", "").toLowerCase());
                }
                else if (s.contains("ro.build.display.id")) {
                    s = s.replaceAll(substring2, phoneInfo.getDisplayId());
                }
                else if (s.contains("ro.build.description")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildDescription());
                }
                else if (s.contains("ro.build.fingerprint")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildFingerprint());
                }
                else if (s.contains("ro.board.platform")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildHardware());
                }
                else if (s.contains("ro.build.host")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildHost());
                }
                else if (s.contains("ro.build.id")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildId());
                }
                else if (s.contains("ro.build.product")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildProduct());
                }
                else if (s.contains("ro.build.version.incremental")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildIncremental());
                }
                else if (s.contains("ro.build.version.release")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildRelease());
                }
                else if (s.contains("ro.build.version.sdk")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildSdk());
                }
                else if (s.contains("ro.hardware") || s.contains("ro.mediatek.platform") || s.contains("ro.mtk.hardware")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildHardware());
                }
                else if (s.contains("ro.product.board")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildBoard());
                }
                else if (s.contains("ro.product.brand")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildBrand());
                }
                else if (s.contains("ro.product.cpu.abi2")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildAbi2());
                }
                else if (s.contains("ro.product.cpu.abi")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildAbi());
                }
                else if (s.contains("ro.product.device")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildDevice());
                }
                else if (s.contains("ro.product.manufacturer")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildManufacturer());
                }
                else if (s.contains("ro.product.model") || s.contains("ro.product.name")) {
                    s = s.replaceAll(substring2, phoneInfo.getBuildModel());
                }
                else if (s.contains("ro.sf.lcd_density")) {
                    s = s.replaceAll(substring2, String.valueOf(phoneInfo.getDensityDpi()));
                }
                else if (s.contains("ro.opengles.version")) {
                    s = s.replaceAll(substring2, String.valueOf(196500 + new Random().nextInt(109)));
                }
                else if (s.contains("ro.cip.build.date") || s.contains("ro.build.date")) {
                    s = s.replaceAll(substring2, new SimpleDateFormat("yyyy年 MM月 dd日 E HH:mm:ss ", Locale.CHINA).format(new Date(phoneInfo.getBuildUtc())) + "CST");
                }
                else if (s.contains("ro.build.date.utc")) {
                    s = s.replaceAll(substring2, String.valueOf(phoneInfo.getBuildUtc() / 1000L));
                }
                else if (s.contains("ro.runtime.firstboot")) {
                    s = s.replaceAll(substring2, String.valueOf(System.currentTimeMillis() - new Random().nextInt(36000000)));
                }
                else if (s.contains("dalvik.vm.heapmaxfree") || s.contains("dalvik.vm.heapstartsize")) {
                    s = s.replaceAll(substring2, String.valueOf(8 * (1 + new Random().nextInt(5))));
                }
                else if (s.contains("dalvik.vm.heapminfree")) {
                    s = s.replaceAll(substring2, String.valueOf(2 * (1 + new Random().nextInt(5))));
                }
                else if (s.contains("dalvik.vm.heapsize")) {
                    s = s.replaceAll(substring2, String.valueOf(128 * (1 + new Random().nextInt(4))));
                }
                else if (s.contains("dalvik.vm.heapgrowthlimit")) {
                    s = s.replaceAll(substring2, String.valueOf(8 * (10 + new Random().nextInt(4))));
                }
            }
            sb.append(s).append("\n");
        }
        FileUtil.writeContent2File(PathFileUtil.str10+ File.separator,"getprop",sb.toString());
        //Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "getprop");
    }
}
