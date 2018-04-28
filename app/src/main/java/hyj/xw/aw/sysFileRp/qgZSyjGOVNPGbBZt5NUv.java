package hyj.xw.aw.sysFileRp;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import hyj.xw.aw.util.Edbw69C30UgVp2ocKByJ;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.util.StringUtilHyj;

/**
 * Created by Administrator on 2018/4/9.
 */

public class qgZSyjGOVNPGbBZt5NUv
{
    public static void O000000o(final Context context) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "pslist");
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> iterator = o00000Oo.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next()).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "pslist");
    }

    public static void O000000o(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "build.prop");
        final StringBuffer sb = new StringBuffer();
        for (String s : o00000Oo) {
            if (s.split("=").length >= 2) {
                final String s2 = s.split("=")[1];
                if (s.contains("dhcp.wlan0.dns1") || s.contains("dhcp.wlan0.gateway") || s.contains("dhcp.wlan0.server") || s.contains("net.dns1")) {
                    s = s.replaceAll(s2, "192.168.0.1");
                }
                else if (s.contains("dhcp.wlan0.ipaddress")) {
                    s = s.replaceAll(s2, phoneInfo.getIpAddress());
                }
                else if (s.contains("gsm.apn.sim.operator.numeric") || s.contains("gsm.sim.operator.numeric")) {
                    s = s.replaceAll(s2, phoneInfo.getSimOperator());
                }
                else if (s.contains("gsm.network.type")) {
                    s = s.replaceAll(s2, phoneInfo.getNetworkTypeName());
                }
                else if (s.contains("gsm.operator.orig.alpha")) {
                    s = s.replaceAll(s2, phoneInfo.getNetworkOperatorName());
                }
                else if (s.contains("gsm.sim.operator.alpha") || s.contains("gsm.operator.alpha") || s.contains("gsm.sim.operator.default-name")) {
                    s = s.replaceAll(s2, phoneInfo.getSimOperatorName());
                }
                else if (s.contains("gsm.sim.operator.imsi")) {
                    s = s.replaceAll(s2, phoneInfo.getSubscriberId());
                }
                else if (s.contains("gsm.version.baseband") || s.contains("ro.baseband") || s.contains("ro.boot.baseband")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildRadioVersion());
                }
                else if (s.contains("gsm.serial") || s.contains("ro.boot.serialno") || s.contains("ro.serialno")) {
                    s = s.replaceAll(s2, phoneInfo.getSerialno());
                }
                else if (s.contains("net.hostname")) {
                    s = s.replaceAll(s2, "android_" + StringUtilHyj.nullToString(phoneInfo.getBuildHost()));
                }
                else if (s.contains("persist.radio.cfu.iccid.1") || s.contains("persist.radio.data.iccid") || s.contains("persist.radio.ia") || s.contains("persist.radio.simswitch.iccid") || s.contains("ril.ia.iccid") || s.contains("ril.iccid.sim2") || s.contains("ril.iccid.sim2")) {
                    s = s.replaceAll(s2, phoneInfo.getSimSerialNumber());
                }
                else if (s.contains("persist.radio.imei")) {
                    s = s.replaceAll(s2, phoneInfo.getDeviceId());
                }
                else if (s.contains("persist.service.bdroid.bdaddr")) {
                    s = s.replaceAll(s2,phoneInfo.getMacAddress());
                }
                else if (s.contains("persist.sys.wifi_mac")) {
                    s = s.replaceAll(s2, StringUtilHyj.nullToString(phoneInfo.getBSSID()).replace(":", "").toLowerCase());
                }
                else if (s.contains("ro.build.display.id")) {
                    s = s.replaceAll(s2, phoneInfo.getDisplayId());
                }
                else if (s.contains("ro.build.description")) {
                    s = s.replaceAll(s2,  StringUtilHyj.nullToString(phoneInfo.getBuildDescription()));
                }
                else if (s.contains("ro.build.fingerprint")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildFingerprint());
                }
                else if (s.contains("ro.board.platform")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildHardware());
                }
                else if (s.contains("ro.build.host")) {
                    s = s.replaceAll(s2, StringUtilHyj.nullToString(phoneInfo.getBuildHost()));
                }
                else if (s.contains("ro.build.id")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildId());
                }
                else if (s.contains("ro.build.product")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildProduct());
                }
                else if (s.contains("ro.build.version.incremental")) {
                    s = s.replaceAll(s2, StringUtilHyj.nullToString(phoneInfo.getBuildIncremental()));
                }
                else if (s.contains("ro.build.version.release")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildRelease());
                }
                else if (s.contains("ro.build.version.sdk")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildSdk());
                }
                else if (s.contains("ro.hardware") || s.contains("ro.mediatek.platform") || s.contains("ro.mtk.hardware")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildHardware());
                }
                else if (s.contains("ro.product.board")) {
                    s = s.replaceAll(s2, StringUtilHyj.nullToString(phoneInfo.getBuildBoard()));
                }
                else if (s.contains("ro.product.brand")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildBrand());
                }
                else if (s.contains("ro.product.cpu.abi2")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildAbi2());
                }
                else if (s.contains("ro.product.cpu.abi")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildAbi());
                }
                else if (s.contains("ro.product.device")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildDevice());
                }
                else if (s.contains("ro.product.manufacturer")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildManufacturer());
                }
                else if (s.contains("ro.product.model") || s.contains("ro.product.name")) {
                    s = s.replaceAll(s2, phoneInfo.getBuildModel());
                }
                else if (s.contains("ro.sf.lcd_density")) {
                    s = s.replaceAll(s2, String.valueOf(phoneInfo.getDensityDpi()));
                }
                else if (s.contains("ro.opengles.version")) {
                    s = s.replaceAll(s2, String.valueOf(196500 + new Random().nextInt(109)));
                }
                else if (s.contains("ro.cip.build.date") || s.contains("ro.build.date")) {
                    s = s.replaceAll(s2, new SimpleDateFormat("yyyy年 MM月 dd日 E HH:mm:ss ", Locale.CHINA).format(new Date(phoneInfo.getBuildUtc())) + "CST");
                }
                else if (s.contains("ro.build.date.utc")) {
                    s = s.replaceAll(s2, String.valueOf(phoneInfo.getBuildUtc() / 1000L));
                }
                else if (s.contains("ro.runtime.firstboot")) {
                    s = s.replaceAll(s2, String.valueOf(System.currentTimeMillis() - new Random().nextInt(36000000)));
                }
                else if (s.contains("dalvik.vm.heapmaxfree") || s.contains("dalvik.vm.heapstartsize")) {
                    s = s.replaceAll(s2, String.valueOf(8 * (1 + new Random().nextInt(5))));
                }
                else if (s.contains("dalvik.vm.heapminfree")) {
                    s = s.replaceAll(s2, String.valueOf(2 * (1 + new Random().nextInt(5))));
                }
                else if (s.contains("dalvik.vm.heapsize")) {
                    s = s.replaceAll(s2, String.valueOf(128 * (1 + new Random().nextInt(4))));
                }
                else if (s.contains("dalvik.vm.heapgrowthlimit")) {
                    s = s.replaceAll(s2, String.valueOf(8 * (10 + new Random().nextInt(4))));
                }
            }
            sb.append(s).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "build.prop");
    }

    public static void O000000o(final NewPhoneInfo phoneInfo) {
        Edbw69C30UgVp2ocKByJ.O00000o0("Linux version 3.18.20-perf (android@buildserver) (gcc version 4.9.x-google 20140827 (prerelease) (GCC) ) #1 SMP PREEMPT Wed Jul 5 11:40:01 CST 2017".replace("3.18.20", "3.18." + new Random().nextInt(21)).replace("android@buildserver", phoneInfo.getBuildUser() + "@" + phoneInfo.getBuildHost()).replace("Wed Jul 5 11:40:01 CST 2017", new Date(phoneInfo.getBuildUtc()).toString()), "procversion");
    }

    public static void O00000Oo(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "procifinet6");
        final StringBuilder sb = new StringBuilder();
        for (String s : o00000Oo) {
            if (s.contains("wlan0")) {
                final String substring = s.substring(s.indexOf("fe80"), s.indexOf(" "));
                final String[] split = phoneInfo.getMacAddress().toLowerCase().split(":");
                System.out.println("tt s--->"+s);
                System.out.println("tt s split--->"+ JSON.toJSONString(split));
                s = s.replaceAll(substring, "fe80000000000000" + split[0] + split[1] + split[2] + "fffe" + split[3] + split[4] + split[5]);
            }
            else if (s.contains("p2p0")) {
                String substring2 = s.substring(s.indexOf("fe80"), s.indexOf(" "));
                String[] split2 = phoneInfo.getP2p0Mac().toLowerCase().split(":");
                if(split2==null||split2.length<5){
                    split2 = phoneInfo.getMacAddress().toLowerCase().split(":");
                }
                s = s.replaceAll(substring2, "fe80000000000000" + split2[0] + split2[1] + split2[2] + "fffe" + split2[3] + split2[4] + split2[5]);
            }
            sb.append(s).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "procifinet6");
    }

    public static void O00000Oo(final NewPhoneInfo phoneInfo) {
        Edbw69C30UgVp2ocKByJ.O00000o0("sched_enable_hmp=1 sched_enable_power_aware=1 console=ttyHSL0,115200,n8 androidboot.console=ttyHSL0 androidboot.hardware=qcom user_debug=31 msm_rtb.filter=0x237 ehci-hcd.park=3 lpm_levels.sleep_disabled=1 cma=32M@0-0xffffffff androidboot.bootdevice=624000.ufshc androidboot.verifiedbootstate=green androidboot.veritymode=enforcing androidboot.serialno=9068614060000 androidboot.authorized_kernel=true androidboot.baseband=msm mdss_mdp.panel=1:dsi:0:qcom,mdss_dsi_nt35597_wqxga_cmd:config0:1:qcom,mdss_dsi_nt35597_wqxga_cmd:config0:cfg:split_dsi".replaceAll("hardware=qcom", "hardware=" + phoneInfo.getBuildHardware()).replaceAll("9068614060000", phoneInfo.getSerialno()), "proccmdline");
    }

    public static void O00000o(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "appList");
        final ArrayList<v9d7UHGMLCSrB5nW5P5A> list = new ArrayList<v9d7UHGMLCSrB5nW5P5A>();
        final Iterator<String> iterator = o00000Oo.iterator();
        while (iterator.hasNext()) {
            final String[] split = iterator.next().split(",");
            final boolean booleanValue = Boolean.valueOf(split[3]);
            String buildRelease = split[2];
            if ("6.0.1".equals(buildRelease)) {
                buildRelease = phoneInfo.getBuildRelease();
            }
            if (new Random().nextBoolean()) {
                list.add(new v9d7UHGMLCSrB5nW5P5A(split[0], split[1], buildRelease, booleanValue));
            }
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(list.toString(), "appList");
    }

    public static void O00000o0(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "battery_uevent");
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> iterator = o00000Oo.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().replace("POWER_SUPPLY_CAPACITY=65", "POWER_SUPPLY_CAPACITY=" + phoneInfo.getBatteryLevel())).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "battery_uevent");
    }
}
