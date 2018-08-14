package hyj.xw;

import org.junit.Test;

import hyj.xw.util.ContactUtil;
import hyj.xw.util.DragImageUtil;
import hyj.xw.util.FileUtil;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String p = ContactUtil.createPersonName();
        int tel = ContactUtil.getRandomByQj(15,30);
        System.out.println("p-->"+p+" tel:"+tel);
    }
}