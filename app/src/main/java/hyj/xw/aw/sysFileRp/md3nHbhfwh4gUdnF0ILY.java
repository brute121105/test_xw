package hyj.xw.aw.sysFileRp;

import android.content.Context;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hyj.xw.aw.util.Edbw69C30UgVp2ocKByJ;
import hyj.xw.hook.newHook.NewPhoneInfo;

/**
 * Created by Administrator on 2018/4/9.
 */

public class md3nHbhfwh4gUdnF0ILY
{
    public static void O000000o(final Context context, final NewPhoneInfo phoneInfo) {
        final List<String> o00000Oo = Edbw69C30UgVp2ocKByJ.O00000Oo(context, "meminfo");
        final StringBuilder sb = new StringBuilder();
        for (String s : o00000Oo) {
            final Matcher matcher = Pattern.compile("[0-9]+").matcher(s);
            if (matcher.find()) {
                final String substring = s.substring(matcher.start(), matcher.end());
                if (!"0".equals(substring)) {
                    final int int1 = Integer.parseInt(substring);
                    if (s.contains("MemTotal")) {
                        s = s.replaceAll(substring, String.valueOf(phoneInfo.getMemTotal() / 1000L));
                    }
                    else if (s.contains("MemFree")) {
                        s = s.replaceAll(substring, String.valueOf(phoneInfo.getMemFree() / 1000L));
                    }
                    else if (s.contains("MemAvailable")) {
                        s = s.replaceAll(substring, String.valueOf(phoneInfo.getMemAvailable() / 1000L));
                    }
                    else {
                        s = s.replaceAll(substring, String.valueOf(new Random().nextInt(int1)));
                    }
                }
            }
            sb.append(s).append("\n");
        }
        Edbw69C30UgVp2ocKByJ.O00000o0(sb.toString(), "meminfo");
    }
}
