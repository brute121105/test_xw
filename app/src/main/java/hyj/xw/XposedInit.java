package hyj.xw;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.common.FilePathCommon;
import hyj.xw.hook.HideApp;
import hyj.xw.hook.Phone;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.hook.newHook.nsEo5EnMUO99qwl4QERu;
import hyj.xw.util.FileUtil;

/**
 * XposedInit
 *
 * @author wrbug
 * @since 2017/4/20
 */
public class XposedInit implements IXposedHookLoadPackage {
    private static String PACKAGE_NAME = "com.tencent.mm";
    private static Context wxContext ;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        final String packageName = lpparam.packageName;
        System.out.println("hyj xw hyj-->"+packageName);

       /* if("app.aptx.chamelemon".equals(packageName)){
            System.out.println("doAction method:"+packageName);
            *//*Class class1  = XposedHelpers.findClass("com.a.a.a.a.b",lpparam.classLoader);
            if(class1!=null){
                String arg = "GwYMQRYEGQILCQxHDAI=";
                String  str = (String) XposedHelpers.callStaticMethod(class1,"香港",arg);
                System.out.println("doAction arg:"+arg+" str:"+str);
                *//**//*XposedHelpers.findAndHookMethod(class1, "香港", String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        System.out.println("doAction args:"+param.args[0]+"--result:"+param.getResult());
                    }
                });*//**//*
            }*//*

            //hook新机 getDeviceJsonObj
            Class class1  = XposedHelpers.findClass("app.aptx.chamelemon.a",lpparam.classLoader);
            if(class1!=null){
                XposedHelpers.findAndHookMethod(class1, "记者", String.class,String.class,new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        String bslStr = param.getResult().toString();
                        if(bslStr.contains("android_id")){
                            System.out.println("doAction--->hook 变色龙改机数据bls str:"+bslStr);
                            FileUtil.writeContent2FileForceUtf8(FilePathCommon.bslStrTxt,bslStr);
                        }
                    }
                });
            }
        }*/


        if("hyj.xw".equals(packageName)){
          /*  new Phone(lpparam);
            System.out.println("versionCode-->hook hook");
            new HideApp(lpparam);*/
        }
        if("hyj.weixin_008".equals(packageName)){
            new Phone(lpparam);
        }

       // if(packageName.equals(PACKAGE_NAME)||"hyj.xw".equals(packageName)){
        if(packageName.equals(PACKAGE_NAME)){
             //aw hook开始
             String con = FileUtil.readAllUtf8(FilePathCommon.baseAppPathAW+FilePathCommon.npiFileName);
             //String con = FileUtil.readAllUtf8("/sdcard/A_hyj_json/a1/PhoneInfo.aw");
             //LogUtil.d("hookaw con",con);
             System.out.println("hookaw con-->"+con);
             NewPhoneInfo pi = JSON.parseObject(con,NewPhoneInfo.class);
             nsEo5EnMUO99qwl4QERu.O000000o(lpparam,pi);
             //aw hook结束

             //-------my hook 开始
            System.out.println("hyj context--->"+PACKAGE_NAME);
             new HideApp(lpparam);
             new Phone(lpparam);
            //--------my hook 结束

           //hook debug 开始
            // HookWxUtil.hoodWxid(lpparam);
          /* PackageHooker hooker = new PackageHooker(lpparam);
           try {
               hooker.hook();
           } catch (IOException e) {
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }*/
          //hook debug 结束

           //--test start
         /*Class c1 = XposedHelpers.findClass("android.app.ActivityThread", null);
           Context localContext1 = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(c1, "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
           try {
               Context wxContext = localContext1.createPackageContext(PACKAGE_NAME,Context.CONTEXT_IGNORE_SECURITY);
               PackageInfo pi = wxContext.getPackageManager().getPackageInfo(PACKAGE_NAME,0);
               System.out.println("pi---->"+ JSON.toJSONString(pi));
               AutoUtil.showToastByRunnable(localContext1,"pi-->"+pi.versionName);
           } catch (PackageManager.NameNotFoundException e) {
               e.printStackTrace();
           }*/

           //获取context方法2
          /* XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "attach", Context.class, new XC_MethodHook() {
               @Override
               protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                   super.afterHookedMethod(param);
                   wxContext = (Context)param.thisObject;
                   System.out.println("context =>" + wxContext.getFilesDir().getPath());
               }
           });
           if(wxContext!=null){
               System.out.println("hyj context--> wxContext666 =" + wxContext.getFilesDir().getPath());
           }



           XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
               @Override
               protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                   super.beforeHookedMethod(param);
                   Bundle bundle = (Bundle)param.args[0];
                   Intent intent = ((Activity)param.thisObject).getIntent();
                   String intentExtrStr ="";
                   if(intent!=null){
                       intentExtrStr = JSON.toJSONString(intent.getExtras());
                   }
                   System.out.println("Activity.onCreate =>" + param.thisObject.getClass()+"  intentExtrStr=>"+intentExtrStr+" bundle=>"+JSON.toJSONString(bundle));
               }
           });
           //hood数据库
            Class c2 = XposedHelpers.findClassIfExists("com.tencent.wcdb.database.SQLiteDatabase",lpparam.classLoader);
            Class c3 = XposedHelpers.findClassIfExists("com.tencent.wcdb.database.SQLiteDatabase.CursorFactory",lpparam.classLoader);
            Class c4 = XposedHelpers.findClassIfExists("com.tencent.wcdb.support.CancellationSignal",lpparam.classLoader);
           XposedHelpers.findAndHookMethod(c2
                   , "rawQueryWithFactory",c3
                   , String.class,String[].class, String.class,c4,new XC_MethodHook() {
               @Override
               protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                   super.beforeHookedMethod(param);
                   System.out.println("hyj DB => query sql = "+param.args[1]+", selectionArgs = "+JSON.toJSONString(param.args[2])+", db = "+param.thisObject);
               }
           });
           //追踪点击事件
           XposedHelpers.findAndHookMethod("android.view.View", lpparam.classLoader, "onTouchEvent", MotionEvent.class, new XC_MethodHook() {
               @Override
               protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                   super.afterHookedMethod(param);
                   System.out.println("hyj context--> iew.onTouchEvent => obj.class =" + param.thisObject.getClass());
                   System.out.println("hyj context--> iew.onTouchEvent wxContext666 =" + wxContext.getFilesDir().getPath());
               }
           });*/
           //----test end
        }

        //抓取朋友圈数据
       /* if(packageName.equals(PACKAGE_NAME)){
            HookWxUtil.hoodPyq(lpparam);
        }*/


         /* XposedHelpers.findAndHookMethod("hyj.weixin_008.MainActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Class c=lpparam.classLoader.loadClass("hyj.weixin_008.MainActivity");
                        Field field=c.getDeclaredField("wxPwd");
                        field.setAccessible(true);
                        //param.thisObject 为执行该方法的对象，在这里指MainActivity
                        EditText textView= (EditText) field.get(param.thisObject);
                        System.out.println("hyj-->"+textView.getText());
                    }
                });*/

        //追踪activiy
        /*XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "startActivity", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Intent intent = (Intent)param.args[0];
                String intentExtrStr ="";
                if(intent!=null){
                    intentExtrStr = JSON.toJSONString(intent.getExtras());
                }
                System.out.println("Activity.startActivit =>" + param.thisObject.getClass()+"  intentExtrStr=>"+intentExtrStr);
            }
        });*/
    }
}
