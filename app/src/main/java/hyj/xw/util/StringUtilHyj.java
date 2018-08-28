package hyj.xw.util;

import android.text.TextUtils;

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

    public static String nullObjectToString(Object obj){
        if(obj==null){
            return "";
        }else{
            String str = obj.toString();
            return nullToString(str);
        }
    }

    public static String nullToString(String str){
        if(str==null || str.equals("null") || str.equals("NULL")){
            return "";
        }else{
            return str;
        }
    }

    public static boolean isNumeric(String str){
            if(TextUtils.isEmpty(str)) return false;
            str = str.trim();
            for (int i = str.length();--i>=0;){
                   if (!Character.isDigit(str.charAt(i))){
                           return false;
                        }
                }
            return true;
         }

}
