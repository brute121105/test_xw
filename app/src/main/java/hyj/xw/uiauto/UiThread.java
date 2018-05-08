package hyj.xw.uiauto;

/**
 * Created by Administrator on 2018/5/7.
 */
public class UiThread extends Thread {
    TestUi tu = new TestUi();

    @Override
    public void run() {
       while (true){
           //AutoUtil.sleep(2000);
           System.out.println("UiThread-->");
           tu.testDemo();
       }
    }
}
