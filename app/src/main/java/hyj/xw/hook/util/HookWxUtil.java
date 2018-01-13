package hyj.xw.hook.util;

import com.alibaba.fastjson.JSON;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

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
}
