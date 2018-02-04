package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class Fetch008DataThread extends BaseThread {

    public  final String TAG = this.getClass().getSimpleName();
    public Fetch008DataThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
    }
    @Override
    public Object call()  {
        while (true){
            AutoUtil.sleep(3000);
            DaoUtil.findByDataFlag("008");
            LogUtil.d(TAG,Thread.currentThread().getName());
            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            if(root==null){
                LogUtil.d(TAG,"root is null");
                AutoUtil.sleep(500);
                continue;
            }
            System.out.println("deb==================================");
            ParseRootUtil.debugRoot(root);
            ParseRootUtil.getCurrentViewAllNode(root);

            NodeActionUtil.doClickByNodePathAndText(root,"快捷历史|历史记录","07","随机生成",record,"008随机生成",1000);
            AccessibilityNodeInfo list = AutoUtil.findNodeInfosById(root,"com.soft.apk008v:id/set_value_con");
            System.out.println("list-->"+list);
            if(list!=null){
                System.out.println("list size-->"+list.getChildCount());
            }
            if(list!=null&&list.getChildCount()>89){
                String[] str = new String[list.getChildCount()+2];
                for(int i=0;i<list.getChildCount();i++){
                    str[i]=list.getChild(i).getText()+"";
                }
                String jsonStr = JSON.toJSONString(str);

                Wx008Data wx008Data = new Wx008Data();
                wx008Data.setGuid(AutoUtil.getUUID());
                wx008Data.setDataFlag("008");
                wx008Data.setDatas(jsonStr);
                //wx008Data.setPhone(pa.getRegSuccessphone());
                //wx008Data.setWxPwd(pa.getZcPwd());
                //wx008Data.setCnNum(pa.getCnNum());
                wx008Data.setCreateTime(new Date());
                if(wx008Data.save()){
                    LogUtil.d("RegisterService","写入数据库:"+JSON.toJSONString(wx008Data));
                }

                LogUtil.log008(jsonStr);
                LogUtil.d("RegisterService","写入txt:"+jsonStr);
                AutoUtil.recordAndLog(record,"008写入成功注册数据完成");
                AutoUtil.sleep(1000);
            }

        }
    }

}
