package hyj.xw.hook.newHook;

import android.text.TextUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2018/4/3.
 */

public class pU9KwKC2ppIRCaiFhaek extends DHHdslt4SqYQ1hSj1a4Y
{
    public pU9KwKC2ppIRCaiFhaek(final XC_LoadPackage.LoadPackageParam xc_LoadPackage$LoadPackageParam, final NewPhoneInfo phoneInfo) {
    super(xc_LoadPackage$LoadPackageParam, phoneInfo);
    O000000o(File.class.getName(), "renameTo", new Object[] { File.class.getName() });
    O000000o(File.class.getName(), new Object[] { String.class.getName() });
    O000000o(File.class.getName(), new Object[] { String.class.getName(), String.class.getName() });
    O000000o(File.class.getName(), new Object[] { File.class.getName(), String.class.getName() });
    O000000o(FileReader.class.getName(), new Object[] { String.class.getName() });
    O000000o(FileReader.class.getName(), new Object[] { File.class.getName() });
    O000000o(FileInputStream.class.getName(), new Object[] { String.class.getName() });
    this.O00000o0(ProcessBuilder.class.getName());
    O000000o(Runtime.class.getName(), "exec", new Object[] { String.class.getName() });
    O000000o(OutputStream.class.getName(), "write", new Object[] { byte[].class.getName() });
    O000000o(DataOutputStream.class.getName(), "writeBytes", new Object[] { String.class.getName() });
}

    private String O00000Oo(String replace) {
    final String string = PathFileUtil.str10 + File.separator;
    if (replace.contains("proc/cpuinfo")) {
        replace = replace.replace("proc/cpuinfo", string + "cpuinfo");
    }
    else {
        if (replace.contains("data/misc/wifi/p2p_supplicant.conf")) {
            return replace.replace("data/misc/wifi/p2p_supplicant.conf", string + "p2pconf");
        }
        if (replace.contains("data/misc/wifi/wpa_supplicant.conf")) {
            return replace.replace("data/misc/wifi/wpa_supplicant.conf", string + "wpaconf");
        }
        if (replace.contains("sys/devices/system/cpu/kernel_max")) {
            return replace.replace("sys/devices/system/cpu/kernel_max", string + "kernelMax");
        }
        if (replace.contains("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")) {
            return replace.replace("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", string + "cpuMaxfreq");
        }
        if (replace.contains("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq")) {
            return replace.replace("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq", string + "cpuCurfreq");
        }
        if (replace.contains("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq")) {
            return replace.replace("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq", string + "cpuMinfreq");
        }
        if (replace.contains("proc/net/if_inet6")) {
            return replace.replace("proc/net/if_inet6", string + "procifinet6");
        }
        if (replace.contains("proc/version")) {
            return replace.replace("proc/version", string + "procversion");
        }
        if (replace.contains("sys/class/net/wlan0/address")) {
            return replace.replace("sys/class/net/wlan0/address", string + "wlan0address");
        }
        if (replace.contains("sys/class/net/eth0/address")) {
            return replace.replace("sys/class/net/eth0/address", string + "wlan0address");
        }
        if (replace.contains("sys/class/net/em0/address")) {
            return replace.replace("sys/class/net/em0/address", string + "wlan0address");
        }
        if (replace.contains("sys/class/net/p2p0/address")) {
            return replace.replace("sys/class/net/p2p0/address", string + "P2p0Mac");
        }
        if (replace.contains("sys/class/net/sit0/address")) {
            return replace.replace("sys/class/net/sit0/address", string + "P2p0Mac");
        }
        if (replace.contains("system/build.prop")) {
            return replace.replace("system/build.prop", string + "build.prop");
        }
        if (replace.contains("proc/meminfo")) {
            return replace.replace("proc/meminfo", string + "meminfo");
        }
        if (replace.contains("sbin/su") || replace.contains("system/bin/su") || replace.contains("system/xbin/su") || replace.contains("data/local/xbin/su") || replace.contains("data/local/bin/su") || replace.contains("system/sd/xbin/su") || replace.contains("system/bin/failsafe/su") || replace.contains("data/local/su") || replace.contains("system/sbin/su") || replace.contains("vendor/bin/su") || replace.contains("system/sd/xbin/su") || replace.contains("system/app/Superuser.apk")) {
            return "notexist";
        }
        if (replace.contains("system/bin/app_process.orig") || replace.contains("data/app/de.robv.android.xposed.installer") || replace.contains("data/app/pro.burgerz.wsm.manager") || replace.contains("data/data/pro.burgerz.wsm.manager") || replace.contains("data/data/pro.burgerz.wsm.manager")) {
            return "notexist";
        }
        if (replace.contains("dev/socket/qemud") || replace.contains("dev/qemu_pipe") || replace.contains("system/bin/qemud") || replace.contains("system/lib/libc_malloc_debug_qemu.so") || replace.contains("sys/qemu_trace")) {
            return "notexist";
        }
        if (replace.contains("bluestacks")) {
            return "notexist";
        }
        if (replace.contains("proc/cmdline")) {
            return replace.replace("proc/cmdline", string + "proccmdline");
        }
        if (replace.contains("proc/net/arp")) {
            return replace.replace("proc/net/arp", string + "NetArp");
        }
        if (replace.contains("sys/class/power_supply/battery/uevent")) {
            return replace.replace("sys/class/power_supply/battery/uevent", string + "battery_uevent");
        }
    }
    return replace;
}

