package hyj.autooperation.test;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hyj.autooperation.common.FilePathCommon;
import hyj.autooperation.conf.WindowOperationConf;
import hyj.autooperation.model.NodeInfo;
import hyj.autooperation.model.WindowNodeInfo;
import hyj.autooperation.model.Wx008Data;
import hyj.autooperation.util.AutoUtil;
import hyj.autooperation.util.DragImageUtil2;
import hyj.autooperation.util.FileUtil;
import hyj.autooperation.util.LogUtil;
import hyj.autooperation.util.OkHttpUtil;

import static android.content.Context.CONNECTIVITY_SERVICE;


@RunWith(AndroidJUnit4.class)
public class HttpPostTest {
    String host = "http://192.168.1.5";
    @Before
    public void init(){

    }
    @Test
    public void test1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //login();
                upLoad();
            }
        }).start();
    }
    @Test
    public void test2(){
        String url =host+"/commons/pic-loc";
        OkHttpUtil.uploadMultiFile(url,"/sdcard","fangkuai.png");
    }

    public void upLoad(){
        String url =host+"/commons/pic-loc";
        System.out.println("OkHttpUtil url---->"+url);
        File file = new File("/sdcard/fangkuai.png");
        OkHttpUtil.upload(url,file);
    }

    public void login(){
        String url = "/user/login";
        String postBody = "{\"name\":\"admin\",\"password\":\"123456\"}";//json数据.
        System.out.println("OkHttpUtil postBody---->"+postBody);
        String res  = OkHttpUtil.okHttpPostBody(url,postBody);
        System.out.println("OkHttpUtil res---->"+res);
    }
}
