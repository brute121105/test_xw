package hyj.xw.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	

	/**
	 * 年月日时分秒
	 */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	

	public static String toStringHourseMinuteSecond (Timestamp stamp) {
		return toString(stamp, YYYY_MM_DD_HH_MM_SS);
	}
	public static Calendar toCalendar(String dateStr, String pattern)  {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		cal.clear();
		try {
			cal.setTime(format.parse(dateStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cal;
	}
	
	public static String toString(Calendar cal, String pattern)  {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(cal.getTime());
	}
	
	public static String toString(long ms, String pattern)  {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(ms);
	}
	public static String toString(long ms,String pattern,int num){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(ms+num*24*60*60*1000); 
	}
	public static String toString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);		
		return format.format(date);
	}


	
	public static Date toDateHMS (String dateFormatStr) {
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		try {
			return format.parse(dateFormatStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String toTime(Timestamp stamp) {
		return toString(stamp, YYYY_MM_DD_HH_MM_SS);
	}
	
	public static String toTime(Date dt) {
		return toString(dt, YYYY_MM_DD_HH_MM_SS);
	}
	
	public static String toString(Date date, String pattern) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		return toString(cal, pattern);
	}
	
	public static String toString(Timestamp stamp, String pattern) {
		return toString((Date) stamp, pattern);
	}
	
	public static Timestamp toTimestamp(String str, String pattern) {
		Calendar cal = toCalendar(str, pattern);
		Timestamp stamp = new Timestamp(cal.getTimeInMillis());
		return stamp;
	}
	
	public static Calendar copy(Calendar cal) {
		Calendar c = Calendar.getInstance();
		c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
		return c;
	}
	
	/**
	 * 计算c2 - c1 之间间隔的天数
	 * @param c1 起始日期
	 * @param c2 终止日期
	 * @return
	 */
	public static double daysBetween(Calendar c1, Calendar c2) {
		long start = c1.getTimeInMillis();
		long end = c2.getTimeInMillis();
		return (end - start) / (24 * 60 * 60 * 1000d);
	}
	

	
	/**
	 * 保留年月日
	 * 时分秒和毫秒置0
	 * @param cal
	 */
	public static void retainYYMMDD(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * 按给定的格式获取当前时间：字符串
	 * @param pattern
	 * @return
	 */
	public static String getCurrentTimeAsStr(String pattern){
		Calendar c = Calendar.getInstance();
		return new SimpleDateFormat(pattern).format(c.getTimeInMillis());
	}
	
	/**
	 * 
	* @methodName:getAge
	* @Description: 根据出生日期获取年龄，只计算到月份
	* @param birthDate
	* @return
	* @author linjg
	* @date 2016年5月6日
	 */
	public static int getAge(Date birthDate) {
		if (birthDate == null)
			throw new RuntimeException("出生日期不能为null");

		int age = 0;

		Date now = new Date();

		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");

		String birth_year = format_y.format(birthDate);
		String this_year = format_y.format(now);

		String birth_month = format_M.format(birthDate);
		String this_month = format_M.format(now);

		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0)
			age -= 1;
		if (age < 0)
			age = 0;
		return age;
	}
	
	/**
	* @methodName:getMinDate
	* @Description: TODO 获取最小的日期
	* @param date1
	* @param date2
	* @return
	* @author wei.wang1
	* @date 2016-5-19
	 */
	public static Date getMinDate(Date date1,Date date2){
		Calendar c1= Calendar.getInstance();   
		c1.setTime(date1); 
		Calendar c2= Calendar.getInstance();   
		c2.setTime(date2);
		int diff=c1.compareTo(c2);
		if(diff>0){
			return date1;
		}else{
			return date2;
		}
	}
}