    public static String O000000o(final String s) {
    String s2;
    if (s.contains("proc/cpuinfo")) {
        s2 = "cpuinfo";
    }
    else {
        if (s.contains("data/misc/wifi/p2p_supplicant.conf")) {
            return "p2pconf";
        }
        if (s.contains("data/misc/wifi/wpa_supplicant.conf")) {
            return "wpaconf";
        }
        if (s.contains("sys/devices/system/cpu/kernel_max")) {
            return "kernelMax";
        }
        if (s.contains("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")) {
            return "cpuMaxfreq";
        }
        if (s.contains("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq")) {
            return "cpuCurfreq";
        }
        if (s.contains("sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq")) {
            return "cpuMinfreq";
        }
        if (s.contains("proc/net/if_inet6")) {
            return "procifinet6";
        }
        if (s.contains("proc/version")) {
            return "procversion";
        }
        if (s.contains("sys/class/net/wlan0/address") || s.contains("sys/class/net/eth0/address") || s.contains("sys/class/net/em0/address")) {
            return "wlan0address";
        }
        if (s.contains("sys/class/net/p2p0/address") || s.contains("sys/class/net/sit0/address")) {
            return "P2p0Mac";
        }
        if (s.contains("sys/class/net/") && s.contains("address")) {
                //\u6769\u6a3b\u75c5\u9286.\u93c0\u70ac\u589c(\u74a7\u8dfa\u63e9.\u6462(), "ranMac");
            return "ranMac";
        }
        if (s.contains("system/build.prop")) {
            return "build.prop";
        }
        if (s.contains("proc/meminfo")) {
            return "meminfo";
        }
        if (s.contains("sbin/su") || s.contains("system/bin/su") || s.contains("system/xbin/su") || s.contains("data/local/xbin/su") || s.contains("data/local/bin/su") || s.contains("system/sd/xbin/su") || s.contains("system/bin/failsafe/su") || s.contains("data/local/su") || s.contains("system/sbin/su") || s.contains("vendor/bin/su") || s.contains("system/sd/xbin/su") || s.contains("system/app/Superuser.apk")) {
            return "notexist";
        }
        if (s.contains("system/bin/app_process.orig") || s.contains("data/app/de.robv.android.xposed.installer") || s.contains("data/app/pro.burgerz.wsm.manager") || s.contains("data/data/pro.burgerz.wsm.manager") || s.contains("data/data/pro.burgerz.wsm.manager")) {
            return "notexist";
        }
        if (s.contains("dev/socket/qemud") || s.contains("dev/qemu_pipe") || s.contains("system/bin/qemud") || s.contains("system/lib/libc_malloc_debug_qemu.so") || s.contains("sys/qemu_trace")) {
            return "notexist";
        }
        if (s.contains("bluestacks")) {
            return "notexist";
        }
        if (s.contains("system/bin/qemu-props") || s.contains("system/bin/windroyed") || s.contains("system/bin/microvirtd") || s.contains("system/bin/nox-prop") || s.contains("system/bin/ttVM-prop") || s.contains("system/lib/libc_malloc_debug_qemu.so")) {
            return "notexist";
        }
        if (s.contains("proc/cmdline")) {
            return "proccmdline";
        }
        if (s.contains("proc/net/arp")) {
            return "NetArp";
        }
        final boolean contains = s.contains("sys/class/power_supply/battery/uevent");
        s2 = null;
        if (contains) {
            return "battery_uevent";
        }
    }
    return s2;
}

    @Override
    protected void afterHookedMethod(final XC_MethodHook.MethodHookParam xc_MethodHook$MethodHookParam) {
    }

