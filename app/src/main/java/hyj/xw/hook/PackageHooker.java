package hyj.xw.hook;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.hook.util.HookUtil;
import hyj.xw.util.RegUtil;

/**
 * Created by Administrator on 2017/11/13.
 */

public class PackageHooker {

    List<ClazzAttribute> clsAtts = new ArrayList<ClazzAttribute>();

    private final XC_LoadPackage.LoadPackageParam loadPackageParam;


    public PackageHooker(XC_LoadPackage.LoadPackageParam param) {
        loadPackageParam = param;
       /* try {
            try {
                hook();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {


        }*/
    }


    public List<ClazzAttribute> hook() throws IOException, ClassNotFoundException {
        System.out.println("--hyj--hook--");
        DexFile dexFile = new DexFile(loadPackageParam.appInfo.sourceDir);
        System.out.println("hyj-->"+JSON.toJSONString(dexFile));
        Enumeration<String> classNames = dexFile.entries();
        while (classNames.hasMoreElements()) {
            String className = classNames.nextElement();
            if(className.equals("com.tencent.mm.a.f")){
           //if(className.indexOf("com.tencent.mm.sdk.platformtools")>-1){
           //if(className.indexOf("com.tencent.mm.storage")>-1){
                if (isClassNameValid(className)) {
                    ClazzAttribute clsAttr = new ClazzAttribute();
                    clsAtts.add(clsAttr);
                    clsAttr.setClsName(className);
                    final Class clazz = Class.forName(className, false, loadPackageParam.classLoader);
                    dumpClass(clazz,clsAttr);
                }

            }

        }
        //XposedBridge.log("hyj--clsAtts:"+JSON.toJSONString(clsAtts));
        /*for(ClazzAttribute attr:clsAtts){
            XposedBridge.log("hyj--clsAtts:"+JSON.toJSONString(attr));
        }*/
        //dumpMethod();
        return clsAtts;
    }


