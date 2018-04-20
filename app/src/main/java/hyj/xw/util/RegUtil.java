package hyj.xw.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/17.
 */

public class RegUtil {

    public static Matcher createMatcher(String matchStr, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(matchStr);
        return m;
    }

    public static String regString(String str, String reg, int groupNum) {
        String resultString = "";
        Matcher m = createMatcher(str, reg);
        if (m.find()) {
            resultString = m.group(groupNum);

        }
        return resultString;
    }
}
