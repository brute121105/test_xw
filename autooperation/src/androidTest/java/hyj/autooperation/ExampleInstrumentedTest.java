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
import com.alibaba.fastjson.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        appContext = InstrumentationRegistry.getTargetContext();
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
        return autoTypes;
    }

    public void initAuto(String tag){
        otherAutoTypes = getAutoTypes(deviceConfig);
        autoType = otherAutoTypes.get(0);
        ops = WindowOperationConf.getOperatioByAutoType(autoType);

        killAndClearWxData();
        if(deviceConfig.getChangeIpMode()==1){
            doVpn();
        }else if(deviceConfig.getChangeIpMode()==2){
            startAriPlaneMode(1000);
        }
        currentWx008Data = tellSetEnvirlmentAndGet008Data(tag);
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
                AutoUtil.sleep(800);
                deviceConfig = getDeviceConfig();
                System.out.println("running-->autoType："+autoType+" currentOperation:"+currentWindowNodeInfo.getOperation()+" stopState:"+stopState);
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
                    windowText =getAllWindowText1();
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
                    if(windowText.contains("正在登录...")||windowText.contains("正在载入数据...")||windowText.contains("progressBar")) continue;
                    /**
                     * 处理微信不在当前窗口
                     */
                    long waitMs = System.currentTimeMillis()-lastNotNullTime;
                    if(waitMs>15000&&"自定义-点击注册2".equals(currentWindowNodeInfo.getOperation())){
                        mDevice.pressBack();
                        continue;
                    }
                    if(waitMs>90000){
                        System.out.println("doAction--->匹配null等待超过90秒，重试");
                        if(deviceConfig.getRunType()==1) updateDeviceConfig("regExp匹配null等待超过90秒1");
                        initAuto("retry");
                    }
                    if(isMathOperation(currentWindowNodeInfo.getOperation())){
                        System.out.println("doAction-->处理微信不在当前窗口");
                        startWx();
                    }
                    System.out.println("doAction-->windowText没有匹配ops动作 currentOperation:"+currentWindowNodeInfo.getOperation());
                    System.out.println("doAction-->匹配null持续时间："+waitMs);
                    if(windowText.contains("找不到网页")){
                        System.out.println("doAction-->网络加载失败，重试");
                        if(deviceConfig.getRunType()==1) updateDeviceConfig("regExp找不到网页");
                        initAuto("retry");
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
                        initAuto("retry");
                        continue;
                    }
                    System.out.println("doAction-->静止停留时间："+waitMs);
                }else {
                    lastSameOperationTime = System.currentTimeMillis();
                }
                if(windowText.contains("正在登录...")||windowText.contains("正在载入数据...")||windowText.contains("progressBar")) continue;
                currentWindowNodeInfo = wni;
                System.out.println("ops-->wni："+JSON.toJSONString(wni));
                doAction(wni);
                if("自定义-登录异常".equals(wni.getOperation())&&wni.isWindowOperatonSucc()){
                    updateDeviceConfig("fail"+windowText);
                    initAuto("next");
                }else if("自定义-注册异常二维码出现".equals(wni.getOperation())&&wni.isWindowOperatonSucc()){
                    updateDeviceConfig("regExp注册出现二维码");
                    initAuto("next");
                }else if("改机失败".equals(wni.getWindowOperationDesc())&&wni.isWindowOperatonSucc()){
                    if(deviceConfig.getRunType()==1)  updateDeviceConfig("regExp改机失败");
                    initAuto("retry");
                }else if(wni.getWindowOperationDesc().contains("发送短信失败或超过最大尝试次数")&&wni.isWindowOperatonSucc()){
                    updateDeviceConfig("regExp发送短信失败或超过最大尝试次数");
                    initAuto("next");
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
                        initAuto("next");//没有其他动作，下一个
                    }else {
                        if(otherAutoTypes.indexOf(autoType)<otherAutoTypes.size()-1){
                            autoType = otherAutoTypes.get(otherAutoTypes.indexOf(autoType)+1);
                            ops = WindowOperationConf.getOperatioByAutoType(autoType);
                            //返回主界面
                            while (mDevice.findObject(By.text("发现"))==null){
                                if(mDevice.getCurrentPackageName().contains("tencent")){
                                    mDevice.pressBack();
                                }else {
                                    startWx();
                                }
                                AutoUtil.sleep(1000);
                            }
                        }else {
                            initAuto("next");//执行完所有动作，下一个
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
        for(String op:operations){
            if(op.equals(operation))
                return true;
        }
        return flag;
    }
    @Test
    public void test1(){
        while (true){
            AutoUtil.sleep(2000);
            List<UiObject2> uiObject2 = mDevice.findObjects(By.clazz(View.class).descContains("开始"));
            for(UiObject2 uiObject21:uiObject2){
                System.out.println("uiObject2-->"+uiObject21.getContentDescription());
            }


        }
    }

    public  Device getDeviceConfig(){
        String srConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
        Device srConfig = JSONObject.parseObject(srConfigStr,Device.class);
        return srConfig;
    }
    public  void saveDeviceConfig(Device device){
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath, JSON.toJSONString(device));
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
                uos.get(0).setText(currentWx008Data.getNickName());
                uos.get(1).setText(currentWx008Data.getPhone());
                uos.get(2).setText(currentWx008Data.getWxPwd());//密码
                isOperationsSucc = clickUiObjectByText("注册");
            }else {
                mDevice.pressBack();
            }
            operationDesc = "输入账号"+currentWx008Data.getPhone()+"，输入密码"+currentWx008Data.getWxPwd()+"，点击【注册】"+isOperationsSucc+" uosSize:"+(uos==null?"null":uos.size());
        }else if("自定义-过滑块".equals(wni.getOperation())){
            if(validEnviroment()){//008不校验改机
                delAllFile();
                AutoUtil.sleep(2000);
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
                cmdScrrenShot();//截图
                Bitmap bi = newWaitAndBitmap();
                Integer[] dragEndX = DragImageUtil2.newGetPic2LocXAndDrapX(bi);
                //Integer[] dragEndX = DragImageUtil2.getPic2LocXAndDrapX(bi);
                if(dragEndX[1]>300){
                    Point[] points = getSwipePoints(dragEndX[0],dragEndX[1]+63,50,100,70,5,10,1000,1050);//63为方块半宽度 dragEndX[0] 为拖动点x起始位置  dragEndX[1] 为方块二边沿起始位置
                    System.out.println("doAction--->拖动滑块开始1");
                    mDevice.swipe(points,15);
                    System.out.println("doAction--->拖动滑块结束1");
                }else {
                    mDevice.click(1000,1150);
                    /*Point[] points2 = getDargPoins(dragEndX[0],dragEndX[1]+66,1029);
                    mDevice.swipe(points2,50);*/
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
            operationDesc = "登录成功";
            isOperationsSucc = true;
        }else if("自定义-点我知道了".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.textContains(wni.getMathWindowText()));
            if(uiObject2!=null){
                int x = uiObject2.getVisibleBounds().centerX();
                int y = uiObject2.getVisibleBounds().bottom+(uiObject2.getVisibleBounds().height()*2);
                mDevice.click(x,y);
                operationDesc = "点我知道了 x:"+x+" y:"+y;
            }else {
                operationDesc="点我知道了 uiObject2 is null";
            }
            isOperationsSucc = true;
        }else if("自定义-输入发圈内容-结束".equals(wni.getOperation())){
            //String inputText = currentWx008Data.getPhone()+""+System.currentTimeMillis();
            String inputText = WxNickNameConstant.getName1();
            UiObject2 uiObject2 = mDevice.findObject(By.textContains(wni.getMathWindowText()));
            uiObject2.setText(inputText);
            mDevice.findObject(By.text("发表")).click();
            operationDesc = "输入发圈内容："+inputText;
            isOperationsSucc = true;
            AutoUtil.sleep(5000);
        }else if("注册异常-注册异常二维码出现".equals(wni.getOperation())){
            operationDesc = wni.getOperation();
            isOperationsSucc = true;
        }else if("自定义-发送短信".equals(wni.getOperation())){
            mDevice.findObject(By.text("发送短信")).click();
            AutoUtil.sleep(1000);
            if(!mDevice.getCurrentPackageName().contains("tencent")) mDevice.pressBack();

            UiObject2 notReceiveMsg = mDevice.findObject(By.textContains("尚未收到你发送的短信验证码"));
            if(notReceiveMsg!=null){
                mDevice.pressBack();
                System.out.println("doAction--->尚未收到你发送的短信验证码，休息20s");
                AutoUtil.sleep(20000);
            }

            UiObject2 sendContentUiObj = mDevice.findObject(By.textStartsWith("发送 zc"));
            UiObject2 receiveUiObj = mDevice.findObject(By.textStartsWith("到 "));
            String res = sendPhoneMsg(sendContentUiObj,receiveUiObj);
            UiObject2 nextUiObj = mDevice.findObject(By.text("已发送短信，下一步"));
            int i = 0;
            while (nextUiObj!=null&&sendContentUiObj!=null&&i<4){
                System.out.println("doAction--->点击已发送短信，下一步"+i);
                nextUiObj.click();
                sendContentUiObj = mDevice.findObject(By.textStartsWith("发送 zc"));
                nextUiObj = mDevice.findObject(By.text("已发送短信，下一步"));
                AutoUtil.sleep(15000);
                ++i;
            }
            operationDesc = "发送短信验证"+sendContentUiObj.getText()+receiveUiObj.getText()+" "+nextUiObj.getText()+" api返回结果："+res;
            if(!"提交成功".equals(res)||i>=4){
                operationDesc = "发送短信失败或超过最大尝试次数"+i;
            }
            isOperationsSucc = true;

        }else if("自定义-尚未收到短信".equals(wni.getOperation())){
            operationDesc = "尚未收到短信,返回:"+mDevice.pressBack();
            isOperationsSucc = true;
        }else if("自定义-提取wxid-结束".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.textStartsWith("微信号：wxid"));
            int count = 0;
            while (uiObject2==null&&count<5){
                if(!mDevice.getCurrentPackageName().contains("tencent")){
                    startWx();
                    AutoUtil.sleep(1000);
                }
                count = count+1;
                System.out.println("doAction--->向左滑动");
                mDevice.swipe(800,800,200,800,5);
                uiObject2 = mDevice.findObject(By.textStartsWith("微信号：wxid"));
            }
            String text = uiObject2.getText();
            String wxid = text.substring(text.indexOf("：")+1);

            deviceConfig = getDeviceConfig();
            deviceConfig.setWxid(wxid);
            saveDeviceConfig(deviceConfig);

            mDevice.swipe(222,900,888,900,5);
            AutoUtil.sleep(500);
            mDevice.swipe(222,700,888,700,10);
            AutoUtil.sleep(1000);

            operationDesc = "获取wxid："+wxid;
            isOperationsSucc = true;
        }else if("自定义-长按拍照分享".equals(wni.getOperation())){
            mDevice.findObject(By.desc("拍照分享")).longClick();
            AutoUtil.sleep(800);
            isOperationsSucc = true;
            while (mDevice.findObject(By.text("拍摄"))!=null){
                mDevice.pressBack();
                isOperationsSucc = false;
            }
        }else if("自定义-点击开始安全校验".equals(wni.getOperation())){
            UiObject2 uiObject2 = mDevice.findObject(By.descContains("开始"));
           if(uiObject2==null) mDevice.pressBack();
            uiObject2.click();
            operationDesc = "点击 开始安全校验";
            isOperationsSucc = true;
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
        }
        wni.setWindowOperationDesc(operationDesc);
        wni.setWindowOperatonSucc(isOperationsSucc);
    }

    private String sendPhoneMsg(UiObject2 sendContentUiObj,UiObject2 receiveUiObj){
        String resMsg = "发送失败";
        String content=sendContentUiObj.getText();
        String calledNumber=receiveUiObj.getText();
        if(content.contains("发送 ")&&calledNumber.contains("到 ")){
            String callNumber=currentWx008Data.getPhone();
            content = content.substring(content.indexOf(" ")+1);
            calledNumber = calledNumber.substring(calledNumber.indexOf(" ")+1);
            resMsg = httpRequestService.sendSms(callNumber,calledNumber,content);
        }
        return resMsg;
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
            List<UiObject2> cbObjs = mDevice.findObjects(By.pkg(pkName));
            if(!cbObjs.isEmpty()){
                for(UiObject2 obj:cbObjs){
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
            System.out.println("debug--->"+obj.getClassName()+" text:"+obj.getText()+" desc:"+obj.getContentDescription()+" pgName:"+obj.getApplicationPackage()+" resName:"+obj.getResourceName()+" childCount:"+obj.getChildCount()+" isclick:"+obj.isClickable()+" ischeked:"+obj.isChecked());
            if(!TextUtils.isEmpty(obj.getText())) result = result+obj.getText()+"|";
            if(!TextUtils.isEmpty(obj.getContentDescription())) result = result+obj.getContentDescription()+"|";
        }catch (Exception e){
            System.out.println("debug--->end  getText StaleObjectException==============================\n");
            e.printStackTrace();
        }
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
    private void delAllFile(){
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        File[] files = new File(path).listFiles();
        if(files!=null&&files.length>0){
            for(File file:files){
                System.out.println("doAction-->删除文件："+file.getName()+" "+file.delete());
            }
        }
    }
   /* private File delAllFiles(String path){
        File picFile = null;
        File[] files = new File(path).listFiles();
        for(File f:files){
            f.delete();
        }
        return picFile;
    }*/

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

   /* String action="";
    @Test
    public void useAppContext2() {

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
                    System.out.println("doAction-->new dragEndX:"+dragEndX);
                    if(dragEndX>300){
                        Point[] points = getSwipePoints(235,dragEndX+63,50,80,70,5,10,1000,1050);
                        mDevice.swipe(points,10);
                    }else {//无效距离
                        Point[] points2 = getDargPoins(235,dragEndX+63,1029);
                        mDevice.swipe(points2,10);
                    }
                }
            }
            UiObject2 qrWindow = mDevice.findObject(By.descContains("联系符合以下条件的"));
            if(qrWindow!=null){
                killAndClearWxData();
                startWx();
            }
        }
    }*/

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
    /*public void copyFile(File fromFile) {
        System.out.println("doAction-->copyFile:"+fromFile.getName());
        File toFile = new File("/sdcard/azy/"+fromFile.getName());
        FileInputStream ins = null;
        try {
            ins = new FileInputStream(fromFile);
            FileOutputStream out = new FileOutputStream(toFile);
            byte[] b = new byte[1024];
            int n=0;
            while((n=ins.read(b))!=-1){
                out.write(b, 0, n);
            }
            ins.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public Bitmap newWaitAndBitmap(){
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
                if(i>15){
                    System.out.println("doAction-->上次清楚失败，继续清除");
                    killAndClearWxData();
                    startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                }
            }
        }

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
    public Wx008Data tellSetEnvirlmentAndGet008Data(String tag){
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,tag);//next登录下一个，retry新登录,首次开启也是retry
        AutoUtil.sleep(800);//等待对方处理写入文件
        String str = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
        System.out.println("doAction-->监听当前标志："+str);
        while (str.equals("next")||str.equals("retry")){//等待对方写入hook和008data，对方修改状态，循环不执行
            str = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
            System.out.println("doAction-->等待对方完成done，当前标志："+str);
            AutoUtil.sleep(500);
        }
        String wx008DataSstr = FileUtil.readAllUtf8(FilePathCommon.wx008DataFilePath);
        Wx008Data currentWx008Data = JSON.parseObject(wx008DataSstr,Wx008Data.class);
        return currentWx008Data;
    }

    public void killAndClearWxData(){
        System.out.println("doAction-->关闭、清楚数据");
        List<String> cmds = getKillAndClearWxCmds();
        for(String cmd:cmds){
            exeShell(cmd);
        }
    }
    public void cmdScrrenShot(){
        System.out.println("doAction-->开始截图");
        ///boolean flag = mDevice.takeScreenshot(new File("/sdcard/azy/fangkuai.png"));
        //exeShell("screencap -p /sdcard/azy/fangkuai.png");
        //exeShell("input keyevent 120");
        mDevice.pressKeyCode(120);
        System.out.println("doAction-->结束截图");
        AutoUtil.sleep(1000);
    }
    /*public String exeShell(String cmd){
        try {
            String result =  mDevice.executeShellCommand(cmd);
            System.out.println("running-->cmd:"+cmd+"  ret:"+result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    public String exeShell(String cmd){
        AutoUtil.execShell(cmd);
        System.out.println("running-->cmd:"+cmd);
        return "";
    }
    public List<String> getKillAndClearWxCmds(){
        List<String> cmds = new ArrayList<String>();
        cmds.add("am force-stop com.tencent.mm" );
        cmds.add("pm clear com.tencent.mm" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/MicroMsg" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/app_cache" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_dex" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_font" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_lib" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_recover_lib" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_tbs" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/cache" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/databases" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/face_detect" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/files" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/shared_prefs" );
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
        if(uiObject2.isChecked()){
            uiObject2.click();
            AutoUtil.sleep(ms);
            uiObject2.click();
        }else {
            uiObject2.click();
        }
    }

    //获取当前ip
    public String getIp(){
        String ipUrl = "http://pv.sohu.com/cityjson?ie=utf-8";
        String ipStr = OkHttpUtil.okHttpGet(ipUrl);
        System.out.println("doActioni--->res ipStr:"+ipStr);
        String ip = "";
        if(ipStr.contains("cip")){
            JSONObject jsonObject = JSONObject.parseObject(ipStr.substring(ipStr.indexOf("{"),ipStr.indexOf("}")+1));
            ip = jsonObject.getString("cname")+jsonObject.getString("cip");
        }
        //updateDeviceConfigIp(ip);
        return ip;
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
