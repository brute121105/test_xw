package hyj.xw;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Random;

import hyj.xw.util.AutoUtil;
import hyj.xw.util.FileUtil;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//adb shell am instrument -w -r   -e debug false -e class hyj.xw.ExampleInstrumentedTest#testClickZc hyj.xw.test/android.support.test.runner.AndroidJUnitRunner
    //      am instrument -w -r   -e debug true -e class penny.com.ui_auto.meituan.MeiTuanApp penny.com.ui_auto.test/android.support.test.runner.AndroidJUnitRunner";
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void testClickZc(){
        Context appContext = InstrumentationRegistry.getTargetContext();

        UiDevice mUIDevice =  UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        testScreenShot(mUIDevice);
    }



    @Test
    public void testuseAppContext() throws Exception {
        // Context of the app under test.

        System.out.println("test cace===");
    }

    public void testScreenShot(UiDevice mUIDevice) {
        //UiDevice mUIDevice =  UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if(mUIDevice!=null){
            while (true){
                System.out.println("开始截图1 imgName-->");
                AutoUtil.sleep(3000);
                String imgName = "hk"+ AutoUtil.getCurrentDate()+".png";
                FileUtil.writeContent2FileForceUtf8("/sdcard/azy/bba.txt","bb");
                String a = FileUtil.readAllUtf8("/sdcard/azy/bba.txt");
                System.out.println("a imgName-->"+a);
                String fileName = "/sdcard/"+imgName;
                File files = new File(fileName);
                mUIDevice.takeScreenshot(files);
                File files1 = new File(fileName);
                System.out.println("files1 imgName-->"+files.length());
                System.out.println("截图成功2 imgName-->"+imgName);
            }
        }
    }
}
