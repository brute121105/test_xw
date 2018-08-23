package hyj.autooperation;

import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
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
import android.support.test.uiautomator.StaleObjectException;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hyj.autooperation.common.FilePathCommon;
import hyj.autooperation.common.WxNickNameConstant;
import hyj.autooperation.conf.WindowOperationConf;
import hyj.autooperation.httpModel.Device;
import hyj.autooperation.model.NodeInfo;
import hyj.autooperation.model.WindowNodeInfo;
import hyj.autooperation.model.Wx008Data;
import hyj.autooperation.service.HttpRequestService;
import hyj.autooperation.util.AutoUtil;
import hyj.autooperation.util.DragImageUtil2;
import hyj.autooperation.util.FileUtil;
import hyj.autooperation.util.LogUtil;
import hyj.autooperation.util.OkHttpUtil;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * bug 获取某些节点可能出现 android.support.test.uiautomator.StaleObjectException
 * 如：uiObject2!=null ;
 *    uiObject2.getText();
 *
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private UiDevice mDevice;
    private Context appContext;
    Instrumentation instrumentation;
    Device deviceConfig;
    HttpRequestService httpRequestService;


    @Before
    public void init(){
        appContext = InstrumentationRegistry.getContext();
        instrumentation = InstrumentationRegistry.getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);
        String deviceConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
        deviceConfig = JSONObject.parseObject(deviceConfigStr,Device.class);
        httpRequestService = new HttpRequestService(deviceConfig);
        System.out.println("doAction--->srConfig:"+JSON.toJSONString(deviceConfig));
    }

    public List<String> getAutoTypes(Device device){
        List<String> autoTypes = new ArrayList<String>();
        autoTypes.add(device.getRunType()==1?"注册":"养号");
        if(device.getSendFriends()==2) autoTypes.add("发圈");
        if(device.getExtractWxId()==2) autoTypes.add("提取wxid");
        if(device.getAddFriend()==2) autoTypes.add("加好友");
        return autoTypes;
    }

    @Test
    public void installTest(){
        UiObject uiObject2 = new UiObject(new UiSelector().className(EditText.class));
        if(uiObject2!=null){
            try {
                uiObject2.click();
                AutoUtil.sleep(500);
                uiObject2.setText("123");
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void changeIp(){
        System.out.println("doAction-->开始修改ip");
        if(deviceConfig.getChangeIpMode()==1){
            String productName = mDevice.getProductName();
            System.out.println("doAction--->productName:"+productName);
            if("hermes".equals(productName)){
                doVpnNote2();
            }else {
                doVpn();
            }
        }else if(deviceConfig.getChangeIpMode()==2){
            startAriPlaneMode(1000);
        }
        deviceConfig.setChangeIp(2);//标志修改ip完成
        saveDeviceConfig(deviceConfig);
        System.out.println("doAction-->结束修改ip");
    }

    public void initAuto(String tag){
        otherAutoTypes = getAutoTypes(deviceConfig);
        autoType = otherAutoTypes.get(0);
        ops = WindowOperationConf.getOperatioByAutoType(autoType);

        //killAndClearWxData();
        currentWx008Data = tellSetEnvirlmentAndGet008Data(tag);
        //changeIp();
        startWxConfirmClear();
        //startWx();
    }

    Wx008Data currentWx008Data;
    String windowText;
    String autoType="";//当前动作
    String stopState ="";
    //String currentOperation = "init";//当前点击动作
    WindowNodeInfo currentWindowNodeInfo = new WindowNodeInfo();
    List<String> otherAutoTypes;
    long lastSameOperationTime,lastNotNullTime;
    Map<String,WindowNodeInfo> ops;
    @Test
    public void useAppContext(){
        currentWindowNodeInfo.setOperation("init");
        mDevice.pressHome();
        initAuto("retry");
        //new MonitorStatusThread().start();

        while (true){
            try {
                AutoUtil.sleep(600);
                instrumentation = InstrumentationRegistry.getInstrumentation();
                mDevice = UiDevice.getInstance(instrumentation);
                mDevice.waitForIdle(50);
                deviceConfig = getDeviceConfig();
                saveRefreshTime2Device();
                System.out.println("v20180815 running-->autoType："+autoType+" currentOperation:"+currentWindowNodeInfo.getOperation()+" stopState:"+stopState);
                if(!mDevice.isScreenOn()){
                    mDevice.wakeUp();
                    System.out.println("doAction-->亮屏幕");
                }
                windowText = getAllWindowText("com.tencent.mm");
                //出现致命错误
                if(windowText.contains("登录出现错误，")){
                    LogUtil.logMyError(FilePathCommon.logErrorPath,windowText);
                    return;
                }
                if(TextUtils.isEmpty(windowText)){
                    mDevice.waitForIdle(50);
                    String pgName = mDevice.getCurrentPackageName();
                    if(!TextUtils.isEmpty(pgName)) windowText =getAllWindowText(pgName);
                }
                stopState = FileUtil.readAllUtf8(FilePathCommon.stopTxtPath);
                if(!"1".equals(stopState)){
                    System.out.println("doAction-->暂停"+stopState);
                    continue;
                }
                System.out.println("ops-->running-->getAllWindowText："+windowText);
                System.out.println("ops-->ops:"+JSON.toJSONString(ops));
                WindowNodeInfo wni = getWniByWindowText(ops,windowText);
                if(wni==null){
                    if(windowText.contains("progressBar")||windowText.contains("正在完成注册")) continue;
                    /**
                     * 处理微信不在当前窗口
                     */
                    long waitMs = System.currentTimeMillis()-lastNotNullTime;
                    if(waitMs>15000&&"自定义-点击注册2".equals(currentWindowNodeInfo.getOperation())){
                        mDevice.pressBack();
                        System.out.println("doAction---->自定义-点击注册2pressBack");
                        continue;
                    }
                    if(waitMs>90000){
                        System.out.println("doAction--->匹配null等待超过90秒，重试");
                        if(deviceConfig.getRunType()==1) updateDeviceConfig("regExp匹配null等待超过90秒1");
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry
                        return;
                        //initAuto("retry");
                    }
                    if(isMathOperation(currentWindowNodeInfo.getOperation())&&!windowText.contains("用短信验证码登录")&&!windowText.contains("正在登录..")&&!windowText.contains("正在载入数据..")){
                        System.out.println("doAction-->处理微信不在当前窗口");
                        startWx();
                    }
                    System.out.println("doAction-->windowText没有匹配ops动作 currentOperation:"+currentWindowNodeInfo.getOperation());
                    System.out.println("doAction-->匹配null持续时间："+waitMs);
                    if(windowText.contains("找不到网页")){
                        System.out.println("doAction-->网络加载失败，重试");
                        if(deviceConfig.getRunType()==1) updateDeviceConfig("regExp找不到网页");
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry
                        //initAuto("retry");
                        return;
                    }
                    continue;
                }else {
                    lastNotNullTime= System.currentTimeMillis();
                }
                if(currentWindowNodeInfo.getOperation().equals(wni.getOperation())){
                    long waitMs = System.currentTimeMillis()-lastSameOperationTime;
                    if(waitMs>90000){
                        System.out.println("doAction--->静止等待超过90秒，重试");
                        if(deviceConfig.getRunType()==1) updateDeviceConfig("regExp匹配null等待超过90秒2");
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry
                        //initAuto("retry");
                        return;
                        //continue;
                    }
                    System.out.println("doAction-->静止停留时间："+waitMs);
                }else {
                    lastSameOperationTime = System.currentTimeMillis();
                }
                if(windowText.contains("正在登录...")||windowText.contains("正在载入数据...")||windowText.contains("progressBar")) continue;
                currentWindowNodeInfo = wni;
                System.out.println("isWindowOperatonSucc ops-->wni："+JSON.toJSONString(wni));
                doAction(wni);
                if("自定义-登录异常".equals(wni.getOperation())&&wni.isWindowOperatonSucc()){
                    updateDeviceConfig("fail"+windowText);
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"next");//next登录下一个，retry新登录,首次开启也是retry
                    //initAuto("next");
                    return;
                }else if("自定义-注册异常二维码出现".equals(wni.getOperation())&&wni.isWindowOperatonSucc()){
                    updateDeviceConfig("regExp注册出现二维码");
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"next");//next登录下一个，retry新登录,首次开启也是retry
                    //initAuto("next");
                    return;
                }else if("改机失败".equals(wni.getWindowOperationDesc())&&wni.isWindowOperatonSucc()){
                    if(deviceConfig.getRunType()==1)  updateDeviceConfig("regExp改机失败");
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry
                    //initAuto("retry");
                    return;
                }else if((wni.getWindowOperationDesc().contains("发送短信失败或超过最大尝试次数")||wni.getOperation().contains("尚未收到短信"))&&wni.isWindowOperatonSucc()){
                    updateDeviceConfig("regExp发送短信失败或超过最大尝试次数");
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"next");//next登录下一个，retry新登录,首次开启也是retry
                    //initAuto("next");
                    return;
                }
                else if(wni.getOperation().contains("-结束")&&wni.isWindowOperatonSucc()){
                    if(wni.getOperation().contains("判断登录成功")){
                        updateDeviceConfig("success");
                        System.out.println("doAction--->写入登录success标志");
                    }
                    if(otherAutoTypes.size()==1){//等于1，登录成功没有其他动作
                        int i = 0;
                        while (i<10){
                            AutoUtil.sleep(1000);
                            System.out.println("doAction-->登录成功等待秒数 "+i);
                            ++i;
                        }
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"next");//next登录下一个，retry新登录,首次开启也是retry
                        //initAuto("next");//没有其他动作，下一个
                        return;
                    }else {
                        if(otherAutoTypes.indexOf(autoType)<otherAutoTypes.size()-1){
                            autoType = otherAutoTypes.get(otherAutoTypes.indexOf(autoType)+1);
                            System.out.println("doAction--->autoType："+autoType);
                            ops = WindowOperationConf.getOperatioByAutoType(autoType);
                            //返回主界面
                            while (mDevice.findObject(By.text("发现"))==null){
                                if(mDevice.getCurrentPackageName().contains("tencent")){
                                    mDevice.pressBack();
                                    System.out.println("doAction---->返回主界面pressBack");
                                }else {
                                    UiObject2 uiObject2 = mDevice.findObject(By.text("用短信验证码登录"));
                                    if(uiObject2!=null){//处理登录被退出登录界面情况
                                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry
                                        //initAuto("retry");
                                        return;
                                    }else {
                                        startWx();
                                    }
                                }
                                AutoUtil.sleep(1000);
                            }
                        }else {
                            FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"next");//next登录下一个，retry新登录,首次开启也是retry
                            //initAuto("next");//执行完所有动作，下一个
                            return;
                        }
                    }
                }

            }catch (Exception e){
                System.out.println("doAction---->全局异常");
                e.printStackTrace();
            }
        }
    }
    public void updateDeviceConfig(String msg){
        deviceConfig = getDeviceConfig();
        deviceConfig.setLoginResult(msg);
        saveDeviceConfig(deviceConfig);
    }

    public void updateDeviceConfigIp(String ip){
        deviceConfig = getDeviceConfig();
        deviceConfig.setIpAddress(ip);
        saveDeviceConfig(deviceConfig);
    }
    public boolean isMathOperation(String operation){
        boolean flag = false;
        List<String> operations = new ArrayList<String>();
        operations.add("自定义-输入账号密码");
        operations.add("点击发现");
        operations.add("点击朋友圈");
        operations.add("自定义-发送短信");
        operations.add("自定义-判断登录成功-结束");
        operations.add("自定义-进入朋友圈");
        operations.add("自定义-提取wxid-结束");
        operations.add("自定义-长按拍照分享");
        operations.add("自定义-随机界面");
        for(String op:operations){
            if(op.equals(operation))
                return true;
        }
        return flag;
    }
    @Test
    public void tesdot1(){
        while (true){
            String str = getAllWindowText("com.tencent.mm");
            mDevice.pressEnter();
            /*List<UiObject2> uos = findNodesByClaZZ(EditText.class);
            if(uos!=null&&uos.size()==3) {
                uos.get(0).setText(currentWx008Data.getNickName());
                mDevice.pressEnter();
                uos.get(1).setText(currentWx008Data.getPhone());
            }*/
            AutoUtil.sleep(2000);
        }
        //doVpn2();
    }

    //每30s刷新一次，记录活跃状态
    public void saveRefreshTime2Device(){
        long currentTime = System.currentTimeMillis();
        if(deviceConfig.getRefreshTime()==null||currentTime-deviceConfig.getRefreshTime()>30000){
            deviceConfig.setRefreshTime(currentTime);
            saveDeviceConfig(deviceConfig);
        }
    }
    public  Device getDeviceConfig(){
        String srConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
        Device srConfig = JSONObject.parseObject(srConfigStr,Device.class);
        return srConfig;
    }
    public  void saveDeviceConfig(Device device){
        if(device!=null){
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath, JSON.toJSONString(device));
        }
    }

    //自定义、通用两种
    public void doAction(WindowNodeInfo wni){
        if(wni.getOperation().contains("自定义-")){
            doCustomerAction(wni);
        }else {
            for(NodeInfo nodeInfo: wni.getNodeInfoList()){
                switch (nodeInfo.getNodeType()){
                    case 1:
                        nodeInfo.setOperationSucc(doClick(nodeInfo));
                        AutoUtil.sleep(nodeInfo.getNodeOperationSleepMs());
                        break;
                    case 6:
                        nodeInfo.setOperationSucc(doLongClick(nodeInfo));
                        break;
                }
            }
            wni.setWindowOperatonSucc(validIsAllTrue(wni));//设置总成功标志
        }
        System.out.println("doAction--->"+wni.toString());
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
        wni.setWindowOperatonSucc(false);
        if("自定义-点击注册2".equals(wni.getOperation())){
            List<UiObject2> uos = findNodesByClaZZ(EditText.class);
            if(uos!=null&&uos.size()==3){
                uos.get(0).setText(currentWx008Data.getNickName());
                String productName = mDevice.getProductName();
                if("hermes".equals(productName)){
                  mDevice.click(1006,1822);
                }
                uos.get(1).setText(currentWx008Data.getPhone());
                try {
                    uos.get(2).setText(currentWx008Data.getWxPwd());//密码
                }catch (Exception e){
                    AutoUtil.inputText(currentWx008Data.getWxPwd());
                    AutoUtil.sleep(4000);
                    e.printStackTrace();
                }
                isOperationsSucc = clickUiObjectByText("注册");
            }else {
                System.out.println("doAction---->点击注册22pressBack");
                mDevice.pressBack();
            }
            operationDesc = "输入账号"+currentWx008Data.getPhone()+"，输入密码"+currentWx008Data.getWxPwd()+"，点击【注册】"+isOperationsSucc+" uosSize:"+(uos==null?"null":uos.size());
        }else if("自定义-过滑块".equals(wni.getOperation())){
            if(validEnviroment()){//008不校验改机
                //delAllFile();
                System.out.println("doAction--->改机成功");
                /**
                 * 服务器获取
                 */
            /*String res = FileUtil.readAllUtf8(FilePathCommon.fkFilePath);
            System.out.println("doAction-->res:"+res);
            while (!res.contains(",")){
                System.out.println("doAction-->等待 res写入"+res);
                AutoUtil.sleep(1000);
                res = FileUtil.readAllUtf8(FilePathCommon.fkFilePath);
            }

            FkResponseBody frb = JSON.parseObject(res,FkResponseBody.class);
            List<Point> pointList = new ArrayList<Point>();
            int startX = 235,locY = 1029;
            pointList.add(new Point(startX,locY));
            for(int str:frb.getData().getPonits()){
                pointList.add(new Point(str,locY));
            }
            Point[] pArr = new Point[frb.getData().getPonits().length+1];
            mDevice.swipe(pointList.toArray(pArr),frb.getData().getTime());
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,"");
            AutoUtil.sleep(3000);*/
                /**
                 * 本地计算
                 */
                int waitPic = 0;
                //等待5s图片生成
                while (waitPic<5&&!windowText.contains("拖动下方滑块完成拼图")){
                    System.out.println("doAction--->windowText:"+windowText);
                    System.out.println("doAction--->等待滑块生成"+waitPic);
                    AutoUtil.sleep(1000);
                    waitPic = waitPic+1;
                    windowText = getAllWindowText("com.tencent.mm");
                }
                //cmdScrrenShot();//截图
                Bitmap bi = newWaitAndBitmap();
                Integer[] dragEndX = DragImageUtil2.newGetPic2LocXAndDrapX(bi);
                if(dragEndX[1]>300){
                    Point[] points = getSwipePoints(dragEndX[0],dragEndX[1]+63,50,100,70,5,10,1000,1050);//63为方块半宽度 dragEndX[0] 为拖动点x起始位置  dragEndX[1] 为方块二边沿起始位置
                    System.out.println("doAction--->拖动滑块开始1");
                    mDevice.swipe(points,15);
                    System.out.println("doAction--->拖动滑块结束1");
                }else {
                    mDevice.click(1000,1150);
                }
                AutoUtil.sleep(4000);
                operationDesc="new 拖动dragEndX"+JSON.toJSONString(dragEndX);
            }else {
                operationDesc="改机失败";
            }
        }else if("自定义-输入账号密码".equals(wni.getOperation())){
            if(validEnviroment()){
                System.out.println("doAction--->改机成功");
                List<UiObject2> uos = findNodesByClaZZ(EditText.class);
                if(uos!=null&uos.size()==2){
                    System.out.println("runn size："+uos.size());
                    String wxid = currentWx008Data.getPhone();
                    String pwd = currentWx008Data.getWxPwd();
                    if(!TextUtils.isEmpty(currentWx008Data.getWxId())){
                        wxid = currentWx008Data.getWxId();
                    }else if(!TextUtils.isEmpty(currentWx008Data.getWxid19())){
                        wxid = currentWx008Data.getWxid19();
                    }
                    if(!windowText.contains(wxid)){
                        uos.get(0).click();
                        AutoUtil.sleep(200);
                        uos.get(0).setText(wxid);
                        String pwd1 = TextUtils.isEmpty(pwd)?"nullnull":pwd;
                        try {
                            uos.get(1).setText(pwd1);
                        }catch (Exception e){
                            AutoUtil.inputText(pwd1);
                            e.printStackTrace();
                        }
                    }
                    isOperationsSucc = clickUiObjectByText("登录");
                    operationDesc = "输入账号["+wxid+"]密码["+pwd+"]点击登录"+isOperationsSucc;
                    AutoUtil.sleep(5000);
                }
            }else {
                isOperationsSucc = true;
                operationDesc = "改机失败";
            }
        }else if("自定义-登录下一步".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.text("下一步"));
            int y = uiObject2.getVisibleBounds().top-uiObject2.getVisibleBounds().height();
            int x = uiObject2.getVisibleBounds().width()/2;
            mDevice.click(x,y);
            operationDesc = "点击用微信号/QQ号/邮箱登录 x:"+x+" y:"+y;
        }else if("自定义-登录异常".equals(wni.getOperation())){
            operationDesc = wni.getMathWindowText();
            isOperationsSucc = true;
        }else if("自定义-判断登录成功-结束".equals(wni.getOperation())){
            if(!windowText.contains("不同意")){
                operationDesc = "登录成功";
                isOperationsSucc = true;
            }
        }else if("自定义-点我知道了".equals(wni.getOperation())){
            if(clickUiObjectByText("我知道了")){
                isOperationsSucc = true;
            }
            /*mDevice.waitForIdle(50);
            UiObject2 uiObject2 = mDevice.findObject(By.textContains(wni.getMathWindowText()));
            if(uiObject2!=null){
                int x = uiObject2.getVisibleBounds().centerX();
                int y = uiObject2.getVisibleBounds().bottom+(uiObject2.getVisibleBounds().height()*2);
                mDevice.click(x,y);
                operationDesc = "点我知道了 x:"+x+" y:"+y;
            }else {
                operationDesc="点我知道了 uiObject2 is null";
            }
            isOperationsSucc = true;*/
        }else if("自定义-输入发圈内容-结束".equals(wni.getOperation())){
            //String inputText = currentWx008Data.getPhone()+""+System.currentTimeMillis();
            String inputText = WxNickNameConstant.getName1();
            //UiObject2 uiObject2 = mDevice.findObject(By.textContains(wni.getMathWindowText()));
            UiObject uiObject2 = getUiObject1ByText(wni.getMathWindowText());
            if(uiObject2!=null){
                if(setUiObject1Text(uiObject2,inputText)){
                    if(clickUiObject1ByText("发表")){
                        if(otherAutoTypes.indexOf("发圈")==otherAutoTypes.size()-1) AutoUtil.sleep(5000);
                    }
                }
                //mDevice.findObject(By.text("发表")).click();
                operationDesc = "输入发圈内容："+inputText;
                isOperationsSucc = true;
            }
        }else if("自定义-注册异常二维码出现".equals(wni.getOperation())){
            operationDesc = wni.getOperation();
            isOperationsSucc = true;
        }else if("自定义-发送短信".equals(wni.getOperation())){

            /*UiObject2 sendMsg =  mDevice.findObject(By.text("发送短信"));
            if(sendMsg!=null){
                sendMsg.click();
                AutoUtil.sleep(1000);
                mDevice.pressBack();
            }*/
            UiObject2 notReceiveMsg = mDevice.findObject(By.textContains("尚未收到你发送的短信验证码"));
            if(notReceiveMsg!=null){
                mDevice.pressBack();
                System.out.println("doAction--->尚未收到你发送的短信验证码，休息20s");
                AutoUtil.sleep(20000);
            }
            UiObject2 sendContentUiObj = mDevice.findObject(By.textStartsWith("发送 zc"));
            UiObject2 receiveUiObj = mDevice.findObject(By.textStartsWith("到 "));
            int wt = 0;
            while ((sendContentUiObj==null||receiveUiObj==null)&&wt<30){
                //mDevice.click(70,128);
                System.out.println("doAction--->sendContentUiObj is null"+wt);
                AutoUtil.sleep(1000);
                sendContentUiObj = mDevice.findObject(By.textStartsWith("发送 zc"));
                receiveUiObj = mDevice.findObject(By.textStartsWith("到 "));
                wt = wt+1;
            }
            sendPhoneMsg(sendContentUiObj,receiveUiObj);
            int i = 0;
            while (i<90){
                AutoUtil.sleep(1000);
                System.out.println("doAction--->等待已发送短信，下一步"+i);
                UiObject2 nextUiObj = mDevice.findObject(By.textContains("已发送短信，下一步"));
                if(nextUiObj!=null){
                    AutoUtil.sleep(15000);
                    isOperationsSucc = true;
                    nextUiObj.click();
                    System.out.println("doAction--->点击已发送短信，下一步"+i);
                    AutoUtil.sleep(10000);
                    break;
                }else {
                    System.out.println("doAction--->已发送短信，下一步 is null"+i);
                }
                i++;
            }
            operationDesc = "发送短信验证"+sendContentUiObj.getText()+receiveUiObj.getText();
        }else if("自定义-尚未收到短信".equals(wni.getOperation())){
            operationDesc = "尚未收到短信,返回:"+mDevice.pressBack();
            System.out.println("doAction--->"+operationDesc);
            UiObject2 uiObject2 = mDevice.findObject(By.text("确定"));
            mDevice.waitForIdle(20);
            if(uiObject2!=null) uiObject2.click();
            int i = 0;
            while (i<45){
                AutoUtil.sleep(1000);
                System.out.println("doAction--->尚未收到短信 等待已发送短信，下一步"+i);
                UiObject2 nextUiObj = mDevice.findObject(By.textContains("已发送短信，下一步"));
                if(nextUiObj!=null){
                    isOperationsSucc = true;
                    nextUiObj.click();
                    AutoUtil.sleep(5000);
                    break;
                }else {
                    System.out.println("doAction--->尚未收到短信 已发送短信，下一步"+i);
                }
                i++;
            }
            if(i>43){
                isOperationsSucc = true;
            }
        }else if("自定义-提取wxid-结束".equals(wni.getOperation())){
            //UiObject2 uiObject2 = mDevice.findObject(By.textStartsWith("微信号：wxid"));
           /* int count = 0;
            while (!windowText.contains("微信号：wxid")&&count<5){
                mDevice.waitForIdle(10);
                if(!mDevice.getCurrentPackageName().contains("tencent")){
                    startWx();
                    AutoUtil.sleep(1000);
                }
                count = count+1;
                System.out.println("doAction--->向左滑动");
                mDevice.swipe(800,800,200,800,5);
                //uiObject2 = mDevice.findObject(By.textStartsWith("微信号：wxid"));
            }*/
            UiObject2 meUiObject = mDevice.findObject(By.text("我"));
            if(meUiObject!=null){
                meUiObject.click();
                AutoUtil.sleep(3000);
            }

            UiObject uiObject2 = new UiObject(new UiSelector().textStartsWith("微信号：wxid"));
            mDevice.waitForIdle(10);
            if(uiObject2!=null&&uiObject2.exists()){
                try {
                    String text = uiObject2.getText();
                    String wxid = text.substring(text.indexOf("：")+1);
                    deviceConfig = getDeviceConfig();
                    deviceConfig.setWxid(wxid);
                    saveDeviceConfig(deviceConfig);
                    isOperationsSucc = true;
                    operationDesc = "获取wxid："+wxid;
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                operationDesc = "获取wxid null";
            }

        }else if("自定义-长按拍照分享".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.descContains("更多功能按钮"));
            if(uiObject2!=null){
                uiObject2.longClick();
            }
            AutoUtil.sleep(800);
            isOperationsSucc = true;
        }else if("自定义-点击开始安全校验".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.descContains("开始"));
           if(uiObject2!=null){
               uiObject2.click();
               isOperationsSucc = true;
           }
            UiObject2 uiObject3 = mDevice.findObject(By.text("安全校验"));
            if(uiObject3!=null){
                System.out.println("doAction--->安全校验 点击坐标 539,1059");
                mDevice.click(539,1059);
                isOperationsSucc = true;
            }

            operationDesc = "点击 开始安全校验";
        }else if("自定义-随机界面".equals(wni.getOperation())){
            mDevice.pressBack();
            operationDesc = "随机界面 pressBack";
            isOperationsSucc = true;
        }else if("自定义-进入朋友圈".equals(wni.getOperation())){
            exeShell("am start -n com.tencent.mm/.plugin.sns.ui.SnsTimeLineUI");
            AutoUtil.sleep(3000);
            operationDesc = "am命令进入朋友圈";
            isOperationsSucc = true;
        }else if("自定义-网络错误".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.text("确定"));
            if(uiObject2!=null) uiObject2.click();
            operationDesc = "网络错误确定";
            isOperationsSucc = true;
        }else if("自定义-点击同意条款下一步".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.descContains("我已阅读并同意上述条款"));
            UiObject2 uiObject3 = mDevice.findObject(By.descStartsWith("下一步"));
            if(uiObject2!=null) uiObject2.click();
            if(uiObject3!=null) uiObject3.click();
            operationDesc = "点击同意条款下一步";
            isOperationsSucc = true;
        }else if("自定义-点击同意条款下一步6.6".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.text("同意"));
            if(uiObject2!=null){
                uiObject2.click();
                operationDesc = "点击同意条款下一步";
                isOperationsSucc = true;
            }
        }else if("自定义-am启动加好友".equals(wni.getOperation())){
            exeShell("am start -n com.tencent.mm/.plugin.subapp.ui.pluginapp.AddMoreFriendsUI");
            AutoUtil.sleep(3000);
            int cn = 0;
            while (cn<5){
                UiObject2 uiObject2 = mDevice.findObject(By.textStartsWith("我的微信号"));
                if(uiObject2==null){
                    System.out.println("doAction--->am加好友启动失败，再次启动");
                    exeShell("am start -n com.tencent.mm/.plugin.subapp.ui.pluginapp.AddMoreFriendsUI");
                }else {
                    break;
                }
                AutoUtil.sleep(4000);
                cn = cn+1;
            }
            operationDesc = wni.getOperation();
            isOperationsSucc = true;
        }else if("自定义-点击加好友输入框".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.textStartsWith("我的微信号"));
            System.out.println("doAction--->我的微信号uiObject2:"+uiObject2);
            if(uiObject2!=null){
                int x = uiObject2.getVisibleBounds().centerX();
                int y = uiObject2.getVisibleBounds().top-uiObject2.getVisibleBounds().height()/2;
                System.out.println("doAction--->点击加好友输入框x:"+x+" y:"+y);
                mDevice.click(x,y);
                isOperationsSucc = true;
            }
            operationDesc = wni.getOperation();
        }else if("自定义-输入微信号".equals(wni.getOperation())){
            mDevice.click(583,333);
            System.out.println("doAction---->点击坐标583,333");
            AutoUtil.sleep(1200);
            UiObject2 uiObject2 = mDevice.findObject(By.textContains("微信号/QQ号/手机号"));
            if(uiObject2!=null){
                uiObject2.setText(currentWx008Data.getFriends());
                AutoUtil.sleep(500);
                isOperationsSucc = true;
            }
            int cn = 0;
            while (cn<15){
                UiObject2 uiObject21 = mDevice.findObject(By.textContains("搜索"));
                if(uiObject21!=null){
                    uiObject21.click();
                    break;
                }
                AutoUtil.sleep(1000);
                cn = cn+1;
            }
            operationDesc = wni.getOperation();
        }else if("自定义-点击发送-结束".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.text("发送"));
            if(uiObject2!=null){
                uiObject2.click();
                isOperationsSucc = true;
            }
            int cn = 0;
            while (cn<20&&mDevice.findObject(By.text("添加到通讯录"))==null){
                cn = cn + 1;
                AutoUtil.sleep(1000);
                System.out.println("doAction--->点击发送，界面已切换");
            }
            operationDesc = wni.getOperation();
        }else if("自定义-输入发送内容-结束".equals(wni.getOperation())){

            UiObject uiObject2 = new UiObject(new UiSelector().className(EditText.class));
            if(uiObject2!=null){
                try {
                    uiObject2.click();
                    AutoUtil.sleep(500);
                    uiObject2.setText(currentWx008Data.getPhone());
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
                UiObject2 uiObject21 = mDevice.findObject(By.text("发送"));
                if(uiObject21!=null){
                    uiObject21.click();
                    System.out.println("doAction--->输入内容-点击发送");
                    httpRequestService.setFriendsNull(currentWx008Data.getId());
                    AutoUtil.sleep(8000);
                    isOperationsSucc = true;
                }
            }else {
                UiObject2 uiObject21 = mDevice.findObject(By.descContains("切换到键盘"));
                if(uiObject21!=null){
                    uiObject21.click();
                }else {
                    System.out.println("doAction--->获取输入框失败");
                    exeShell("am start -n com.tencent.mm/.plugin.subapp.ui.pluginapp.AddMoreFriendsUI");
                    AutoUtil.sleep(3000);
                }
            }
            operationDesc = wni.getOperation();
        }else if("自定义-无响应等待".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.text("等待"));
            if(uiObject2!=null){
                uiObject2.click();
                isOperationsSucc = true;
            }
            operationDesc = wni.getOperation();
        }
        wni.setWindowOperationDesc(operationDesc);
        wni.setWindowOperatonSucc(isOperationsSucc);
    }
    private UiObject getUiObject1ByDesc(String text){
        UiObject uiObject =  new UiObject(new UiSelector().description(text));
        return uiObject;
    }
    private boolean setUiObject1Text(UiObject uiObject,String setTextStr){
        if(uiObject==null) return false;
        try {
            return uiObject.setText(setTextStr);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    private UiObject getUiObject1ByText(String text){
        UiObject uiObject =  new UiObject(new UiSelector().text(text));
        return uiObject;
    }
    private boolean clickUiObject1(UiObject uiObject){
        if(uiObject==null) return false;
        try {
            return uiObject.clickAndWaitForNewWindow(500);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean clickUiObject1ByText(String text){
        return clickUiObject1(getUiObject1ByText(text));
    }

    private void sendPhoneMsg(UiObject2 sendContentUiObj,UiObject2 receiveUiObj){
        String content=sendContentUiObj.getText();
        String calledNumber=receiveUiObj.getText();
        if(content.contains("发送 ")&&calledNumber.contains("到 ")){
            String callNumber=currentWx008Data.getPhone();
            content = content.substring(content.indexOf(" ")+1);
            calledNumber = calledNumber.substring(calledNumber.indexOf(" ")+1);
            System.out.println("doAction--->告诉对方发送短信callNumber:"+callNumber+" calledNumber:"+calledNumber+" content:"+content);
            deviceConfig.setCallNumber(callNumber);//告诉对方发送短信
            deviceConfig.setCalledNumber(calledNumber);
            deviceConfig.setContent(content);
            saveDeviceConfig(deviceConfig);
            //resMsg = httpRequestService.sendSms(callNumber,calledNumber,content);
        }
    }

    /*private String sendPhoneMsg(UiObject2 sendContentUiObj,UiObject2 receiveUiObj){
        String resMsg = "发送失败";
        String content=sendContentUiObj.getText();
        String calledNumber=receiveUiObj.getText();
        if(content.contains("发送 ")&&calledNumber.contains("到 ")){
            String callNumber=currentWx008Data.getPhone();
            content = content.substring(content.indexOf(" ")+1);
            calledNumber = calledNumber.substring(calledNumber.indexOf(" ")+1);
            String url = WindowOperationConf.sendPhoneMsgUrl+"&callNumber="+callNumber+"&calledNumber="+calledNumber+"&content="+content;
            System.out.println("doAction---> sendPhoneMsg url-->"+url);
            String resBody = OkHttpUtil.okHttpGet(url);
            System.out.println("doAction--->resBody sendPhoneMsg url-->"+resBody);
            if(resBody.contains("提交成功")||resBody.contains("号码已发送过zc短信")){
                resMsg = "提交成功";
            }
        }
        return resMsg;
    }*/

    //判断改机是否成功 改机成功返回true
    private boolean validEnviroment(){
        String phoneTag = FileUtil.readAllUtf8(FilePathCommon.phoneTagPath);
        String phoneTag008 = TextUtils.isEmpty(currentWx008Data.getPhone())?currentWx008Data.getWxId():currentWx008Data.getPhone();
        System.out.println("phoneTag-->"+phoneTag+" phoneTag008:"+phoneTag008);
        if(!phoneTag.equals(phoneTag008)&&deviceConfig.getHookType()==1){
            LogUtil.login(" exception change phone fail",currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd());
            return false;
        }else {
            return true;
        }
    }

    public boolean doInputText(NodeInfo nodeInfo){
        if(!TextUtils.isEmpty(nodeInfo.getNodeText())){
             return setUiObjectTextByText(nodeInfo.getNodeText(),"556");
        }
        return false;
    }

    public boolean doLongClick(NodeInfo nodeInfo){
        if(!TextUtils.isEmpty(nodeInfo.getNodeDesc())){
            return longClickUiObjectByDesc(nodeInfo.getNodeDesc());
        }else if(!TextUtils.isEmpty(nodeInfo.getNodeText())){
            return longClickUiObjectByText(nodeInfo.getNodeText());
        }
        return false;
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
        List<WindowNodeInfo> wins = new ArrayList<WindowNodeInfo>();
        for(String key:ops.keySet()){
            String comKey = key.substring(key.indexOf("-")+1);//养号-注册|登录  去掉-前面
            if(comKey.contains("|")){
                String[] arr = comKey.split("\\|");
                boolean flag = true;
                for(String str :arr){
                    if(!windowText.contains(str)){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    wins.add(ops.get(key));
                }
            }else if(windowText.contains(comKey)){
                wins.add(ops.get(key));
            }
        }

        if(wins.size()==1){
            return wins.get(0);
        }else if(wins.size()==0){
            return null;
        }else if(wins.size()==2){//size=2，MathWindowText可能同时匹配的有 通讯录|发现 和 朋友圈，优先取 通讯录|发现 以外
            for(WindowNodeInfo windowNodeInfo:wins){
                if(!windowNodeInfo.getMathWindowText().contains("通讯录|发现")){
                    return  windowNodeInfo;
                }
            }
        }
        return null;
    }

    @Test
    public void testGetContent() throws Exception {
        while (true){
            getAllWindowText("com.tencent.mm");
        }
    }
    //获取当前窗口的所有文本，耗时200毫秒
    public String getAllWindowText(String pkName){
        System.out.println("debug--->start============getAllWindowText================");
        String windowContent = "";
        try {
            mDevice.waitForIdle(40);
            List<UiObject2> cbObjs = mDevice.findObjects(By.pkg(pkName));
            mDevice.waitForIdle(40);
            if(!cbObjs.isEmpty()){
                for(UiObject2 obj:cbObjs){
                    mDevice.waitForIdle(10);
                    windowContent = windowContent +getNotNullComponentText(obj);
                }
            }
        System.out.println("debug--->end============getAllWindowText================\n");
        }catch (Exception e){
            System.out.println("debug--->end StaleObjectException============getAllWindowText================\n");
            e.printStackTrace();
        }
        return windowContent;
    }

    public String newGetAllTextByClass(){
        String allText = getAllTextByClass(Button.class)+getAllTextByClass(EditText.class)+getAllTextByClass(View.class);
        return allText;
    }

    public String getAllTextByClass(Class clazz){
        String windowContent = "";
        try {
            List<UiObject2> cbObjs = mDevice.findObjects(By.clazz(clazz));
            if (!cbObjs.isEmpty()) {
                for (UiObject2 obj : cbObjs) {
                    windowContent = windowContent + getNotNullComponentText(obj);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return windowContent;
    }
    public String getAllWindowText1(){
        System.out.println("debug--->start============getAllWindowText1================");
        String windowContent = "";
        try {

            List<UiObject2> cbObjs = mDevice.findObjects(By.clazz(CheckBox.class));
            if(!cbObjs.isEmpty()){
                for(UiObject2 obj:cbObjs){
                    windowContent = windowContent +getNotNullComponentText(obj);
                }
            }
            List<UiObject2> edtObjs = mDevice.findObjects(By.clazz(EditText.class));
            List<UiObject2> trObjs = mDevice.findObjects(By.clazz(TextView.class));
            List<UiObject2> viewObjs = mDevice.findObjects(By.clazz(View.class));
            List<UiObject2> btnObjs = mDevice.findObjects(By.clazz(Button.class));
            if(!btnObjs.isEmpty()){
                for(UiObject2 obj:btnObjs){
                    windowContent = windowContent +getNotNullComponentText(obj);
                }
            }
            if(!viewObjs.isEmpty()){
                for(UiObject2 obj:viewObjs){
                    windowContent = windowContent +getNotNullComponentText(obj);
                }
            }
            if(!edtObjs.isEmpty()){
                for(UiObject2 obj:edtObjs){
                    windowContent = windowContent +getNotNullComponentText(obj);
                }
            }
            if(!trObjs.isEmpty()){
                for(UiObject2 obj:trObjs){
                    windowContent = windowContent +getNotNullComponentText(obj);
                }
            }
            System.out.println("debug--->end============getAllWindowText1================\n");
        }catch (Exception e){
            System.out.println("debug--->end StaleObjectException============getAllWindowText1================\n");
            e.printStackTrace();
        }
        return windowContent;
    }
    //获取非空文本
    public String getNotNullComponentText(UiObject2 obj){
        String result = "";
        try {
            //System.out.println("debug---> text:"+obj.getText()+" desc:"+obj.getContentDescription());
            mDevice.waitForIdle(50);
            //String clsName  = obj.getClassName();
            String text  = obj.getText();
            String getContentDescription  = obj.getContentDescription();
            boolean isClickable  = obj.isClickable();
            mDevice.waitForIdle(50);
            System.out.println(" debug--->"+" text:"+text+" desc:"+getContentDescription+" isclick:"+isClickable);
            if(!TextUtils.isEmpty(text)) result = result+text+"|";
            if(!TextUtils.isEmpty(getContentDescription)) result = result+getContentDescription+"|";
        }catch (StaleObjectException e){
            System.out.println("debug--->end  getText StaleObjectException==============================\n");
            e.printStackTrace();
        }
        return result;
    }

    private File waitAndGetFile(String path){
        File picFile = null;
        File[] files = new File(path).listFiles();
        if(files!=null&&files.length>0){
            picFile = files[files.length-1];
        }
        return picFile;
    }
    private void delAllFile(){
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        File[] files = new File(path).listFiles();
        if(files!=null&&files.length>0){
            for(File file:files){
                System.out.println("doAction-->删除文件："+file.getName()+" "+file.delete());
            }
        }
    }

   private void delAllFilesInDir(String dir){
        File[] files = new File(dir).listFiles();
       if(files!=null&&files.length>80){
           for(File f:files){
               boolean flag = f.delete();
           }
       }
    }

    public Bitmap waitAndGetBitmap(){
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        File file = waitAndGetFile(path);
        Bitmap bi = null;
        while (file==null||bi==null){
            AutoUtil.sleep(2000);
            file = waitAndGetFile(path);
            if(file!=null){
                String pngPath = path+"/"+file.getName();
                System.out.println("doAction--->file name:"+file.getName()+" size:"+file.length());
                bi = BitmapFactory.decodeFile(pngPath);
            }else {
                System.out.println("doAction--->file is null");
            }
            System.out.println("running-->等待图片生成:");
        }

        return bi;
    }

   /* public Bitmap newWaitAndBitmap(){
        File file = newWaitAndGetFile();
        System.out.println("doAction----->获取到文件："+file.getName()+" length:"+file.length());
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        String pngPath = path+"/"+file.getName();
        Bitmap bi = BitmapFactory.decodeFile(pngPath);
        while (bi==null){
            AutoUtil.sleep(1000);
            bi = BitmapFactory.decodeFile(pngPath);
            if(bi!=null){
                System.out.println("doAction--->biMap is ok height:"+bi.getHeight()+" width:"+bi.getWidth());
            }else {
                System.out.println("doAction--->biMap is null");
            }
        }
        return bi;
    }*/
     public Bitmap newWaitAndBitmap(){
         delAllFilesInDir(FilePathCommon.fkPngPath);
        File file = new File(FilePathCommon.fkPngPath);
        if(!file.exists()){
            file.mkdir();
        }
        String pathName = FilePathCommon.fkPngPath+File.separator+System.currentTimeMillis()+".png";;
        File pngFile = new File(pathName);
        boolean flag = mDevice.takeScreenshot(pngFile);
        System.out.println("doAction--->takeScreenshot flag:"+flag+" pathName:"+pathName);
        InputStream is =null;
        Bitmap bi = null;
        try {
            is = new FileInputStream(pngFile);
            bi = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bi;
    }
    public File newWaitAndGetFile(){
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        File file = waitAndGetFile(path);
        while (file==null){
            AutoUtil.sleep(1000);
            file = waitAndGetFile(path);
            if(file!=null){
                System.out.println("doAction--->file name:"+file.getName()+" size:"+file.length());
            }else {
                System.out.println("doAction--->file is null");
            }
        }
        return file;
    }
    /**
     * @param startX endX 滑动起始 结束 位置
     * @param min1，max1 滑动前段快速部分每段间隔为 min1和max1 之间随机数
     * @param slowDistance 后面减速距离
     * @param min2,max2 滑动 后面减速距离 每段间隔为min2和max2 之间随机数
     * @param yMin ,yMax 滑动y方向波动距离
     * @return
     */
    public Point[] getSwipePoints(int startX,int endY,int min1,int max1,int slowDistance,int min2,int max2,int yMin,int yMax){
        List<Integer> xList = getAllInterVal(startX,endY,min1,max1,slowDistance,min2,max2);
        System.out.println("doAction-->滑动坐标距离"+JSON.toJSONString(xList));
        Point[] points = new Point[xList.size()];
        for(int i=0;i<xList.size();i++){
            Point point = new Point();
            point.set(xList.get(i),getRandomNum(yMin,yMax));
            points[i] = point;
        }
        return points;
    }

    /**
     * @param startX endX 滑动起始 结束 位置
     * @param min1，max1 滑动前段快速部分每段间隔为 min1和max1 之间随机数
     * @param slowDistance 后面减速距离
     * @param min2,max2 滑动 后面减速距离 每段间隔为min2和max2 之间随机数
     * @return 滑动点集合
     */
    public List<Integer> getAllInterVal(int startX,int endX,int min1,int max1,int slowDistance,int min2,int max2){
        List<Integer> result  = new ArrayList<Integer>();
        List<Integer> list1 = getInterVal(startX,endX-slowDistance,min1,max1);
        List<Integer> list2 = getInterVal(endX-slowDistance+3,endX,min2,max2);
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }
    //获取指定范围随机数
    public int getRandomNum(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
    //startX 和 tmpEndX 划分段，每段大小为为min 和max之间随机数
    public List<Integer> getInterVal(int startX,int tmpEndX,int min,int max){
        List<Integer> list = new ArrayList<Integer>();
        list.add(startX);
        while (true){
            startX = startX+ getRandomNum(min,max);
            if(startX<tmpEndX){
                list.add(startX);
            }else {
                list.add(tmpEndX);
                break;
            }
        }
        return list;
    }


    public Point[] getDargPoins(int startX,int endX,int locY){
        Point point0 = new Point();
        Point point1 = new Point();
        Point point2 = new Point();
        point0.set(startX,locY);
        point1.set(endX-70,locY);
        point2.set(endX,locY);
        Point[] points = {point0,point1,point2};
        return points;
    }

    public void startAriPlaneMode(long sleepMs){
        System.out.println("doAction-->开启飞行模式");
        exeShell("settings put global airplane_mode_on 1" );
        exeShell("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true" );
        AutoUtil.sleep(sleepMs);
        exeShell("settings put global airplane_mode_on 0");
        exeShell("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false");
        boolean flag = isConnectInternet();
        int i = 1;
        while (!flag){
            AutoUtil.sleep(800);
            ++i;
            flag = isConnectInternet();
            System.out.println("doAction-->开启飞行模式-等待网络恢复"+i);
        }
        AutoUtil.sleep(1000);
        deviceConfig.setIpAddress(getIp());
    }
    public void startWx(){
        System.out.println("doAction-->启动微信");
        startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
        AutoUtil.sleep(800);
    }
    public void startWxConfirmClear(){
        lastSameOperationTime = 0;
        lastNotNullTime = 0;
        System.out.println("doAction-->启动微信");
        startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
        AutoUtil.sleep(1000);
        int i = 0;
        int waitStartNum=0;
        while (!mDevice.getCurrentPackageName().contains("tencent")||mDevice.findObject(By.text("注册"))==null){
            System.out.println("doAction-->等待微信启动成功");
            AutoUtil.sleep(1000);
            String pgName  = mDevice.getCurrentPackageName();
            System.out.println("doAction-->"+mDevice.getProductName()+" "+pgName);
            if(pgName==null) continue;
            if(!pgName.contains("tencent")){
                if(waitStartNum>25){
                    waitStartNum = 0;
                    startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                }
                waitStartNum = waitStartNum+1;
                continue;
            }else if(mDevice.findObject(By.text("注册"))==null){
                AutoUtil.sleep(1000);
                ++i;
                if(i>30){
                    System.out.println("doAction-->上次清楚失败，继续清除");
                    AutoUtil.killAndClearWxData();
                    startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                }
            }
        }

    }

    public  void startAppByPackName(String packageName,String activity){
        exeShell("am start "+packageName+"/"+activity);
        /*Intent intent = new Intent();
        ComponentName cmp=new ComponentName(packageName,activity);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        System.out.println("running-->start app:"+packageName+" activity:"+activity);
        appContext.startActivity(intent);*/
    }
    public Wx008Data tellSetEnvirlmentAndGet008Data(String tag){
        //FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,tag);//next登录下一个，retry新登录,首次开启也是retry
       /* AutoUtil.sleep(800);//等待对方处理写入文件
        String str = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
        System.out.println("doAction-->监听当前标志："+str);
        while (str.equals("next")||str.equals("retry")){//等待对方写入hook和008data，对方修改状态，循环不执行
            str = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
            System.out.println("doAction-->等待对方完成done，当前标志："+str);
            AutoUtil.sleep(500);
        }*/
        if(deviceConfig.getHookType()==2){//如果008改机
            //if(deviceConfig.getRunType()==1)
                do008();//生成随机数据，zc才需要
        }
        String wx008DataSstr = FileUtil.readAllUtf8(FilePathCommon.wx008DataFilePath);
        Wx008Data currentWx008Data = JSON.parseObject(wx008DataSstr,Wx008Data.class);
        return currentWx008Data;
    }

    @Test
    public void testDo008(){
        do008();
    }


    String  action008="init";
    public boolean do008(){
        System.out.println("doAction-->设置008开始");
        while (true){
            try {
                System.out.println("doAction-->action008:"+action008);
                String allText = getAllWindowText("com.soft.apk008v");
                if(allText.contains("是否上传此错误报告")){
                    UiObject2 uiObject2 = mDevice.findObject(By.text("取消"));
                    if(uiObject2!=null){
                        uiObject2.click();
                        System.out.println("doAction--->008错误取消");
                    }
                }
                instrumentation = InstrumentationRegistry.getInstrumentation();
                mDevice = UiDevice.getInstance(instrumentation);
                String pnName = mDevice.getCurrentPackageName();
                //确保init状态在主界面
                if("init".equals(action008)&&pnName.contains("apk008v")&&!allText.contains("工具箱")){
                    mDevice.pressBack();
                    AutoUtil.sleep(300);
                    continue;
                }
                if(!pnName.contains("apk008v")){
                    AutoUtil.startAppByPackName(appContext,"com.soft.apk008v","com.soft.apk008.LoadActivity");
                    AutoUtil.sleep(2000);
                    if(mDevice.getCurrentPackageName().equals("com.miui.home")){
                        System.out.println("doAction-->com.miui.home pressBack");
                        mDevice.pressBack();
                    }
                    System.out.println("doAction-->打开008 pgName2");
                    continue;
                }

                if(allText.contains("工具箱")){
                    UiObject2 uiObject2 = mDevice.findObject(By.textStartsWith("ID:"));
                    if(uiObject2!=null){
                        int x = uiObject2.getVisibleBounds().centerX();
                        int y = uiObject2.getVisibleBounds().top-80;
                        System.out.println("doAction--->点击修改数据x:"+x+" y:"+y);
                        mDevice.click(x,y);
                        AutoUtil.sleep(500);
                        action008 = "点击修改数据";
                    }
                    continue;
                }
                if("点击修改数据".equals(action008)||"工具箱".equals(action008)){
                    System.out.println("doAction--->点击修改数据");
                    if(saveAndGenerate()){
                        AutoUtil.execShell("am force-stop com.soft.apk008v");
                        return true;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean saveAndGenerate(){
        UiObject2 uiObject21 = mDevice.findObject(By.text("保存"));
        System.out.println("doAction-->uiObject21:"+uiObject21);
        if(uiObject21!=null){
            uiObject21.click();
            System.out.println("doAction--->点击保存");
            AutoUtil.sleep(2000);
           // mDevice.pressBack();
           // mDevice.pressHome();
            action008 = "点击保存";
            return true;
        }
        return false;
    }


    public void cmdScrrenShot(){
        System.out.println("doAction-->开始截图");
        //boolean flag = mDevice.takeScreenshot(new File("/sdcard/azy/fangkuai11.png"));
        //exeShell("screencap -p /sdcard/azy/fangkuai22.png");
        //exeShell("input keyevent 120");
        mDevice.pressKeyCode(120);
        System.out.println("doAction-->结束截图");
        AutoUtil.sleep(1000);
    }

    public String exeShell(String cmd){
       /* try {
            mDevice.executeShellCommand(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        AutoUtil.execShell(cmd);
        System.out.println("running-->cmd:"+cmd);
        return "";
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
            if(uo!=null){
                if(uo.isClickable()){
                    return uo.clickAndWaitForNewWindow();
                }else {
                    System.out.println("doAction--->x:"+uo.getBounds().centerX()+" y:"+uo.getBounds().centerY());
                    return mDevice.click(uo.getBounds().centerX(),uo.getBounds().centerY());
                }
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean longClickUiObject(UiObject uo){
        try {
            if(uo!=null){
                if(uo.isLongClickable()){
                    return uo.longClick();
                }
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
    public boolean longClickUiObjectByDesc(String desc){
        return longClickUiObject(findNodeByDesc(desc));
    }
    public boolean longClickUiObjectByText(String text){
        return longClickUiObject(findNodeByText(text));
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
    //判断是否联网
    public boolean isConnectInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager)appContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable()) {
            //当前无可用网络
            return false;
        }else {
            //当前有可用网络
            return true;
        }
    }
    public boolean waitVpnConn(int seconds){
        int i = 0;
        while (i<seconds){
            if(isVpnConnected()) return true;
            System.out.println("doAction--->等待vpn连接"+i);
            AutoUtil.sleep(500);
            ++i;
        }
        return false;
    }
    public boolean isVpnConnected() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if(niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    @Test
    public void testVpdpn()  {
        doVpn();
    }
    public void doVpn(){
        String lastAction="init";
        while (true){
            //System.out.println("doAction----------------------------------------------------->action:"+lastAction);
            if("点击连接".equals(lastAction)&&waitVpnConn(35)){
                System.out.println("doAction--->vpn连接成功");
                if(waitAndCheckIp())  return;
            }
            String pkg = mDevice.getCurrentPackageName();
            String allText = getAllWindowText(pkg);
            if(allText.contains("是否上传此错误报告")){
                UiObject2 uiObject2 = mDevice.findObject(By.text("取消"));
                if(uiObject2!=null){
                    uiObject2.click();
                    System.out.println("doAction--->008错误取消");
                }
            }
            if(!"com.android.settings".equals(pkg)&&!"com.android.vpndialogs".equals(pkg)){
                System.out.println("doAction-->打开vpn:"+pkg);
                opentActivity(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                lastAction = "打开vpn界面";
                continue;
            }else if(allText.contains("网络共享")||allText.contains("与显示设备无线连接")){
                boolean flag = mDevice.click(584,410);
                lastAction = "点击VPN";
                System.out.println("doAction-->点击VPN"+flag);
            }else if(allText.contains("添加VPN")||allText.contains("开启VPN")){
                if(!allText.contains("连接")){
                    System.out.println("doAction-->点击弹出窗口 537,600");
                    mDevice.click(537,600);
                    lastAction = "点击弹出窗口";
                }
            }
            UiObject2 uiObject2  = mDevice.findObject(By.text("连接"));
            if(uiObject2!=null){
                UiObject2 uiObject21 = mDevice.findObject(By.res("com.android.settings:id/username"));
                UiObject2 uiObject22 = mDevice.findObject(By.res("com.android.settings:id/password"));
                if(uiObject21!=null&&uiObject22!=null){
                    delAndSetVpnAccount(uiObject21,deviceConfig.getVpnAccount());
                    delAndSetVpnPwd(uiObject22,deviceConfig.getVpnPassword());
                    mDevice.pressBack();
                    System.out.println("doAction-->pressBack去掉输入法");
                    mDevice.findObject(By.text("连接")).click();
                    lastAction = "点击连接";
                }

            }
            UiObject2 uiObject3  = mDevice.findObject(By.text("断开连接"));
            if(uiObject3!=null){
                System.out.println("doAction-->点击断开连接");
                uiObject3.click();
                lastAction = "点击断开连接";
            }

        }
    }

    public void delAndSetVpnAccount(UiObject2 uiObject2,String inputText){
        uiObject2.click();
        String text = uiObject2.getText();
        if(text!=null){
            for(int i=0;i<text.length();i++){
                mDevice.pressDelete();
            }
        }
        AutoUtil.inputText(inputText);
    }
    public void delAndSetVpnPwd(UiObject2 uiObject2,String inputText){
        uiObject2.click();
        for(int i=0;i<5;i++){
            mDevice.pressDelete();
        }
        AutoUtil.inputText(inputText);
    }
    /*public void doVpn(){
        String lastAction="init";
        while (true){
            //System.out.println("doAction----------------------------------------------------->action:"+lastAction);
            if("点击连接".equals(lastAction)&&waitVpnConn(35)){
                System.out.println("doAction--->vpn连接成功");
                if(waitAndCheckIp())  return;
            }
            String pkg = mDevice.getCurrentPackageName();
            String allText = getAllWindowText(pkg);
            if(allText.contains("是否上传此错误报告")){
                UiObject2 uiObject2 = mDevice.findObject(By.text("取消"));
                if(uiObject2!=null){
                    uiObject2.click();
                    System.out.println("doAction--->008错误取消");
                }
            }
            if(!"com.android.settings".equals(pkg)&&!"com.android.vpndialogs".equals(pkg)){
                System.out.println("doAction-->打开vpn:"+pkg);
                opentActivity(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                lastAction = "打开vpn界面";
                continue;
            }else if(allText.contains("网络共享")||allText.contains("与显示设备无线连接")){
                boolean flag = mDevice.click(584,410);
                lastAction = "点击VPN";
                System.out.println("doAction-->点击VPN"+flag);
            }else if(allText.contains("添加VPN")||allText.contains("开启VPN")){
                if(!allText.contains("连接")){
                    System.out.println("doAction-->点击VPN 537,600");
                    mDevice.click(537,600);
                }
            }
            UiObject2 uiObject2  = mDevice.findObject(By.text("连接"));
            if(uiObject2!=null){
                mDevice.click(1005,1215);
                AutoUtil.sleep(800);
                System.out.println("doAction-->点击连接前");
                mDevice.findObject(By.text("连接")).click();
                lastAction = "点击连接";
            }
            UiObject2 uiObject3  = mDevice.findObject(By.text("断开连接"));
            if(uiObject3!=null){
                System.out.println("doAction-->点击断开连接");
                uiObject3.click();
                lastAction = "点击断开连接";
            }

        }
    }*/
    @Test
    public void testVpdpnNote2()  {
        doVpnNote2();
    }

   public void doVpnNote2(){
        String lastAction="init";
        while (true){
            //System.out.println("doAction----------------------------------------------------->action:"+lastAction);
            if("点击开启VPN".equals(lastAction)&&waitVpnConn(10)){
                System.out.println("doAction--->vpn连接成功");
                if(waitAndCheckIp())  return;
            }
            String pkg = mDevice.getCurrentPackageName();
            String allText = getAllWindowText(pkg);
            if(!"com.android.settings".equals(pkg)&&!"com.android.vpndialogs".equals(pkg)){
                System.out.println("doAction-->打开vpn:"+pkg);
                opentActivity(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                lastAction = "打开vpn界面";
                continue;
            }else if(allText.contains("网络共享")||allText.contains("与显示设备无线连接")){
                boolean flag = mDevice.click(584,410);
                lastAction = "点击VPN";
                System.out.println("doAction-->点击VPN"+flag);
            }else if(allText.contains("添加VPN")||allText.contains("开启VPN")){
                List<UiObject2> chs = mDevice.findObjects(By.res("android:id/checkbox"));
                if(chs!=null&&chs.size()>1){
                    lastAction = "点击开启VPN";
                    System.out.println("doAction-->点击开启VPN");
                    switchVpnAndMidSleep(chs.get(1),5000);
                }else {
                    mDevice.pressBack();
                    System.out.println("doAction-->chs is null");
                }
            }
        }
    }

    //对比ip直到ip不同结束
    public boolean waitAndCheckIp(){
        AutoUtil.sleep(1000);
        String ipAddress = getIp();
        if("广东".equals(ipAddress)){
            mDevice.click(973,458);
            System.out.println("doAction--->获取本次ip广东,点击纠正");
            return false;
        }
        if(checkIp(ipAddress)){
            mDevice.pressBack();
            mDevice.pressHome();
            updateDeviceConfigIp(ipAddress);
            return true;
        }else {
            int waitCheckCn = 0;
            while (waitCheckCn<3){
                waitCheckCn = waitCheckCn+1;
                System.out.println("doAction--->休眠1秒再对比ip");
                AutoUtil.sleep(1000);
                ipAddress = getIp();
                if(checkIp(ipAddress)){
                    mDevice.pressBack();
                    mDevice.pressHome();
                    updateDeviceConfigIp(ipAddress);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIp(String ipAdress){
        System.out.println("doAction--->上次ip："+deviceConfig.getIpAddress()+" 本次获取ip："+ipAdress);

        if(TextUtils.isEmpty(ipAdress)){
            System.out.println("doAction--->获取本次ip失败");
            return false;
        }
        if(ipAdress.equals(deviceConfig.getIpAddress())){
            System.out.println("doAction--->获取ip相同："+deviceConfig.getIpAddress());
            return false;
        }
        return true;
    }

    public void switchVpnAndMidSleep(UiObject2 uiObject2,long ms){
        mDevice.click(973,458);
        AutoUtil.sleep(ms);
        mDevice.click(973,458);
    }

    //获取当前ip
    public String getIp(){
        System.out.println("doAction--->告诉对方获取ip");
        deviceConfig = getDeviceConfig();
        deviceConfig.setLastIpAddress("1");//告诉对方获取ip
        saveDeviceConfig(deviceConfig);
        int i = 0;
        while (i<50){
            System.out.println("doAction--->等待获取ip "+i);
            AutoUtil.sleep(500);
            deviceConfig = getDeviceConfig();
            String ip = deviceConfig.getLastIpAddress();
            if(!"1".equals(ip)){
                return ip;
            }
            ++i;
        }
        return "";
    }

    public String getFuture(Future<String> future){
        String res = "";
        try {
             res  = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void opentActivity(String acvityName){
        try {
            Intent intent = new Intent(acvityName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(intent);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            System.out.println("doAction---ActivityNotFoundException");
        }
    }
}
