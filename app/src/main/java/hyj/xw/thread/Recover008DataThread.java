package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class Recover008DataThread extends BaseThread {

    public  final String TAG = this.getClass().getSimpleName();
    String currentName = null;
    public Recover008DataThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
        AutoUtil.recordAndLog(record,"init");
        init();
    }

    List<Wx008Data> wx008Datas;
    private int loginIndex;
    Wx008Data currentWx008Data;
    private void init(){
        wx008Datas = DaoUtil.getWx008Datas();
        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
        currentWx008Data = wx008Datas.get(loginIndex);
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

                AccessibilityNodeInfo cnNode1 = ParseRootUtil.getNodePath(root,"091");
                if(cnNode1!=null&&"序列号".equals(cnNode1.getText().toString())&&AutoUtil.checkAction(record,"init")){
                    //填充数据
                    set008Data(root);
                }



            }catch (Exception e){
                AutoUtil.recordAndLog(record,"抛出异常");
                LogUtil.logError(e);
            }
        }
    }

    private void set008Data(AccessibilityNodeInfo root){
        AccessibilityNodeInfo list = AutoUtil.findNodeInfosById(root,"com.soft.apk008v:id/set_value_con");
        if(list!=null){
            System.out.println("--list-getChildCount->"+list.getChildCount());
        }
        if(list!=null&&list.getChildCount()>90){
            AutoUtil.sleep(3000);
            System.out.println("currentWx008Data---->"+JSON.toJSONString(currentWx008Data));
            List<String> dataStrs =  JSON.parseArray(currentWx008Data.getDatas(),String.class);
            for(int i=1;i<91;i++){
                if(list.getChild(i).isEditable()){
                    String data;
                    if(dataStrs.get(1).contains("历史记录")){//红米2s提取的008数据
                        data  = dataStrs.get(i+1);
                    }else {
                        data  = dataStrs.get(i);
                    }
                    System.out.println("-rr->"+i+" "+data);
                    AutoUtil.performSetText(list.getChild(i),data,record,"008写入"+i+" "+data);
                }
            }
            AutoUtil.recordAndLog(record,"008写入数据完成");
            if(AutoUtil.checkAction(record,"008写入数据完成")){
                AutoUtil.sleep(3000);
                AccessibilityNodeInfo save =AutoUtil.findNodeInfosByText(root,"保存");
                AutoUtil.performClick(save,record,"st保存",3500);
                AutoUtil.recordAndLog(record,"saat写入数据");
            }
        }
    }

}
