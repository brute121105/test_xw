package hyj.xw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wx.wyyk.netty.client.NettyClient;

import java.io.File;
import java.util.List;

import hyj.xw.GlobalApplication;
import hyj.xw.R;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.service.HttpRequestService;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.LogUtil;

public class AppSettingActivity extends AppCompatActivity implements View.OnClickListener{

    EditText delteIndexEdt;
    EditText newPwdEdt;
    EditText fzWxNumEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        Button exportConfigBtn = (Button)this.findViewById(R.id.btn_export_config);
        Button importConfigBtn = (Button)this.findViewById(R.id.btn_import_config);
        Button killAppBtn = (Button)this.findViewById(R.id.btn_kill_app);
        Button import62Btn = (Button)this.findViewById(R.id.btn_import_62);
        Button deleteIndexBtn = (Button)this.findViewById(R.id.btn_delete_data_index);
        Button updatePwdBtn = (Button)this.findViewById(R.id.btn_update_pwd);
        //数据索引范围
        delteIndexEdt = (EditText)this.findViewById(R.id.edt_delte_index);
        String delteIndexValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DELETE_DATA_INDEX);
        delteIndexEdt.setText(TextUtils.isEmpty(delteIndexValue)?"5-20":delteIndexValue);
        //修改密码
        newPwdEdt = (EditText)this.findViewById(R.id.edt_new_pwd);
        newPwdEdt.setText("www23");

        exportConfigBtn.setOnClickListener(this);
        importConfigBtn.setOnClickListener(this);
        killAppBtn.setOnClickListener(this);
        import62Btn.setOnClickListener(this);
        deleteIndexBtn.setOnClickListener(this);
        updatePwdBtn.setOnClickListener(this);
        //上传数据
        Button uploadDataBtn = (Button)this.findViewById(R.id.uploadData);
        uploadDataBtn.setOnClickListener(this);
        //开启辅助
        Button openAssist2Btn = (Button)this.findViewById(R.id.open_assist2);
        openAssist2Btn.setOnClickListener(this);
        //辅助微信号
        fzWxNumEdt = (EditText)this.findViewById(R.id.edt_fzWxNum);
        String fzWxNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_FZWXNUM);
        if(TextUtils.isEmpty(fzWxNum)){
            //为空，从读取008配置数据
            String wx008DataSstr = FileUtil.readAllUtf8(FilePathCommon.wx008DataFilePath);
            Wx008Data currentWx008Data = JSON.parseObject(wx008DataSstr,Wx008Data.class);
            fzWxNum = currentWx008Data.getPhone();
        }
        fzWxNumEdt.setText(fzWxNum);
        //启动长连接
        Button openLongConn = (Button)this.findViewById(R.id.open_long_conn);
        openLongConn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_export_config:
                exportAppConfig();
                break;
            case R.id.btn_import_config:
                importAppConfig();
                break;
            case R.id.btn_kill_app:
                AutoUtil.killApp();
                break;
            case R.id.btn_import_62:
                import62();
                break;
            case R.id.btn_delete_data_index:
                delteByIndex();
                break;
            case R.id.btn_update_pwd:
                updatePwd();
                break;
            case R.id.uploadData:
                uploadData();
                break;
            case R.id.open_assist2:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.open_long_conn:
                openLongConn();
                break;
        }
    }
    private void openLongConn(){
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
                String host = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_HOST);
                if(host.contains(":")){
                    host = host.substring(0,host.indexOf(":"));
                }
                System.out.println("doAction--->deviceNum:"+deviceNum+" host:"+host);
                NettyClient client = new NettyClient(host, 8000, deviceNum,(byte)1);
                try {
                    client.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
    private void uploadData(){
        final String indexStr = delteIndexEdt.getText().toString();
        final int startIndex,endIndex;
        if(indexStr.indexOf("-")>-1){
            String[] indexArr = indexStr.split("-");
            startIndex = Integer.parseInt(indexArr[0]);
            endIndex = Integer.parseInt(indexArr[1]);
            if(startIndex>endIndex){
                Toast.makeText(AppSettingActivity.this,"开始序号大于结束序号",Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            Toast.makeText(AppSettingActivity.this,"序号格式不错误，必须包含-，例如：5-20",Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequestService service = new HttpRequestService(3);
                List<Wx008Data> wx008Datas = DaoUtil.findByDataBydataFlag();
                int uploadCn=0,failCn = 0;
                for(int i=0,l=wx008Datas.size();i<l;i++){
                    if(wx008Datas.get(i).getExpMsg()!=null&&wx008Datas.get(i).getExpMsg().contains("success")){
                        if(i>=startIndex&&i<=endIndex){
                            Wx008Data wx008Data = wx008Datas.get(i);
                            wx008Data.setId(null);
                            createUploadWxdata1(wx008Data,i);//上次旧008数据
                            //createUploadWxdata2(wx008Data,i);
                            System.out.println(i+" upload req wx008Data --->"+JSON.toJSONString(wx008Data));
                            String str = service.uploadPhoneDataReturnAll(JSON.toJSONString(wx008Data));
                            System.out.println(i+" upload res--->"+str);
                            if(str.contains("成功")){
                                uploadCn = uploadCn+1;
                            }else {
                                failCn = failCn+1;
                            }

                        }
                    }
                }
                AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"上传序号"+indexStr+"之间：成功"+uploadCn+" 条，失败："+failCn+"条");
            }
        }).start();
    }

    //上次旧008数据
    public void createUploadWxdata1(Wx008Data wx008Data,int i){
        if(!TextUtils.isEmpty(wx008Data.getWxId())){
            wx008Data.setPhone(wx008Data.getWxId());//避免手机号重复
        }
        wx008Data.setDieFlag(0);
        wx008Data.setRegDevice("note2三 "+i);
        wx008Data.setRegHookType(2);
        wx008Data.setPhoneStrs("");
        wx008Data.setPhoneStrsAw("");
        wx008Data.setPhoneStrs008Json("");
        wx008Data.setDatas("");
        wx008Data.setId(null);
    }
    //上次数据，重新创建一份008数据
    public void createUploadWxdata2(Wx008Data wx008Data,int i){
        if(TextUtils.isEmpty(wx008Data.getWxId())&&!TextUtils.isEmpty(wx008Data.getWxid19())){
            wx008Data.setWxId(wx008Data.getWxid19());
        }
        wx008Data.setDatas("");
        wx008Data.setDieFlag(0);
        wx008Data.setRegDevice("note2四美国号 "+i);
        wx008Data.setRegHookType(2);
        wx008Data.setPhoneStrs(PhoneConf.create008DataStr(wx008Data.getPhone()));
        wx008Data.setPhoneStrsAw("");
        wx008Data.setPhoneStrs008Json("");
        wx008Data.setId(null);
    }

    private void importAppConfig(){
        File file = new File("/sdcard/A_hyj_json/appConfig/");
        File[] files = file.listFiles();
        String configStr="";
        if(files!=null&&files.length>0){
            String imporFileName = files[files.length - 1].getPath();//获取最新文件名
            System.out.println("--->导入文件：" + imporFileName);
            configStr = FileUtil.readAll(imporFileName);
            List<AppConfig> datas = JSON.parseArray(configStr, AppConfig.class);
            int cn = 0;
            for(AppConfig config:datas){
                if(TextUtils.isEmpty(config.getConfigContent())) continue;
                if(config.save()){
                    cn = cn+1;
                }
            }
            Toast.makeText(AppSettingActivity.this,"已导入条数："+cn,Toast.LENGTH_SHORT).show();
        }

    }
    private void exportAppConfig(){
        List<AppConfig> configs = AppConfigDao.findAll();
        String json = JSON.toJSONString(configs);
        LogUtil.exportConfig("/sdcard/A_hyj_json/appConfig/",json);
        Toast.makeText(AppSettingActivity.this,"已导出条数："+configs.size(),Toast.LENGTH_LONG).show();
    }
    private void updatePwd(){
        String indexStr = delteIndexEdt.getText().toString();
        String newPwdValue = newPwdEdt.getText().toString();
        List<Wx008Data> datas = DaoUtil.getWx008Datas();
        Wx008Data data = datas.get(Integer.parseInt(indexStr));
        String oldPwd = data.getWxPwd();
        data.setWxPwd(newPwdValue);
        int cn = data.updateAll("phone=?",data.getPhone());
        Toast.makeText(AppSettingActivity.this,"账号："+data.getPhone()+"原密码："+oldPwd+" 新密码："+newPwdValue+"\n已修改条数："+cn,Toast.LENGTH_LONG).show();

    }
    private void delteByIndex(){
        String indexStr = delteIndexEdt.getText().toString();
        int startIndex,endIndex;
        if(indexStr.indexOf("-")>-1){
            String[] indexArr = indexStr.split("-");
            startIndex = Integer.parseInt(indexArr[0]);
            endIndex = Integer.parseInt(indexArr[1]);
            if(startIndex>endIndex){
                Toast.makeText(AppSettingActivity.this,"开始序号大于结束序号",Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            Toast.makeText(AppSettingActivity.this,"序号格式不错误，必须包含-，例如：5-20",Toast.LENGTH_LONG).show();
            return;
        }
        List<Wx008Data> datas = DaoUtil.getWx008Datas();
        int delCn=0;
        for (int i = 0, l = datas.size(); i < l; i++) {
            if(i>=startIndex&&i<=endIndex){
                delCn = delCn+datas.get(i).delete();
            }
        }
        Toast.makeText(AppSettingActivity.this,"共删除需要"+indexStr+"之间："+delCn+" 条",Toast.LENGTH_LONG).show();
    }

    public void import62(){
        String wx = FileUtil.readAll1("/sdcard/wx.txt");
        String[] strs = wx.split("\n");
        int ct = 1;
        for(String str :strs){
            if(!TextUtils.isEmpty(str)&&str.length()>15){
                if(str.contains("封号")) continue;
                String[] s=null;
                if(str.indexOf("----")>-1){
                    s = str.split("----");
                }else if(str.indexOf("-")>-1){
                    s = str.split("-");
                }
                //String[] s = str.split("----");
                Wx008Data wx008Data = PhoneConf.create008Data(s[0],s[1],"1");
                System.out.println("import62---> ct:"+ct+" boolean:"+wx008Data.save()+" s[0]:"+s[0]+ "s[1]:"+s[1]);
                ct = ct+1;
            }
        }
    }
    private void save(){
        //删除数据索引
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_DELETE_DATA_INDEX,delteIndexEdt.getText().toString());
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_FZWXNUM,fzWxNumEdt.getText().toString());

    }
    @Override
    protected void onStop() {
        save();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }
}
