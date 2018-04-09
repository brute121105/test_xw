package hyj.xw.aw.sysFileRp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hyj.xw.aw.util.Edbw69C30UgVp2ocKByJ;
import hyj.xw.hook.newHook.NewPhoneInfo;

/**
 * Created by Administrator on 2018/4/9.
 */

public class p3qsLWuGbzc0VpuSX7fl
{
    private static final String[] O000000o;
    private static final String[] O00000Oo;

    static {
        O000000o = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        O00000Oo = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
    }

    public static List<String> O000000o() {
        final int nextInt = new Random().nextInt(4);
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < nextInt + 5; ++i) {
            final StringBuffer sb = new StringBuffer();
            sb.append(O00000o()).append(",");
            sb.append(O00000oo().toLowerCase()).append(",");
            sb.append(O00000oO()).append(",");
            sb.append(O00000o0()).append(",");
            sb.append(O0000O0o()).append(",");
            sb.append(O00000Oo());
            list.add(sb.toString());
        }
        return list;
    }

    public static void O000000o(final Context context, final NewPhoneInfo phoneInfo) {
        final String lowerCase = phoneInfo.getP2p0Mac().substring(0, 5).replace(":", "").toLowerCase();
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "p2pconf");
        final StringBuffer sb = new StringBuffer();
        for (String replace : o00000Oo) {
            if (replace.contains("fca8")) {
                replace = replace.replace("fca8", lowerCase);
            }
            sb.append(replace).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "p2pconf");
    }

    public static void O000000o(final NewPhoneInfo phoneInfo) {
        final String format = String.format("192.168.31.1     0x1         0x2         %s     *        wlan0", phoneInfo.getBSSID());
        final StringBuffer sb = new StringBuffer("IP address       HW type     Flags       HW address            Mask     Device");
        sb.append("\n").append(format).append("\n");
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "NetArp");
    }

    public static long O00000Oo() {
        return 188416408L + Long.valueOf(new Random().nextInt(35461) + "" + new Random().nextInt(22180));
    }

    public static void O00000Oo(final Context context, final NewPhoneInfo phoneInfo) {
        final String buildManufacturer = phoneInfo.getBuildManufacturer();
        final String buildDevice = phoneInfo.getBuildDevice();
        final String buildModel = phoneInfo.getBuildModel();
        final String serialno = phoneInfo.getSerialno();
        final String ssid = phoneInfo.getSSID();
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "wpaconf");
        final StringBuffer sb = new StringBuffer();
        for (String s : o00000Oo) {
            if (s.contains("YUPHORIA")) {
                s = s.replaceAll("YUPHORIA", buildDevice);
            }
            else if (s.contains("manufacturer=YU")) {
                s = s.replaceAll("manufacturer=YU", "manufacturer=" + buildManufacturer);
            }
            else if (s.contains("YU5010")) {
                s = s.replaceAll("YU5010", buildModel);
            }
            else if (s.contains("2a21d3b")) {
                s = s.replaceAll("2a21d3b", serialno);
            }
            else if (s.contains("APP_NO1")) {
                s = s.replaceAll("APP_NO1", ssid);
            }
            sb.append(s).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "wpaconf");
    }

    public static String O00000o() {
        int n = 1;
        final String s = p3qsLWuGbzc0VpuSX7fl.O000000o[new Random().nextInt(p3qsLWuGbzc0VpuSX7fl.O000000o.length)];
        final String s2 = p3qsLWuGbzc0VpuSX7fl.O000000o[new Random().nextInt(p3qsLWuGbzc0VpuSX7fl.O000000o.length)];
        final String s3 = p3qsLWuGbzc0VpuSX7fl.O000000o[new Random().nextInt(p3qsLWuGbzc0VpuSX7fl.O000000o.length)];
        final String s4 = p3qsLWuGbzc0VpuSX7fl.O000000o[new Random().nextInt(p3qsLWuGbzc0VpuSX7fl.O000000o.length)];
        final String s5 = p3qsLWuGbzc0VpuSX7fl.O000000o[new Random().nextInt(p3qsLWuGbzc0VpuSX7fl.O000000o.length)];
        final String s6 = p3qsLWuGbzc0VpuSX7fl.O000000o[new Random().nextInt(p3qsLWuGbzc0VpuSX7fl.O000000o.length)];
        final int nextInt = new Random().nextInt(29000);
        int n2;
        if (nextInt >= 0) {
            n2 = n;
        }
        else {
            n2 = 0;
        }
        int n3;
        if (nextInt < 1000) {
            n3 = n;
        }
        else {
            n3 = 0;
        }
        if ((n3 & n2) != 0x0) {
            return "iTV-" + s + s2 + s3 + s4;
        }
        int n4;
        if (nextInt >= 1000) {
            n4 = n;
        }
        else {
            n4 = 0;
        }
        int n5;
        if (nextInt < 4000) {
            n5 = n;
        }
        else {
            n5 = 0;
        }
        if ((n5 & n4) != 0x0) {
            return "Tenda_" + s + s2 + s3 + s4 + s5 + s6;
        }
        int n6;
        if (nextInt >= 4000) {
            n6 = n;
        }
        else {
            n6 = 0;
        }
        int n7;
        if (nextInt < 7000) {
            n7 = n;
        }
        else {
            n7 = 0;
        }
        if ((n7 & n6) != 0x0) {
            return "TP-LINK_" + s + s2 + s3 + s4 + s5 + s6;
        }
        int n8;
        if (nextInt >= 7000) {
            n8 = n;
        }
        else {
            n8 = 0;
        }
        int n9;
        if (nextInt < 8000) {
            n9 = n;
        }
        else {
            n9 = 0;
        }
        if ((n9 & n8) != 0x0) {
            return "TP-LINK_" + s + s2 + s3 + s4;
        }
        int n10;
        if (nextInt >= 8000) {
            n10 = n;
        }
        else {
            n10 = 0;
        }
        int n11;
        if (nextInt < 12000) {
            n11 = n;
        }
        else {
            n11 = 0;
        }
        if ((n11 & n10) != 0x0) {
            return "PHICOMM_" + s + s2 + s3 + s4 + s5 + s6;
        }
        int n12;
        if (nextInt >= 12000) {
            n12 = n;
        }
        else {
            n12 = 0;
        }
        int n13;
        if (nextInt < 18000) {
            n13 = n;
        }
        else {
            n13 = 0;
        }
        if ((n13 & n12) != 0x0) {
            return "Netcore_" + s + s2 + s3 + s4 + s5 + s6;
        }
        int n14;
        if (nextInt >= 18000) {
            n14 = n;
        }
        else {
            n14 = 0;
        }
        int n15;
        if (nextInt < 23000) {
            n15 = n;
        }
        else {
            n15 = 0;
        }
        if ((n15 & n14) != 0x0) {
            return "MERCURY_" + s + s2 + s3 + s4 + s5 + s6;
        }
        int n16;
        if (nextInt >= 23000) {
            n16 = n;
        }
        else {
            n16 = 0;
        }
        int n17;
        if (nextInt < 24000) {
            n17 = n;
        }
        else {
            n17 = 0;
        }
        if ((n17 & n16) != 0x0) {
            return "MERCURY_" + s + s2 + s3 + s4;
        }
        int n18;
        if (nextInt >= 24000) {
            n18 = n;
        }
        else {
            n18 = 0;
        }
        int n19;
        if (nextInt < 27000) {
            n19 = n;
        }
        else {
            n19 = 0;
        }
        if ((n19 & n18) != 0x0) {
            return "FAST_" + s + s2 + s3 + s4 + s5 + s6;
        }
        int n20;
        if (nextInt >= 27000) {
            n20 = n;
        }
        else {
            n20 = 0;
        }
        int n21;
        if (nextInt < 28000) {
            n21 = n;
        }
        else {
            n21 = 0;
        }
        if ((n21 & n20) != 0x0) {
            return "FAST_" + s + s2 + s3 + s4;
        }
        int n22;
        if (nextInt >= 28000) {
            n22 = n;
        }
        else {
            n22 = 0;
        }
        if (nextInt >= 29000) {
            n = 0;
        }
        if ((n & n22) != 0x0) {
            return "ChainaNet_" + s + s2 + s3 + s4;
        }
        return "TP-LINK_0B3C";
    }

    public static void O00000o(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "netcfg");
        final StringBuffer sb = new StringBuffer();
        for (String replaceAll : o00000Oo) {
            if (replaceAll.contains("wlan0")) {
                final int n = -2 + replaceAll.indexOf(":");
                final String replaceAll2 = replaceAll.replaceAll(replaceAll.substring(n, n + 17), phoneInfo.getBSSID());
                replaceAll = replaceAll2.replaceAll(replaceAll2.substring(-3 + replaceAll2.indexOf("."), replaceAll2.indexOf("/")), phoneInfo.getIpAddress());
            }
            sb.append(replaceAll).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "netcfg");
        Edbw69C30UgVp2ocKByJ.O00000o0(phoneInfo.getBSSID(), "wlan0address");
        Edbw69C30UgVp2ocKByJ.O00000o0(phoneInfo.getP2p0Mac(), "P2p0Mac");
    }

    public static int O00000o0() {
        final int n = (int)(100.0 * Math.random());
        boolean b;
        if (n >= 1) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2 = false;
        if (n <= 80) {
            b2 = true;
        }
        if (b2 & b) {
            return -71 - (int)(25.0 * Math.random());
        }
        return -50 - (int)(20.0 * Math.random());
    }

    public static void O00000o0(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "ifconfig");
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < o00000Oo.size(); ++i) {
            String s = o00000Oo.get(i);
            if (i == 9 && s.contains("p2p0")) {
                final int n = 7 + s.indexOf("HWaddr");
                s = s.replaceAll(s.substring(n, n + 17), phoneInfo.getP2p0Mac().toUpperCase());
            }
            else if (i == 10 && s.contains("fe80")) {
                final String substring = s.substring(s.indexOf("fe80"), s.indexOf("/"));
                final String[] split = phoneInfo.getP2p0Mac().toLowerCase().split(":");
                s = s.replaceAll(substring, "fe80::" + split[0] + split[1] + ":" + split[2] + "ff:fe" + split[3] + ":" + split[4] + split[5]);
            }
            else if (i == 15 && s.contains("bytes")) {
                s = s.replaceAll(s.substring(1 + s.lastIndexOf(":"), -1 + s.lastIndexOf("(")), String.valueOf(50 + new Random().nextInt(500)));
            }
            else if (i == 17 && s.contains("wlan0")) {
                final int n2 = 7 + s.indexOf("HWaddr");
                s = s.replaceAll(s.substring(n2, n2 + 17), phoneInfo.getMacAddress().toUpperCase());
            }
            else if (i == 18 && s.contains("Bcast")) {
                final String ipAddress = phoneInfo.getIpAddress();
                final String replaceAll = s.replaceAll(s.substring(1 + s.indexOf(":"), -2 + s.indexOf("Bcast")), ipAddress);
                final String substring2 = replaceAll.substring(6 + replaceAll.indexOf("Bcast"), -2 + replaceAll.indexOf("Mask"));
                String s2;
                if (ipAddress.contains(".0.")) {
                    s2 = "192.168.0.255";
                }
                else {
                    s2 = "192.168.1.255";
                }
                s = replaceAll.replaceAll(substring2, s2);
            }
            else if (i == 19 && s.contains("fe80")) {
                final String substring3 = s.substring(s.indexOf("fe80"), s.indexOf("/"));
                final String[] split2 = phoneInfo.getMacAddress().toLowerCase().split(":");
                s = s.replaceAll(substring3, "fe80::" + split2[0] + split2[1] + ":" + split2[2] + "ff:fe" + split2[3] + ":" + split2[4] + split2[5]);
            }
            else if (i == 24 && s.contains("bytes")) {
                final String replaceAll2 = s.replaceAll(s.substring(1 + s.lastIndexOf(":"), -1 + s.lastIndexOf("(")), String.valueOf(1153434 + new Random().nextInt(100000)));
                s = replaceAll2.replaceAll(replaceAll2.substring(1 + replaceAll2.lastIndexOf(":"), -1 + replaceAll2.lastIndexOf("(")), String.valueOf(2306868 + new Random().nextInt(100000)));
            }
            sb.append(s).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "ifconfig");
    }

    public static String O00000oO() {
        int n = 1;
        final int nextInt = new Random().nextInt(1787);
        int n2;
        if (nextInt >= n) {
            n2 = n;
        }
        else {
            n2 = 0;
        }
        int n3;
        if (nextInt <= 312) {
            n3 = n;
        }
        else {
            n3 = 0;
        }
        if ((n3 & n2) != 0x0) {
            return "[WPA-PSK-CCMP][WPA2-PSK-CCMP][ESS]";
        }
        int n4;
        if (nextInt <= 855) {
            n4 = n;
        }
        else {
            n4 = 0;
        }
        int n5;
        if (nextInt > 312) {
            n5 = n;
        }
        else {
            n5 = 0;
        }
        if ((n5 & n4) != 0x0) {
            return "[WPA2-PSK-CCMP][WPS][ESS]";
        }
        int n6;
        if (nextInt <= 1367) {
            n6 = n;
        }
        else {
            n6 = 0;
        }
        int n7;
        if (nextInt > 855) {
            n7 = n;
        }
        else {
            n7 = 0;
        }
        if ((n7 & n6) != 0x0) {
            return "[WPA-PSK-CCMP][WPA2-PSK-CCMP][WPS][ESS]";
        }
        int n8;
        if (nextInt <= 1745) {
            n8 = n;
        }
        else {
            n8 = 0;
        }
        int n9;
        if (nextInt > 1367) {
            n9 = n;
        }
        else {
            n9 = 0;
        }
        if ((n9 & n8) != 0x0) {
            return "[WPA2-PSK-CCMP][ESS]";
        }
        int n10;
        if (nextInt <= 1751) {
            n10 = n;
        }
        else {
            n10 = 0;
        }
        int n11;
        if (nextInt > 1745) {
            n11 = n;
        }
        else {
            n11 = 0;
        }
        if ((n11 & n10) != 0x0) {
            return "[WPA-PSK-TKIP+CCMP][WPA2-PSK-TKIP+CCMP][WPS][ESS]";
        }
        int n12;
        if (nextInt <= 1754) {
            n12 = n;
        }
        else {
            n12 = 0;
        }
        int n13;
        if (nextInt > 1751) {
            n13 = n;
        }
        else {
            n13 = 0;
        }
        if ((n13 & n12) != 0x0) {
            return "[WPA-PSK-TKIP+CCMP][WPA2-PSK-TKIP+CCMP][ESS]";
        }
        int n14;
        if (nextInt <= 1769) {
            n14 = n;
        }
        else {
            n14 = 0;
        }
        int n15;
        if (nextInt > 1754) {
            n15 = n;
        }
        else {
            n15 = 0;
        }
        if ((n15 & n14) != 0x0) {
            return "[WPS][ESS]";
        }
        int n16;
        if (nextInt <= 1779) {
            n16 = n;
        }
        else {
            n16 = 0;
        }
        int n17;
        if (nextInt > 1769) {
            n17 = n;
        }
        else {
            n17 = 0;
        }
        if ((n17 & n16) != 0x0) {
            return "[WPA2-PSK-CCMP-preauth][ESS]";
        }
        int n18;
        if (nextInt > 1779) {
            n18 = n;
        }
        else {
            n18 = 0;
        }
        if (nextInt > 1785) {
            n = 0;
        }
        if ((n & n18) != 0x0) {
            return "[WPA2-PSK-TKIP+CCMP][WPS][ESS]";
        }
        return "[WPS][WEP][ESS]";
    }

    public static String O00000oo() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 2; ++j) {
                sb.append(p3qsLWuGbzc0VpuSX7fl.O000000o[(int)(16.0 * Math.random())]);
            }
            if (i != 5) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    public static int O0000O0o() {
        int n = 2447;
        int n2 = 1;
        final int nextInt = new Random().nextInt(17194);
        int n3;
        if (nextInt >= n2) {
            n3 = n2;
        }
        else {
            n3 = 0;
        }
        int n4;
        if (nextInt <= 4155) {
            n4 = n2;
        }
        else {
            n4 = 0;
        }
        if ((n4 & n3) != 0x0) {
            n = 2462;
        }
        else {
            int n5;
            if (nextInt <= 4435) {
                n5 = n2;
            }
            else {
                n5 = 0;
            }
            int n6;
            if (nextInt > 4155) {
                n6 = n2;
            }
            else {
                n6 = 0;
            }
            if ((n6 & n5) != 0x0) {
                return 2457;
            }
            int n7;
            if (nextInt <= 9220) {
                n7 = n2;
            }
            else {
                n7 = 0;
            }
            int n8;
            if (nextInt > 4435) {
                n8 = n2;
            }
            else {
                n8 = 0;
            }
            if ((n8 & n7) != 0x0) {
                return 2437;
            }
            int n9;
            if (nextInt <= 9833) {
                n9 = n2;
            }
            else {
                n9 = 0;
            }
            int n10;
            if (nextInt > 9220) {
                n10 = n2;
            }
            else {
                n10 = 0;
            }
            if ((n10 & n9) != 0x0) {
                return 2472;
            }
            int n11;
            if (nextInt <= 14715) {
                n11 = n2;
            }
            else {
                n11 = 0;
            }
            int n12;
            if (nextInt > 9833) {
                n12 = n2;
            }
            else {
                n12 = 0;
            }
            if ((n12 & n11) != 0x0) {
                return 2412;
            }
            int n13;
            if (nextInt <= 15009) {
                n13 = n2;
            }
            else {
                n13 = 0;
            }
            int n14;
            if (nextInt > 14715) {
                n14 = n2;
            }
            else {
                n14 = 0;
            }
            if ((n14 & n13) != 0x0) {
                return 2422;
            }
            int n15;
            if (nextInt <= 15397) {
                n15 = n2;
            }
            else {
                n15 = 0;
            }
            int n16;
            if (nextInt > 15009) {
                n16 = n2;
            }
            else {
                n16 = 0;
            }
            if ((n16 & n15) != 0x0) {
                return 2427;
            }
            int n17;
            if (nextInt <= 15760) {
                n17 = n2;
            }
            else {
                n17 = 0;
            }
            int n18;
            if (nextInt > 15397) {
                n18 = n2;
            }
            else {
                n18 = 0;
            }
            if ((n18 & n17) != 0x0) {
                return 2442;
            }
            int n19;
            if (nextInt <= 16167) {
                n19 = n2;
            }
            else {
                n19 = 0;
            }
            int n20;
            if (nextInt > 15760) {
                n20 = n2;
            }
            else {
                n20 = 0;
            }
            if ((n20 & n19) != 0x0) {
                return 2452;
            }
            int n21;
            if (nextInt <= 16422) {
                n21 = n2;
            }
            else {
                n21 = 0;
            }
            int n22;
            if (nextInt > 16167) {
                n22 = n2;
            }
            else {
                n22 = 0;
            }
            if ((n22 & n21) != 0x0) {
                return 2467;
            }
            int n23;
            if (nextInt <= 16550) {
                n23 = n2;
            }
            else {
                n23 = 0;
            }
            int n24;
            if (nextInt > 16422) {
                n24 = n2;
            }
            else {
                n24 = 0;
            }
            if ((n24 & n23) != 0x0) {
                return 2432;
            }
            int n25;
            if (nextInt <= 16872) {
                n25 = n2;
            }
            else {
                n25 = 0;
            }
            int n26;
            if (nextInt > 16550) {
                n26 = n2;
            }
            else {
                n26 = 0;
            }
            if ((n26 & n25) != 0x0) {
                return 2417;
            }
            int n27;
            if (nextInt > 16872) {
                n27 = n2;
            }
            else {
                n27 = 0;
            }
            if (nextInt >= 17194) {
                n2 = 0;
            }
            if ((n2 & n27) != 0x0) {
                return n;
            }
        }
        return n;
    }
}

