package hyj.xw.hook.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.util.FileUtil;

/**
 * Created by Administrator on 2018/1/10.
 */

public class HookWxUtil {

    public static  void hoodPyq(XC_LoadPackage.LoadPackageParam lpparam){

        XposedHelpers.findAndHookMethod("com.tencent.mm.a.f",lpparam.classLoader,
                "create",Object.class,
                new XC_MethodHook(){
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String str = JSON.toJSONString(param.args);
                        if(!str.contains("wxid_")&&!str.contains("chatroom")&&str.contains("@")){
                            System.out.println("hyj-->66【param.args："+str );
                            //new Thread(new SendDataThread(str)).start();
                        }
                    }
                });
    }

    public static  void hoodWxid(XC_LoadPackage.LoadPackageParam lpparam){

        XposedHelpers.findAndHookMethod("com.tencent.mm.a.f",lpparam.classLoader,
                "sizeOf",Object.class,Object.class,
                new XC_MethodHook(){
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String str = JSON.toJSONString(param.args);
                        if(param.args!=null&&param.args.length==2){
                            String args0 = param.args[0].toString();
                            if(args0.contains("wxid_")&&args0.length()==19){
                                String con = FileUtil.readAll("/sdcard/A_hyj_json/wxid.txt");
                                if(TextUtils.isEmpty(con)){
                                    FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","wxid.txt",args0);
                                }
                                System.out.println("hoodWxid wxid-->"+args0);
                            }
                        }
                    }
                });
    }
}
