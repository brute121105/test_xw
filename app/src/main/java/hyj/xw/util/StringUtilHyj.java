package hyj.xw.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2018/2/3.
 */

public class StringUtilHyj {

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
    public static String getIp(String str){
        String result = "";
        String reg = "(?<=<code>)(.+?)(?=</code>)";
        Matcher m = createMatcher(str, reg);
        while (m.find()){
            result = result+m.group();
        }
        return result;
    }
}
