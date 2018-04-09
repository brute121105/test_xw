package hyj.xw.aw.sysFileRp;

import android.content.Context;

import java.util.List;
import java.util.Random;

import hyj.xw.aw.util.Edbw69C30UgVp2ocKByJ;
import hyj.xw.hook.newHook.NewPhoneInfo;

/**
 * Created by Administrator on 2018/4/9.
 */

public class GoicqOVehRY68RCP7lXb
{
    public static void O000000o(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "cpuinfo");
        final StringBuilder sb = new StringBuilder();
        for (String replace : o00000Oo) {
            if (replace.contains("Hardware")) {
                replace = replace.replace("MT6589T", phoneInfo.getCpuName());
            }
            sb.append(replace).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "cpuinfo");
        final String string = String.valueOf(1300 + new Random().nextInt(1000)) + "000";
        final String string2 = String.valueOf(300 + new Random().nextInt(200)) + "000";
        final String string3 = String.valueOf(600 + new Random().nextInt(200)) + "000";
        final int nextInt = new Random().nextInt(10);
        int n = 2;
        if (nextInt < 5) {
            n = 4;
        }
        else if (nextInt < 5 || nextInt >= 7) {
            n = 8;
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(string, "cpuMaxfreq");
        Edbw69C30UgVp2ocKByJ.O00000o0(string2, "cpuMinfreq");
        Edbw69C30UgVp2ocKByJ.O00000o0(string3, "cpuCurfreq");
        Edbw69C30UgVp2ocKByJ.O00000o0(String.valueOf(n), "kernelMax");
    }
}