    public void log(Object str) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        XposedBridge.log("[" + df.format(new Date()) + "]:  "
                + str.toString());
    }

    public boolean isClassNameValid(String className) {
        return className.startsWith(loadPackageParam.packageName)
                && !className.contains("$")
                && !className.contains("BuildConfig")
                && !className.equals(loadPackageParam.packageName + ".R");
    }

    private void dumpClass(Class actions,ClazzAttribute clsAttr) {

        //XposedBridge.log("hyj--Methods");
        Method[] m = actions.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {
            XposedBridge.log("hyj-->【"+m[i].toString()+"】 getName【"+m[i].getName()+"】 getDeclaringClass【"+m[i].getDeclaringClass()+"】");
            clsAttr.getMethodName().add(m[i].toString());
            String classNameStr = m[i].getDeclaringClass().toString();
            String clsName = classNameStr.substring(classNameStr.indexOf(" ")+1);
            XposedBridge.log("hyj-->【"+clsName);
            myAfterHookMethod1(m[i].toString(),clsName,m[i].getName());
        }
        //XposedBridge.log("hyj--Fields");
        Field[] f = actions.getDeclaredFields();
        for (int j = 0; j < f.length; j++) {
            //XposedBridge.log("hyj--"+f[j].toString());
            //dumpStaticField(f[j].toString());
            clsAttr.getFieldName().add(f[j].toString());
        }
        //XposedBridge.log("hyj--Classes");
        Class[] c = actions.getDeclaredClasses();
        for (int k = 0; k < c.length; k++) {
            //XposedBridge.log("hyj--"+c[k].toString());
            clsAttr.getClsdName().add(c[k].toString());
        }
    }

    private String dumpStaticField(String fieldStr){
        String fieldValue = "";
        if(fieldStr!=null&&fieldStr.indexOf("static")>-1){
            String[] strs1 = fieldStr.split("\\s");
            if(strs1!=null&&strs1.length>1){
                String str = strs1[strs1.length-1];
                if(str.indexOf(".")>-1){
                    String fieldType = str.substring(0,str.lastIndexOf("."));
                    String fieldName = str.substring(str.lastIndexOf(".")+1);
                    Object obj =  XposedHelpers.getStaticObjectField(XposedHelpers.findClassIfExists(fieldType,loadPackageParam.classLoader),fieldName);
                    fieldValue = JSON.toJSONString(obj);
                    XposedBridge.log("hyj--fieldType:"+fieldType+"fieldName:"+fieldName+" value:"+JSON.toJSONString(obj));
                }
            }
        }
        return fieldValue;
    }

    private void dumpMethod(){
        for(final ClazzAttribute ca:clsAtts){

            if(ca.getClsName().indexOf("abstract")>-1) continue;

            //XposedBridge.log("hyj--debug clsName1:"+ca.getClsName());
            //输出静态属性值
            for(String fieldName:ca.getFieldName()){
                dumpStaticField(fieldName);
            }

            //遍历方法名称，hook方法
            for(String methodName:ca.getMethodName()){
               //if(methodName.indexOf("static")>-1) continue;
                myAfterHookMethod(methodName,ca);
            }


        }
    }

   public   void myAfterHookMethod(String methodName,final ClazzAttribute ca){
       Object[] args = createHookArgs(methodName,ca);
       if(args==null||methodName.indexOf("abstract")>-1) return;
       String realMethodName = RegUtil.regString(methodName,"\\.([^\\(\\.]+)\\(",1);
       //XposedBridge.log("hyj--debug realMethodName:"+ca.getClsName()+"."+realMethodName+" args:"+JSON.toJSONString(args));
       XposedHelpers.findAndHookMethod(ca.getClsName(), loadPackageParam.classLoader,realMethodName,args);
   }

    private Object[] createHookArgs(String methodStr,final ClazzAttribute ca){
        XC_MethodHook xm = createCallBack(ca,methodStr);
        Object[] args = null;
        String methodArgs = methodStr.substring(methodStr.lastIndexOf("(")+1,methodStr.lastIndexOf(")"));
        if(methodArgs.length()>0){
            if(methodArgs.indexOf(",")>-1){
                String[] argStr = methodArgs.split(",");
                List<Object> objs = new ArrayList<Object>();
                for(String arg:argStr){
                    objs.add(XposedHelpers.findClass(arg,loadPackageParam.classLoader));
                }
                objs.add(xm);
                args = (Object[])objs.toArray();
            }else{
                List<Object> objs = new ArrayList<Object>();
                objs.add(XposedHelpers.findClass(methodArgs,loadPackageParam.classLoader));
                objs.add(xm);
                args = (Object[])objs.toArray();
              // if(methodArgs.indexOf("android.os.Bundle")>-1){
                // args = new Object[]{ XposedHelpers.findClass(methodArgs,loadPackageParam.classLoader),xm};
                   //args = new Object[]{Bundle.class,xm};
              // }
            }
        }else {
            args = new Object[]{xm};
        }
        return args;
    }
    //创建回调参数
    private XC_MethodHook createCallBack(final ClazzAttribute ca,final String methodStr){
        XC_MethodHook xm = new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                System.out.println("hyj-->【进入XC_MethodHook】 claName;"+ca.getClsName()+" methodStr:"+methodStr);
                System.out.println("hyj-->【"+JSON.toJSONString(param.args));
                //hook方法后，输出类全局变量值
                for(String fieldName:ca.getFieldName()){
                    //XposedBridge.log("hyj--debug fieldName:"+fieldName);
                    if(fieldName.indexOf(".")==-1) continue;
                    Object obj= HookUtil.getFieldObjByFieldName(ca.getClsName(),fieldName.substring(fieldName.lastIndexOf(".")+1),loadPackageParam,param);
                    String text = getTextByObj(obj);
                    if(!"".equals(text)){
                        XposedBridge.log("hyj--【属性值】className-->"+ca.getClsName()+" field-->"+fieldName+" text-->"+text);
                    }
                }
                //hook方法后，输出类返回值
                if(methodStr.indexOf("void")>-1) return;
                Object obj = param.getResult();
                try {
                    XposedBridge.log("hyj--【方法返回值】methodStr-->"+ca.getClsName()+"."+methodStr+" resultValue-->"+JSON.toJSONString(obj));
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        };
        return xm;
    }

    public   void myAfterHookMethod1(String methodStr,String clsName,String methodName){
        Object[] args = createHookArgs1(methodStr,clsName,methodName);
        if(args==null||methodName.indexOf("abstract")>-1) return;
        XposedHelpers.findAndHookMethod(clsName, loadPackageParam.classLoader,methodName,args);
    }

    //创建回调参数1
    private XC_MethodHook createCallBack1(final String clsName,final String methodName,final String methodStr){
        XC_MethodHook xm = new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                System.out.println("hyj-->【开始进入XC_MethodHook】 claName【"+clsName+"】 methodName【"+methodName+"】 methodStr:"+methodStr);
                //hook方法后，输出类全局变量值
                //hook方法后，输出类返回值
                try {
                    System.out.println("hyj-->【param.args："+JSON.toJSONString(param.args));
                    Object obj = param.getResult();
                    XposedBridge.log("hyj-->【返回值param.getResult-->"+JSON.toJSONString(obj));
                }catch (JSONException e){
                    e.printStackTrace();
                }
                System.out.println("hyj-->【结束进入XC_MethodHook】 claName【"+clsName+"】 methodName【"+methodName+"】");

            }
        };
        return xm;
    }
    private Object[] createHookArgs1(String methodStr,String clsName,String methodName){
        XC_MethodHook xm = createCallBack1(clsName,methodName,methodStr);
        Object[] args = null;
        String methodArgs = methodStr.substring(methodStr.lastIndexOf("(")+1,methodStr.lastIndexOf(")"));
        if(methodArgs.length()>0){
            if(methodArgs.indexOf(",")>-1){
                String[] argStr = methodArgs.split(",");
                List<Object> objs = new ArrayList<Object>();
                for(String arg:argStr){
                    objs.add(XposedHelpers.findClass(arg,loadPackageParam.classLoader));
                }
                objs.add(xm);
                args = (Object[])objs.toArray();
            }else{
                List<Object> objs = new ArrayList<Object>();
                objs.add(XposedHelpers.findClass(methodArgs,loadPackageParam.classLoader));
                objs.add(xm);
                args = (Object[])objs.toArray();
            }
        }else {
            args = new Object[]{xm};
        }
        return args;
    }
    private String getTextByObj(Object obj){
        String text = "";
        if(obj==null) return "";
        if(obj instanceof EditText){
            text = ((EditText)obj).getText()+"";
        }else if(obj instanceof TextView){
            text = ((TextView)obj).getText()+"";
        }else if(obj instanceof CheckBox){
            text = ((CheckBox)obj).getText()+"";
        }else {
            text = JSON.toJSONString(obj);
        }
        if(text.equals("")||"null".equals(text)||text.replaceAll("\\d|-","").equals("")||"true".equals(text)||"false".equals(text)||"[]".equals(text)||"{}".equals(text))
            return "";
        return text;
    }
}
//输出普通属性值
          /*  if(ca.getClsName().indexOf("MainActivity")>-1){
                final List<String> fieldNams = ca.getFieldName();
                XposedBridge.log("hyj--debug fieldNams:"+fieldNams);
                myAfterHookMethod(ca);

               *//* XposedHelpers.findAndHookMethod("hyj.weixin_008.MainActivity", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        for(String fieldName:fieldNams){
                            XposedBridge.log("hyj--debug fieldName:"+fieldName);
                            if(fieldName.indexOf(".")==-1) continue;
                            Object obj= HookUtil.getFieldObjByFieldName(ca.getClsName(),fieldName.substring(fieldName.lastIndexOf(".")+1),loadPackageParam,param);
                            String text = getTextByObj(obj);
                            XposedBridge.log("hyj--field text:"+text);
                        }
                    }
                });*//*
            }*/

                  /* private  void myAfterHookMethod(final ClazzAttribute ca){
        XposedBridge.log("hyj--debug myAfterHookMethod:"+ca.getClsName());
        XposedHelpers.findAndHookMethod(ca.getClsName(), loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                for(String fieldName:ca.getFieldName()){
                    //XposedBridge.log("hyj--debug fieldName:"+fieldName);
                    if(fieldName.indexOf(".")==-1) continue;
                    Object obj= HookUtil.getFieldObjByFieldName(ca.getClsName(),fieldName.substring(fieldName.lastIndexOf(".")+1),loadPackageParam,param);
                    String text = getTextByObj(obj);
                    XposedBridge.log("hyj--field text:"+text);
                }
            }
        });
    }*/

                    /* for (Method method: clazz.getDeclaredMethods()) {
                    if (!Modifier.isAbstract(method.getModifiers())) {
                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                                log("hyj--HOOKED: " + clazz.getName() + " methodName-->" + param.method.getName());
                            }
                        });
                    }
                }*/