package hyj.xw.hook;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.NetworkInterface;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.model.PhoneInfo;
import hyj.xw.util.FileUtil;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class Phone {
    PhoneInfo phoneInfo;
    ClassLoader classLoader;
    public Phone(XC_LoadPackage.LoadPackageParam sharePkgParam) {
        classLoader = sharePkgParam.classLoader;
         String jsonStr = FileUtil.readAll("/sdcard/A_hyj_json/phone.txt");
         System.out.println("xposed--->"+jsonStr);
         phoneInfo = JSONObject.parseObject(jsonStr,PhoneInfo.class);
        //phoneInfo = createInfo();
        hookBuild();
        Telephony(sharePkgParam);
    }

    private PhoneInfo createInfo(){
        PhoneInfo phoneInfo = new PhoneInfo();
        phoneInfo.setAndroidId("cc307385e3bf7bcc");
        phoneInfo.setBrand("Coolpad");
        phoneInfo.setBuildId("fb7be2db9556");
        phoneInfo.setDevice("Coolpad 8720L");
        phoneInfo.setDeviceId("864093024001182");
        phoneInfo.setDisplay("51e3ce7be76c");
        phoneInfo.setFingerprint("samsung/ja3gzc/ja3g:4.4.2/KOT49H/I9500ZCUHNH4:user/release-keys");
        phoneInfo.setHardware("universal5410");
        phoneInfo.setLineNumber("8970359836");
        phoneInfo.setManufacturer("Coolpad");
        phoneInfo.setModel("Coolpad 8720L");
        phoneInfo.setNetworkCountryIso("cn");
        phoneInfo.setNetworkOperator("46007");
        phoneInfo.setNetworkOperatorName("中国移动");
        phoneInfo.setNetworkType(13);
        phoneInfo.setPhoneType(1);
        phoneInfo.setProductName("8720L");
        phoneInfo.setRelease("6.0");
        phoneInfo.setSdk("23");
        phoneInfo.setSerialno("b086ae09");
        phoneInfo.setSimCountryIso("cn");
        phoneInfo.setSimOperator("46007");
        phoneInfo.setSimOperatorName("中国移动");
        phoneInfo.setSimSerialNumber("89860256460528372004");
        phoneInfo.setSimState(1);
        phoneInfo.setSubscriberId("460075646052837");
        return phoneInfo;
    }






    public void Telephony(XC_LoadPackage.LoadPackageParam loadPkgParam) {

      /*  for (Method localMethod :TelephonyManager.class.getDeclaredMethods()){
            System.out.println("watch 1 TelephonyManager localMethod.getNam-->"+localMethod.getName());
            if (!Modifier.isAbstract(localMethod.getModifiers())){
                localMethod.setAccessible(true);
                XposedBridge.hookMethod(localMethod, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam paramMethodHookParam) throws Throwable {
                        String str1 = paramMethodHookParam.method.getName();
                        System.out.println("watch 2 TelephonyManager method--->android.os.Build methodName:" + str1);
                    }
                });
            }
        }*/

        String TelePhone = TelephonyManager.class.getName();

        HookTelephony(TelePhone,loadPkgParam, "getDeviceId",phoneInfo.getDeviceId());  //序列号
        //android_id
        try {
            XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", loadPkgParam.classLoader, "getString",ContentResolver.class, String.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                    System.out.println("watch0 method--->android.os.Build android.provider.Settings.Secure.getString");
                   if (param.args[1].equals(Settings.Secure.ANDROID_ID)) {
                        param.setResult(phoneInfo.getAndroidId());
                    }
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" Android ID 错误: " + ex.getMessage());
        }

        HookTelephony(TelePhone,loadPkgParam, "getLine1Number",phoneInfo.getLineNumber());//手机号码
        HookTelephony(TelePhone,loadPkgParam, "getSimSerialNumber",phoneInfo.getSimSerialNumber());//手机卡序列号
        HookTelephony(TelePhone,loadPkgParam, "getSubscriberId",phoneInfo.getSubscriberId());//IMSI

        HookTelephony(TelePhone,loadPkgParam, "getSimCountryIso",phoneInfo.getSimCountryIso());  //手机卡国家
        HookTelephony(TelePhone,loadPkgParam, "getSimOperator",phoneInfo.getSimOperator());//运营商
        HookTelephony(TelePhone,loadPkgParam, "getSimOperatorName",phoneInfo.getSimOperatorName());//运营商名字
        HookTelephony(TelePhone,loadPkgParam, "getNetworkCountryIso",phoneInfo.getSimCountryIso());//国家iso代码

        HookTelephony(TelePhone,loadPkgParam, "getNetworkOperator",phoneInfo.getNetworkOperator());  //网络运营商类型
        HookTelephony(TelePhone,loadPkgParam, "getNetworkOperatorName",phoneInfo.getNetworkOperatorName());//网络类型名
        HookTelephony(TelePhone,loadPkgParam, "getNetworkType",phoneInfo.getNetworkType());//网络类型
        HookTelephony(TelePhone,loadPkgParam, "getPhoneType",phoneInfo.getPhoneType());//手机类型
        HookTelephony(TelePhone,loadPkgParam, "getSimState",phoneInfo.getSimState());//手机卡状态

        HookTelephony(TelePhone,loadPkgParam, "getSimState",phoneInfo.getSimState());//手机卡状态

        HookTelephony("com.android.internal.telephony.PhoneSubInfo",loadPkgParam,"getDeviceId",phoneInfo.getDeviceId());
        HookTelephony("com.android.internal.telephony.PhoneSubInfo",loadPkgParam, "getImei",phoneInfo.getDeviceId());
        HookTelephony("com.android.internal.telephony.PhoneSubInfo",loadPkgParam, "getIccSerialNumber",phoneInfo.getSimSerialNumber());
        HookTelephony("com.android.internal.telephony.PhoneSubInfo",loadPkgParam,"getLine1Number",phoneInfo.getLineNumber());
        HookTelephony("com.android.internal.telephony.PhoneSubInfo",loadPkgParam,"getSubscriberId",phoneInfo.getSubscriberId());

        HookTelephony(NetworkInterface.class.getName(),loadPkgParam,"getName","ccmni0");//ccmni0流量卡 ppp0连vpn
        //HookTelephony(Locale.class.getName(),loadPkgParam, "getCountry","");//修改系统语言
        //HookTelephony(Locale.class.getName(),loadPkgParam, "getLanguage","");//修改系统语言



        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//mac地址
        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//无线路由器名称
        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//无线路由器地址

        try {

            hookBuild("ro.build.version.release",phoneInfo.getRelease());//系统版本 6.0.1
            hookBuild("ro.build.version.sdk",phoneInfo.getSdk());//系统版本值
            hookBuild("ro.product.brand",phoneInfo.getBrand());//品牌

            hookBuild("ro.product.model",phoneInfo.getModel());//型号
            hookBuild("ro.build.id",phoneInfo.getBuildId());//ID
            hookBuild("ro.build.display.id",phoneInfo.getDisplay());//display
            hookBuild("ro.product.name",phoneInfo.getProductName());//产品名
            hookBuild("ro.build.display.manufacturer",phoneInfo.getManufacturer());//制造商
            hookBuild("ro.build.display.device",phoneInfo.getDevice());//设备名
            hookBuild("ro.hardware",phoneInfo.getHardware());//硬件
            hookBuild("ro.build.fingerprint",phoneInfo.getFingerprint());//指纹
            hookBuild("ro.serialno",phoneInfo.getSerialno());//串口序列号；


        }catch (Exception e){
            XposedBridge.log(" BuilProp 错误: " + e.getMessage());
        }


        for (Method localMethod : XposedHelpers.findClass("android.os.Build", this.classLoader).getDeclaredMethods()){
            System.out.println("watch1 localMethod.getNam-->"+localMethod.getName());
            if ((localMethod.getName().equals("getString")) && (!Modifier.isAbstract(localMethod.getModifiers()))) {
                localMethod.setAccessible(true);
                XposedBridge.hookMethod(localMethod, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam paramMethodHookParam) throws Throwable {
                        String str1 = paramMethodHookParam.method.getName();
                        System.out.println("watch11 method--->android.os.Build methodName:"+str1);
                        if ("getString".equals(str1))
                        {
                            String str2 = (String)paramMethodHookParam.args[0];
                            if ("ro.product.model".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getModel());
                            }
                            else if ("ro.build.id".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getBuildId());
                            }
                            else if ("ro.build.display.id".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getDisplay());
                            }

                            else if ("ro.product.brand".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getBrand());
                            }
                            else if ("ro.product.device".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getDevice());
                            }

                            else if ("ro.product.name".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getProductName());
                            }
                            else if ("ro.product.manufacturer".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getManufacturer());
                            }
                            else if ("ro.hardware".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getHardware());
                            }
                            else if ("ro.serialno".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getSerialno());
                            }

                            else if ("ro.build.version.release".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getRelease());
                            }
                            else if ("ro.build.version.sdk".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getSdk());
                            }

                            else if ("ro.build.fingerprint".equals(str2))
                            {
                                paramMethodHookParam.setResult(phoneInfo.getFingerprint());
                            }

                        }
                    }
                });
            }
        }



        //不兼容redmiNode2 cm系统 ，暂时去掉
        HookTelephony("android.telephony.MSimTelephonyManager",loadPkgParam, "getDeviceId",phoneInfo.getDeviceId());
        HookTelephony("android.telephony.MSimTelephonyManager",loadPkgParam, "getSubscriberId",phoneInfo.getSubscriberId());

        //不兼容redmiNode2 cm系统 ，暂时去掉
       for (Method localMethod : XposedHelpers.findClass("android.telephony.MSimTelephonyManager", this.classLoader).getDeclaredMethods()){
            System.out.println("watch2 MSimTelephonyManager localMethod.getNam-->"+localMethod.getName());
            if (!Modifier.isAbstract(localMethod.getModifiers())){
                localMethod.setAccessible(true);
                XposedBridge.hookMethod(localMethod, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam paramMethodHookParam) throws Throwable {
                        String str1 = paramMethodHookParam.method.getName();
                        System.out.println("watch21 MSimTelephonyManager method--->android.os.Build methodName:" + str1);
                    }
                });
            }
        }


    }


    private void HookTelephony(String hookClass, XC_LoadPackage.LoadPackageParam loadPkgParam,
                               final String funcName, final String value) {
        try {
            XposedHelpers.findAndHookMethod(hookClass,
                    loadPkgParam.classLoader, funcName, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            System.out.println("watch31 method--->HookTelephony methodName:"+param.method.getName()+" clsName:"+param.thisObject.toString());
                            if("ccmni0".equals(value)){//处理链接vpn情况
                                String result = param.getResult().toString();
                                System.out.println("param.getResult-->"+result);
                                if("ppp0".equals(result)){
                                    param.setResult("ccmni0");
                                }
                            }else if("getLanguage".equals(funcName)){
                                System.out.println("param.getResult-->"+param.getResult().toString());
                                //param.setResult("en");
                                /*if(FileUtil.readContentToJsonTxt("isFeedStatus.txt").equals("0")){//如果是注册，修改为英文
                                    param.setResult("en");
                                }*/
                            }else if("getCountry".equals(funcName)){
                                if(FileUtil.readContentToJsonTxt("isFeedStatus.txt").equals("0")){//如果是注册，修改为英文
                                    param.setResult("US");
                                }
                            }
                            else {
                                param.setResult(value);
                            }

                        }

                    });
        } catch (Exception e) {

        }
    }
    private void HookTelephony(String hookClass, XC_LoadPackage.LoadPackageParam loadPkgParam,
                               String funcName, final int value) {
        try {
            XposedHelpers.findAndHookMethod(hookClass,
                    loadPkgParam.classLoader, funcName, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            System.out.println("watch32 method--->HookTelephony methodName:"+param.method.getName()+" clsName:"+param.thisObject.toString());
                            param.setResult(value);
                        }

                    });
        } catch (Exception e) {

        }
    }

    private void hookBuild(final String params1 ,final String value){

        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            if(cls != null){
                for (Member mem : cls.getDeclaredMethods()) {
                    XposedBridge.hookMethod(mem, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            if (param.args.length > 0 && param.args[0] != null && param.args[0].equals(params1)) {
                                System.out.println("watch4 method--->hookBuild methodName:"+param.method.getName());
                                param.setResult(value);
                            }
                        }
                    });
                }
            }

        } catch (ClassNotFoundException e) {
            XposedBridge.log(" DESCRIPTION 错误: " + e.getMessage());
        }

    }

    private void hookBuild()
    {
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE",phoneInfo.getRelease());
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK",phoneInfo.getSdk());
            XposedHelpers.setStaticObjectField(Build.class, "BRAND",phoneInfo.getBrand());
           XposedHelpers.setStaticObjectField(Build.class, "MODEL",phoneInfo.getModel());
          XposedHelpers.setStaticObjectField(Build.class, "ID",phoneInfo.getBuildId());
           XposedHelpers.setStaticObjectField(Build.class, "DISPLAY",phoneInfo.getDisplay());
            XposedHelpers.setStaticObjectField(Build.class, "PRODUCT",phoneInfo.getProductName());
            XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER",phoneInfo.getManufacturer());
            XposedHelpers.setStaticObjectField(Build.class, "DEVICE",phoneInfo.getDevice());
           XposedHelpers.setStaticObjectField(Build.class, "HARDWARE",phoneInfo.getHardware());
            XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT",phoneInfo.getFingerprint());
            XposedHelpers.setStaticObjectField(Build.class, "SERIAL",phoneInfo.getSerialno());
          XposedHelpers.setStaticObjectField(Build.class, "BOOTLOADER", "unkown");

    }


}