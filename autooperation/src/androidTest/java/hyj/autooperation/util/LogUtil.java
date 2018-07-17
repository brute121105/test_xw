package hyj.autooperation.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/15.
 */

public class LogUtil {
    //记录日志到sd卡
    public static void d(String tab,String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(new Date());
        String logMsg = dateTime+" "+tab+"--hyj---->"+msg;
        //以天为单位生成日志文件
        System.out.println(logMsg);
        FileUtil.writeContent2File("/sdcard/A_hyj_log/","log_"+dateTime.substring(0,13)+":00.txt",logMsg);
    }

    //记录登录账号sd卡
    public static void login(String tab,String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(new Date());
        String logMsg = dateTime+" "+tab+"---->"+msg;
        //以天为单位生成日志文件
        LogUtil.d("logMsg",logMsg);
        FileUtil.writeContent2File("/sdcard/A_hyj_login/","loginInfo_"+dateTime.substring(0,10)+".txt",logMsg);
    }
    //记录登录账号sd卡
    public static void reg(String tab,String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(new Date());
        String logMsg = dateTime+" "+tab+"---->"+msg;
        //以天为单位生成日志文件
        System.out.println(logMsg);
        FileUtil.writeContent2File("/sdcard/A_hyj_reg/","reg_"+dateTime.substring(0,10)+".txt",logMsg);
    }
    //记录008数据到sd卡
    public static void log008(String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(new Date());
        //以天为单位生成日志文件
        FileUtil.writeContent2File("/sdcard/A_hyj_008data/","008data.txt",msg);
    }
    //记录008数据到sd卡
    public static void export(String path,String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = sdf.format(new Date());
        String fileName = "bakData_"+dateTime+".txt";
        FileUtil.writeContent2File(path,fileName,msg);
    }

    //记录008数据到sd卡
    public static void logMyError(String path,String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = sdf.format(new Date());
        String fileName = "error_"+dateTime+".txt";
        FileUtil.writeContent2File(path,fileName,msg);
    }
    //记录008数据到sd卡
    public static void exportAWUtf8(String path,String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = sdf.format(new Date());
        String fileName = "aw_"+dateTime+".aw";
        FileUtil.writeContent2FileForceUtf8(path,fileName,msg);
    }
    //记录008数据到sd卡
    public static void exportAWUtf8ByPhoneName(String path,String msg,String phone){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = sdf.format(new Date());
        String fileName = "aw_"+phone+".aw";
        FileUtil.writeContent2FileForceUtf8(path,fileName,msg);
    }
    //记录008数据到sd卡
    public static void exportConfig(String path,String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = sdf.format(new Date());
        String fileName = "appconfig"+dateTime+".txt";
        FileUtil.writeContent2File(path,fileName,msg);
    }

    public static void logError(Exception e) {
        Writer writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        System.out.println("-->打印错误:"+writer.toString());
        dError("error",writer.toString().replaceAll("\n","|"));

    }
    public static void dError(String tab,String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(new Date());
        String logMsg = dateTime+" "+tab+"--hyj---->"+msg;
        //以天为单位生成日志文件
        System.out.println(logMsg);
        FileUtil.writeContent2File("/sdcard/A_hyj_crash/","log_"+dateTime.substring(0,17)+".txt",logMsg);
    }
}
