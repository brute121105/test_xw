package hyj.xw;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.widget.Toast;

import org.litepal.LitePalApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by Administrator on 2017/5/18.
 */

public class GlobalApplication extends Application {
    private static Context context;
    private static ContentResolver resolver;
    private Toast O0000O0o = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context =getApplicationContext();
        //context = revokeContext();
        //context = getContext1();
        resolver = getContentResolver();
        //程序崩溃错误捕捉
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);
        LitePalApplication.initialize(context);
        //EventBus.getDefault().register(this);
    }

    public static Context getContext() {
        return context;
    }
    public static ContentResolver getResolver(){
        return resolver;
    }
    /*public static Context getContext1()
    {
        return ActivityThread.currentApplication();
    }*/


   /* //EventBus.getDefault().post("ddd");
    @Subscribe(threadMode = ThreadMode.MAIN) //表示消息接收函数运行在ui线程，即可以直接操作界面显示
    public void onMessageEvent(String msg) {
        System.out.println("onMessageEvent--->"+msg);
       // Toast.makeText(getContext(),"from fragment: "+msg, Toast.LENGTH_SHORT).show();
        O000000o(this, msg);
    }

    public void O000000o(Context paramContext, String paramString)
    {
        if (this.O0000O0o == null){
            this.O0000O0o = Toast.makeText(paramContext, paramString, Toast.LENGTH_SHORT);
            this.O0000O0o.show();
        }

    }*/

    public Context revokeContext(){
        Class<?> clazz= null;
        try {
            clazz = Class.forName("android.app.ActivityThread");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method method= null;
        try {
            method = clazz.getDeclaredMethod("currentApplication");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Application application= null;
        try {
            application = (Application) method.invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return application.getApplicationContext();
    }

}
