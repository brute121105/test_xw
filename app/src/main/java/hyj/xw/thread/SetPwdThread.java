package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;


/**
 * Created by asus on 2017/8/20.
 */

public class SetPwdThread implements Runnable {
    public  final String TAG = this.getClass().getSimpleName();
    AccessibilityService context;
    Map<String,String> record;
    Wx008Data currentWx008Data;
    AccessibilityParameters parameters;
    public SetPwdThread(AccessibilityService context, Map<String,String> record, Wx008Data currentWx008Data,AccessibilityParameters parameters){
        this.context = context;
        this.record = record;
        this.currentWx008Data = currentWx008Data;
        this.parameters = parameters;
    }
    String wxid="";
    @Override
    public void run() {
        while (true){
            AutoUtil.sleep(1500);
            if(!AutoUtil.actionContains(record,"SetPwdThread")&&!AutoUtil.checkAction(record,"1")) continue;
            if(AutoUtil.checkAction(record,"wx登陆成功")) return;

            LogUtil.d("SetPwdThread","【SetPwdThread...】"+Thread.currentThread().getName()+" phone:"+record.get("phone"));
            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            if(root==null){
                LogUtil.d("SetPwdThread","SetPwdThread root is null");
                AutoUtil.sleep(500);
                continue;
            }

            if(parameters.getIsStop()==1){
                LogUtil.d(TAG,"暂停....");
                continue;
            }

            ParseRootUtil.debugRoot(root);
            setPwdConfig(root,record);

        }
    }

    public void setPwdConfig(AccessibilityNodeInfo root,Map<String,String> record){
        //NodeActionUtil.doInputByNodePathAndText(root,"手机号登录|用短信验证码登录","00331","123",record,"wx输入密码",500);
        //NodeActionUtil.doClickByNodePathAndText(root,"手机号登录|用短信验证码登录","0035","登录",record,"wx点击登录",3000);
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","040","我",record,"SetPwdThread点击我",500);
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","00490","设置",record,"SetPwdThread点击设置");
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","00690","设置",record,"SetPwdThread点击设置");
        NodeActionUtil.doClickByNodePathAndText(root,"勿扰模式|聊天|通用","00260","帐号与安全",record,"SetPwdThread帐号与安全");
        NodeActionUtil.doClickByNodePathAndText(root,"邮件地址|未绑定|声音锁","00270","微信密码",record,"SetPwdThread微信密码");
        NodeActionUtil.doInputByNodePathAndText(root,"为保障你的数据安全，修改密码前请填写原密码。|取消","01",currentWx008Data.getWxPwd(),record,"SetPwdThread输入密码",1000);
        NodeActionUtil.doClickByNodePathAndText(root,"为保障你的数据安全，修改密码前请填写原密码。|取消","03","确定",record,"SetPwdThread输入密码确定",1000);
        String phone = TextUtils.isEmpty(currentWx008Data.getWxId())?currentWx008Data.getPhone():currentWx008Data.getWxId();
        System.out.println(TAG+"-->phone:"+phone+" ,currentWx008Data.getWxPwd():"+currentWx008Data.getWxPwd());
        String newPwd = "www23"+phone.substring(phone.length()-3);
        NodeActionUtil.doInputByNodePathAndText(root,"设置密码|完成|请设置微信密码。你可以用","0034",newPwd,record,"SetPwdThread输入密码1",1000);
        NodeActionUtil.doInputByNodePathAndText(root,"设置密码|完成|请设置微信密码。你可以用","0036",newPwd,record,"SetPwdThread输入密码2",1000);
        if(AutoUtil.checkAction(record,"SetPwdThread输入密码2")){
            AutoUtil.recordAndLog(record,"wx登陆成功");
            return;
        }
        NodeActionUtil.doClickByNodePathAndText(root,"设置密码|完成|请设置微信密码。你可以用","002","完成",record,"SetPwdThread设置微信密码完成",1000);
        if(AutoUtil.checkAction(record,"SetPwdThread设置微信密码完成")){
            List<Wx008Data> ds1 = DaoUtil.findByDataByColumn("guid",currentWx008Data.getGuid());
            System.out.println(TAG+"-->findByDataByColumn1:"+ JSON.toJSONString(ds1));
            System.out.println(TAG+"-->newPwd:"+newPwd+" old:"+currentWx008Data.getWxPwd()+" wxid:"+currentWx008Data.getWxId());
            int cn = DaoUtil.updatePwd(currentWx008Data,newPwd);
            System.out.println(TAG+"-->cn:"+ cn);
            if(cn==1){
                System.out.println(TAG+"-->updatePwd success");
                AutoUtil.recordAndLog(record,"wx登陆成功");
                List<Wx008Data> ds2 = DaoUtil.findByDataByColumn("guid",currentWx008Data.getGuid());
                System.out.println(TAG+"-->findByDataByColumn2:"+ JSON.toJSONString(ds2));
                return;

            }else {
                System.out.println(TAG+"-->updatePwd fail cn:"+cn);
            }
        }
    }

}
