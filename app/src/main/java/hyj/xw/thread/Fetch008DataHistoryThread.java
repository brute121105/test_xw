package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class Fetch008DataHistoryThread extends BaseThread {

    public  final String TAG = this.getClass().getSimpleName();
    String currentName = null;
    public Fetch008DataHistoryThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
        AutoUtil.recordAndLog(record,"init");
    }

    int count=1;
    @Override
    public Object call()  {
        while (true){
            try {
                AutoUtil.sleep(500);
                //DaoUtil.findByDataFlag("008");
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

                if(parameters.getIsStop()==1){
                    LogUtil.d(TAG,"暂停....");
                    continue;
                }

                if(AutoUtil.checkAction(record,"init")||AutoUtil.checkAction(record,"008保存数据成功")){
                    NodeActionUtil.doClickByNodePathAndText(root,"快捷历史|历史记录","05","历史记录",record,"008点击历史记录",1000);
                }
                if(NodeActionUtil.isWindowContainStr(root,"设置最大容量")){//历史记录列表
                    List<AccessibilityNodeInfo> imeis = root.findAccessibilityNodeInfosByViewId("com.soft.apk008v:id/listItem_imei");
                    if(imeis!=null&&imeis.size()>0){
                        AccessibilityNodeInfo node = getNextNodeByCurrentNodeTextName(imeis,currentName);
                        if(node!=null){
                            currentName = node.getText().toString();
                            AutoUtil.performClick(node,record,"008点击记录",500);
                            continue;
                        }else {
                            boolean flag = AutoUtil.performScroll(imeis.get(0),record,"下滚");
                            System.out.println("node-->flag:"+flag);
                            continue;
                        }
                    }
                }

                //NodeActionUtil.doClickByNodePathAndText(root,"快捷历史|历史记录","07","随机生成",record,"008随机生成",1000);
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
                        System.out.println("写入数据库:--->"+count);
                        count = count+1;
                        AutoUtil.recordAndLog(record,"008保存数据成功");

                        LogUtil.log008(jsonStr);
                        LogUtil.d("RegisterService","写入txt:"+jsonStr);
                    }
                }

            }catch (Exception e){
                AutoUtil.recordAndLog(record,"抛出异常");
                LogUtil.logError(e);
            }
        }
    }
    public static AccessibilityNodeInfo  getNextNodeByCurrentNodeTextName( List<AccessibilityNodeInfo> imeis,String currentName){
        AccessibilityNodeInfo node  = null;
        if(imeis!=null&&imeis.size()>0){
           if(TextUtils.isEmpty(currentName)){
               node = imeis.get(0);
           }else {
               for(int i=0,l=imeis.size();i<l;i++){
                   if(imeis.get(i).getText().toString().equals(currentName)&&(i+1)<l){
                       node = imeis.get(i+1);
                   }
               }
           }
        }
        return node;
    }
}