    protected void beforeHookedMethod(final MethodHookParam xc_MethodHook$MethodHookParam) throws Throwable {
        super.beforeHookedMethod(xc_MethodHook$MethodHookParam);
        final String name = xc_MethodHook$MethodHookParam.method.getName();
        final String name2 = xc_MethodHook$MethodHookParam.method.getDeclaringClass().getName();
        if (File.class.getName().equals(name2) || FileReader.class.getName().equals(name2) || FileInputStream.class.getName().equals(name2)) {
            String s;
            if (xc_MethodHook$MethodHookParam.args.length == 2) {
                if (xc_MethodHook$MethodHookParam.args[0] instanceof File) {
                    s = String.valueOf(((File)xc_MethodHook$MethodHookParam.args[0]).getAbsolutePath()) + File.separator + xc_MethodHook$MethodHookParam.args[1];
                }
                else {
                    s = xc_MethodHook$MethodHookParam.args[0] + File.separator + xc_MethodHook$MethodHookParam.args[1];
                }
            }
            else if (xc_MethodHook$MethodHookParam.args[0] instanceof File) {
                s = ((File)xc_MethodHook$MethodHookParam.args[0]).getAbsolutePath();
            }
            else {
                s = (String)xc_MethodHook$MethodHookParam.args[0];
            }
            if (s.startsWith(PathFileUtil.str15) && !PathFileUtil.str15.equals(s) && !s.contains(".money")) {
                //\u9286.\u9518\u630e\u579c\u93c4\u5ea3\u6ae7\u6d5c\u55d0(this.O000000o, s);
            }
            else if (!s.contains(".money")) {
                final String str10 = PathFileUtil.str10;
                final String str11 = O000000o(s);
                if (!TextUtils.isEmpty((CharSequence)str11)) {
                    final File file = new File(str10, str11);
                    if (xc_MethodHook$MethodHookParam.args.length == 1) {
                        if (xc_MethodHook$MethodHookParam.args[0] instanceof File) {
                            xc_MethodHook$MethodHookParam.args[0] = file;
                            return;
                        }
                        xc_MethodHook$MethodHookParam.args[0] = file.getAbsolutePath();
                    }
                    else {
                        xc_MethodHook$MethodHookParam.args[1] = str11;
                        if (xc_MethodHook$MethodHookParam.args[0] instanceof File) {
                            xc_MethodHook$MethodHookParam.args[0] = new File(str10);
                            return;
                        }
                        xc_MethodHook$MethodHookParam.args[0] = str10;
                    }
                }
            }
        }
        else if (ProcessBuilder.class.getName().equals(name2)) {
            if (xc_MethodHook$MethodHookParam.args != null && xc_MethodHook$MethodHookParam.args[0] != null) {
                final String str13 = PathFileUtil.str13;
                if (xc_MethodHook$MethodHookParam.args[0] instanceof List) {
                    final List<String> list = (List<String>)xc_MethodHook$MethodHookParam.args[0];
                    final ArrayList<String> list2 = new ArrayList<String>();
                    for (String string : list) {
                        final String strd02 = O000000o(string);
                        if (!TextUtils.isEmpty((CharSequence)strd02)) {
                            string = str13 + File.separator + strd02;
                        }
                        list2.add(string);
                    }
                    xc_MethodHook$MethodHookParam.args[0] = list2;
                    return;
                }
                for (int i = 0; i < xc_MethodHook$MethodHookParam.args.length; ++i) {
                    final String strd023 = O000000o(String.valueOf(xc_MethodHook$MethodHookParam.args[i]));
                    if (!TextUtils.isEmpty((CharSequence)strd023)) {
                        xc_MethodHook$MethodHookParam.args[i] = str13 + File.separator + strd023;
                    }
                }
            }
        }
        else {
            if (Runtime.class.getName().equals(name2) && "exec".equals(name)) {
                String s2 = (String)xc_MethodHook$MethodHookParam.args[0];
                final String strd024 = this.O00000Oo(s2);
                if (!TextUtils.equals((CharSequence)s2, (CharSequence)strd024)) {
                    s2 = strd024;
                }
                xc_MethodHook$MethodHookParam.args[0] = s2;
                return;
            }
            if (OutputStream.class.getName().equals(name2) || DataOutputStream.class.getName().equals(name2)) {
                if (!(xc_MethodHook$MethodHookParam.args[0] instanceof byte[])) {
                    final String value = String.valueOf(xc_MethodHook$MethodHookParam.args[0]);
                    String strd025 = this.O00000Oo(value);
                    if (TextUtils.equals((CharSequence)value, (CharSequence)strd025)) {
                        strd025 = value;
                    }
                    xc_MethodHook$MethodHookParam.args[0] = strd025;
                    return;
                }
                final String s3 = new String((byte[])xc_MethodHook$MethodHookParam.args[0]);
                final String strd026 = this.O00000Oo(s3);
                if (!TextUtils.equals((CharSequence)s3, (CharSequence)strd026)) {
                    xc_MethodHook$MethodHookParam.args[0] = strd026.getBytes();
                }
            }
        }
    }
}
