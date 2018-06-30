package hyj.autooperation;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hyj.autooperation.common.FilePathCommon;
import hyj.autooperation.conf.WindowOperationConf;
import hyj.autooperation.model.NodeInfo;
import hyj.autooperation.model.WindowNodeInfo;
import hyj.autooperation.thread.TemplateThread;
import hyj.autooperation.util.AutoUtil;
import hyj.autooperation.util.DragImageUtil2;
import hyj.autooperation.util.FileUtil;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private UiDevice mDevice;
    private Context appContext;

    @Before
    public void init(){
        appContext = InstrumentationRegistry.getTargetContext();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public void initAuto(){
        killAndClearWxData();
        startWx();
    }

    @Test
    public void testAuto(){
        initAuto();
        Map<String,WindowNodeInfo> ops = WindowOperationConf.getOperations();
        while (true){
            String windowText = getAllWindowText();
            System.out.println("running-->getAllWindowText："+windowText);
            WindowNodeInfo wni = getWniByWindowText(ops,windowText);
            if(wni==null){
                System.out.println("windowText没有匹配ops动作");
                continue;
            }
            System.out.println("running-->wni："+JSON.toJSONString(wni));
            if(wni!=null){
                doAction(wni);
                continue;
            }
        }
    }

    //自定义、通用两种
    public void doAction(WindowNodeInfo wni){
        if(wni.getOperation().contains("自定义-")){
            doCustomerAction(wni);
        }else  if(wni.getOperation().contains("异常-")){
            initAuto();
        }else {
            for(NodeInfo nodeInfo: wni.getNodeInfoList()){
                switch (nodeInfo.getNodeType()){
                    case 1:
                        nodeInfo.setOperationSucc(doClick(nodeInfo));
                        AutoUtil.sleep(nodeInfo.getNodeOperationSleepMs());
                        break;
                    case 2:
                        break;
                }
            }
            wni.setWindowOperatonSucc(validIsAllTrue(wni));//设置总成功标志
        }
        System.out.println("doAction-->"+wni.toString());
    }
    public boolean validIsAllTrue(WindowNodeInfo wni){
        if(wni.getNodeInfoList().size()==0) return false;
        for(NodeInfo nodeInfo:wni.getNodeInfoList()){
            if(!nodeInfo.isOperationSucc()){
                return false;
            }
        }
        return true;
    }
    public void doCustomerAction(WindowNodeInfo wni){
        String operationDesc="";
        boolean isOperationsSucc = false;
        if("自定义-点击注册2".equals(wni.getOperation())){
            List<UiObject2> uos = findNodesByClaZZ(EditText.class);
            if(uos!=null&&uos.size()==3){
                uos.get(0).setText("1123");
                uos.get(1).setText("136521598"+new Random().nextInt(10)+new Random().nextInt(10));
                uos.get(2).setText("789lkjmnhikj");//密码
                isOperationsSucc = clickUiObjectByText("注册");
                operationDesc = "输入账号，输入密码，点击【注册】"+isOperationsSucc;
            }
        }else if("自定义-过滑块".equals(wni.getOperation())){
            int dragEndX=0;
            while (dragEndX==0){
                cmdScrrenShot();//截图
                Bitmap bi = waitAndGetBitmap();
                dragEndX = DragImageUtil2.getPic2LocX(bi);
                operationDesc="拖动dragEndX"+dragEndX;
                Point[] points = getDargPoins(235,dragEndX+63,1029);
                mDevice.swipe(points,100);
            }
        }
        wni.setWindowOperationDesc(operationDesc);
        wni.setWindowOperatonSucc(isOperationsSucc);
    }

    public boolean doClick(NodeInfo nodeInfo){
        if(!TextUtils.isEmpty(nodeInfo.getNodeText())){
            if(nodeInfo.getNodeText().contains("%")){
                //clickUiObjectByText(nodeInfo.getNodeText());
            }
            return clickUiObjectByText(nodeInfo.getNodeText());
        }else if(!TextUtils.isEmpty(nodeInfo.getNodeDesc())){
            if(nodeInfo.getNodeDesc().contains("%")){
                return clickNodeByDesContain(nodeInfo.getNodeDesc().replaceAll("%",""));
            }
            return clickUiObjectByDesc(nodeInfo.getNodeDesc());
        }
        return false;
    }

    public WindowNodeInfo getWniByWindowText(Map<String,WindowNodeInfo>  ops,String windowText){
        for(String key:ops.keySet()){
            if(key.contains("|")){
                String[] arr = key.split("\\|");
                boolean flag = true;
                for(String str :arr){
                    if(!windowText.contains(str)){
                        flag = false;
                        break;
                    }
                }
                if(flag) return ops.get(key);
                continue;
            }else if(windowText.contains(key)){
                return ops.get(key);
            }
        }
        return null;
    }

    @Test
    public void testGetContent() throws Exception {
        while (true){
            getAllWindowText();
        }
    }
    //获取当前窗口的所有文本，耗时200毫秒
    public String getAllWindowText(){
        String windowContent = "";
        try {
            List<UiObject2> edtObjs = mDevice.findObjects(By.clazz(EditText.class));
            List<UiObject2> trObjs = mDevice.findObjects(By.clazz(TextView.class));
            List<UiObject2> viewObjs = mDevice.findObjects(By.clazz(View.class));
            List<UiObject2> cbObjs = mDevice.findObjects(By.clazz(CheckBox.class));
            List<UiObject2> btnObjs = mDevice.findObjects(By.clazz(Button.class));
            if(!cbObjs.isEmpty()){
                for(UiObject2 obj:cbObjs){
                    windowContent = windowContent +getNotNullComponentText(obj.getText(),obj.getContentDescription());
                }
            }
            if(!btnObjs.isEmpty()){
                for(UiObject2 obj:btnObjs){
                    windowContent = windowContent +getNotNullComponentText(obj.getText(),obj.getContentDescription());
                }
            }
            if(!viewObjs.isEmpty()){
                for(UiObject2 obj:viewObjs){
                    windowContent = windowContent +getNotNullComponentText(obj.getText(),obj.getContentDescription());
                }
            }
            if(!edtObjs.isEmpty()){
                for(UiObject2 obj:edtObjs){
                    windowContent = windowContent +getNotNullComponentText(obj.getText(),obj.getContentDescription());
                }
            }
            if(!trObjs.isEmpty()){
                for(UiObject2 obj:trObjs){
                    windowContent = windowContent +getNotNullComponentText(obj.getText(),obj.getContentDescription());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return windowContent;
    }
    //获取非空文本
    public String getNotNullComponentText(String text,String desc){
        String result = "";
        if(!TextUtils.isEmpty(text)) result = result+text+"|";
        if(!TextUtils.isEmpty(desc)) result = result+desc+"|";
        return result;
    }
    @Test
    public void useAppContext1() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        System.out.println("getPackageName-->"+appContext.getPackageName());
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        while (true){
            Thread.sleep(1000);
            System.out.println("test runing-->"+System.currentTimeMillis());

            UiSelector uiselector2 = new UiSelector().text("注册");
            UiObject uiobject2 = new UiObject(uiselector2);
            if(uiobject2.exists()){
                try {
                    boolean flag = uiobject2.click();
                    AutoUtil.sleep(1000);
                    boolean flagBack = mDevice.pressBack();
                    System.out.println("test runing-->clickFlag:"+flag+" backFlag:"+flagBack);
                    String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,"1");
                    File file = waitAndGetFile(path);
                    if(file!=null){
                        Bitmap bi = BitmapFactory.decodeFile(path+"/"+file.getName());
                        System.out.println("fileSize runing-->"+file.length()+" fileName:"+file.getName()+" width:"+bi.getWidth()+" height:"+bi.getHeight());
                        System.out.println("delete runing-->"+file.delete());

                        File file0 = waitAndGetFile(path);
                        if(file0!=null){
                            System.out.println("fileSize0 runing-->"+file0.length()+" fileName:"+file0.getName());
                        }else {
                            System.out.println("fileSize0 runing-->null");
                        }
                    }
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private File waitAndGetFile(String path){
        File picFile = null;
        File[] files = new File(path).listFiles();
        if(files!=null&&files.length>0){
            picFile = files[files.length-1];
        }
        return picFile;
    }
    private File delAllFiles(String path){
        File picFile = null;
        File[] files = new File(path).listFiles();
        for(File f:files){
            f.delete();
        }
        return picFile;
    }

    public void testScreenShot(UiDevice mUIDevice) {
        //UiDevice mUIDevice =  UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if(mUIDevice!=null){
            while (true){
                System.out.println("版本1 开始截图1 imgName-->");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                File file3s = new File("/sdcard/azy/aa.txt");
                try {
                    file3s.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String imgName = "16666new1"+System.currentTimeMillis()+".png";
                File files = new File("/sdcard/azy/"+imgName);
                mUIDevice.takeScreenshot(files);
                System.out.println("截图成功2 imgName-->"+imgName);
            }
        }
    }

    String action="";
    @Test
    public void useAppContext() {

        killAndClearWxData();
        startWx();

        while (true){
            System.out.println("running...."+mDevice.getCurrentPackageName()+" traver:"+mDevice.getLastTraversedText()+" pdName:"+mDevice.getProductName()+" action:"+action);
            boolean flag1 = clickUiObjectByText("注册");
            System.out.println("running点击注册1："+flag1);
            List<UiObject2> uos = findNodesByClaZZ(EditText.class);
            if(uos!=null&&uos.size()==3){
                uos.get(0).setText("1123");
                uos.get(1).setText("136521598"+new Random().nextInt(10)+new Random().nextInt(10));
                uos.get(2).setText("789lkjmnhikj");//密码
                boolean clickFlag2 = clickUiObjectByText("注册");
                System.out.println("running点击注册2："+clickFlag2);
            }
            boolean clickFlag3 = clickNodeByDesContain("我已阅读并同意上述条款");
            boolean clickFlag4 = clickUiObjectByDesc("下一步");
            System.out.println("running点击同意条款下一步："+clickFlag4);
            boolean clickFlag5 = clickUiObjectByDesc("开始");
            System.out.println("running点击开始安全验证："+clickFlag5);

            UiObject2  tdText = mDevice.findObject(By.desc("拖动下方滑块完成拼图"));
            if(tdText!=null){
                int dragEndX=0;
                while (dragEndX==0){
                    System.out.println("running-->tdText:"+tdText.getContentDescription());
                    cmdScrrenShot();//截图
                    Bitmap bi = waitAndGetBitmap();
                    dragEndX = DragImageUtil2.getPic2LocX(bi);
                    System.out.println("running-->dragEndX:"+dragEndX);
                    Point[] points = getDargPoins(235,dragEndX+63,1029);
                    mDevice.swipe(points,100);
                }
            }

            UiObject2 qrWindow = mDevice.findObject(By.descContains("联系符合以下条件的"));
            if(qrWindow!=null){
                killAndClearWxData();
                startWx();
            }
        }
    }

    public Bitmap waitAndGetBitmap(){
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        File file = waitAndGetFile(path);
        Bitmap bi = null;
        while (file==null||bi==null){
            AutoUtil.sleep(1000);
            file = waitAndGetFile(path);
            if(file!=null){
                String pngPath = path+"/"+file.getName();
                System.out.println("running-->pngPath:"+pngPath);
                bi = BitmapFactory.decodeFile(pngPath);
            }
            System.out.println("running-->等待图片生成:");
        }
        return bi;
    }

    public Point[] getDargPoins(int startX,int endY,int locY){
        Point point0 = new Point();
        Point point1 = new Point();
        Point point2 = new Point();
        point0.set(startX,locY);
        point1.set(endY-70,locY);
        point2.set(endY,locY);
        Point[] points = {point0,point1,point2};
        return points;
    }

    public void startWx(){
        System.out.println("doAction-->启动微信");
        startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
        AutoUtil.sleep(800);
    }

    public  void startAppByPackName(String packageName,String activity){
        Intent intent = new Intent();
        ComponentName cmp=new ComponentName(packageName,activity);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        System.out.println("running-->start app:"+packageName+" activity:"+activity);
        appContext.startActivity(intent);
    }
    public void killAndClearWxData(){
        System.out.println("doAction-->关闭、清楚数据");
        List<String> cmds = getKillAndClearWxCmds();
        for(String cmd:cmds){
            exeShell(cmd);
        }
    }
    public void cmdScrrenShot(){
        exeShell("input keyevent 120");
        AutoUtil.sleep(1000);
    }
    public String exeShell(String cmd){
        try {
            String result =  mDevice.executeShellCommand(cmd);
            System.out.println("running-->cmd:"+cmd+"  ret:"+result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<String> getKillAndClearWxCmds(){
        List<String> cmds = new ArrayList<String>();
        cmds.add("am force-stop com.tencent.mm" );
        cmds.add("pm clear com.tencent.mm" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/MicroMsg" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_cache" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_dex" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_font" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_lib" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_recover_lib" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_tbs" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/cache" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/databases" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/face_detect" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/files" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/shared_prefs" );
        cmds.add("rm -r -f /sdcard/tencent" );
        return cmds;
    }

    public UiObject2 findNodeByBySelector(BySelector bs){
        if(bs!=null){
            UiObject2 uo = mDevice.findObject(bs);
            if(uo!=null) return uo;
        }
        return null;
    }
    public void setUiObject2Text(UiObject2 uo2,String text){
        if(uo2!=null){
            uo2.setText(text);
        }
    }
    public UiObject findNodeByUiSelector(UiSelector us){
        if(us!=null){
            UiObject uo = mDevice.findObject(us);
            if(uo!=null&&uo.exists()) return uo;
        }
        return null;
    }
    public UiObject findNodeByText(String text){
        UiSelector us = new UiSelector().text(text);
        return findNodeByUiSelector(us);
    }
    public UiObject findNodeByDesc(String desc){
        UiSelector us = new UiSelector().description(desc);
        return findNodeByUiSelector(us);
    }
    public UiObject findNodeById(String id){
        UiSelector us = new UiSelector().resourceId(id);
        return findNodeByUiSelector(us);
    }

    public boolean clickUiObject(UiObject uo){
        try {
            if(uo!=null&&uo.isClickable()){
                return uo.clickAndWaitForNewWindow();
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean setUiObjectText(UiObject uo,String text){
        if(uo!=null&&uo.exists()){
            try {
                return uo.setText(text);
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean clickUiObjectByText(String text){
        return clickUiObject(findNodeByText(text));
    }
    public boolean clickUiObjectByDesc(String desc){
        return clickUiObject(findNodeByDesc(desc));
    }
    public boolean setUiObjectTextByDesc(String desc,String text){
        return setUiObjectText(findNodeByDesc(desc),text);
    }
    public boolean setUiObjectTextByText(String uiText,String text){
        return setUiObjectText(findNodeByText(uiText),text);
    }
    public boolean setUiObjectTextById(String id,String text){
        return setUiObjectText(findNodeById(id),text);
    }
    public List<UiObject2> findNodesByClaZZ(Class clazz){
        BySelector bs = By.clazz(clazz);
        if(bs!=null){
            return mDevice.findObjects(bs);
        }
        return null;
    }

    public UiSelector findUiSelectorByDesContain(String desc){
          return new UiSelector().descriptionContains(desc);
    }
    public UiObject findUiObjectByDesContain(String desc){
         return mDevice.findObject(findUiSelectorByDesContain(desc));
    }
    public boolean clickNodeByDesContain(String desc){
        UiObject obj = findUiObjectByDesContain(desc);
        try {
            if(obj!=null&&obj.isClickable()){
                return obj.click();
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
