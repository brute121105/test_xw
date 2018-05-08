package hyj.xw;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import hyj.xw.uiauto.UiThread;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        System.out.println("test cace===22");
        //TestUi testUi = new TestUi();
        //testUi.testDemo();
        new UiThread().start();
        System.out.println("test cace===");
    }
}
