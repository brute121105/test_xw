package hyj.xw;

import com.wx.wyyk.netty.client.NettyClient;

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
        System.out.println("doAction---conn");
        NettyClient client = new NettyClient("192.168.31.73", 8000, "A999");
        client.connect();
    }
}