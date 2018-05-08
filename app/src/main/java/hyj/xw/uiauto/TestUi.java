package hyj.xw.uiauto;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiAutomatorTestCase;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Test;

import java.io.File;

/**
 * Created by Administrator on 2018/5/7.
 */

public class TestUi extends UiAutomatorTestCase {

    @Test
    public void testDemo(){
        UiDevice mUIDevice =  UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        System.out.println("mUIDevice---->"+mUIDevice);
        if(mUIDevice!=null){
            File files = new File("/sdcard/256.jpg");
            mUIDevice.takeScreenshot(files);
        }

        UiSelector uiselector = new UiSelector().text("注册");
        UiObject uiobject = new UiObject(uiselector);
        if(uiobject.exists()){
            try {
                uiobject.click();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
