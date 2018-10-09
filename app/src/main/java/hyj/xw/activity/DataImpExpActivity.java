package hyj.xw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Map;

import hyj.xw.R;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.ImpExpData;
import hyj.xw.conf.PhoneConf;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.GetPhoneInfoUtil;
import hyj.xw.util.LogUtil;

public class DataImpExpActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_imp_exp);

        Button btn_impAData = (Button)this.findViewById(R.id.btn_impAData);
        Button btn_expAData = (Button)this.findViewById(R.id.btn_expAData);
        Button importBakDataBtn = (Button)this.findViewById(R.id.importBakData);
        Button exportBakDataBtn = (Button)this.findViewById(R.id.exportBakData);
        Button import008DataBtn = (Button)this.findViewById(R.id.btn_imp008Data);
        Button export008DataBtn = (Button)this.findViewById(R.id.btn_exp008Data);
        importBakDataBtn.setOnClickListener(this);
        exportBakDataBtn.setOnClickListener(this);
        btn_impAData.setOnClickListener(this);
        btn_expAData.setOnClickListener(this);
        import008DataBtn.setOnClickListener(this);
        export008DataBtn.setOnClickListener(this);

        Button killAppBtn = (Button)this.findViewById(R.id.btn_kill_app);
        killAppBtn.setOnClickListener(this);

        //返回
        Button afBackBtn = (Button)this.findViewById(R.id.btn_af_back);
        afBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.importBakData:
                Toast.makeText(this, "已导入数据：" + PhoneConf.importData(FilePathCommon.dataBakPath) + "条", Toast.LENGTH_LONG).show();
                break;
            case R.id.exportBakData:
                List<Wx008Data> datas = DataSupport.findAll(Wx008Data.class);
                //setPhoneInfo1置为空，解决导出后，导入失败
                Log.i("exportBakData","00");
                for(Wx008Data data:datas){
                    data.setPhoneInfo1(null);
                }
                Log.i("exportBakData","11");
                if (datas != null && datas.size() > 0) {
                    //LogUtil.export("/sdcard/A_hyj_008data/", JSON.toJSONString(datas));
                    LogUtil.export(FilePathCommon.dataBakPath, JSON.toJSONString(datas));
                    Toast.makeText(this, "已导出数据：" + datas.size() + "条", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "没有可导出数据", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_impAData:
                Map<String,Object> result =  ImpExpData.importAData();
                Toast.makeText(this,"导入成功："+result.get("countSucc")+"条,失败："+result.get("countExist")+"条", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_expAData:
                //ImpExpData.importAData();
                NewPhoneInfo npi = GetPhoneInfoUtil.getEnvironmentAwData();
                String json =  JSON.toJSONString(npi);
                LogUtil.exportAWUtf8ByPhoneName(FilePathCommon.importDataAPath,json,npi.getLine1Number());
                Toast.makeText(this, npi.getLine1Number(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_imp008Data:
                //Map<String,Object> resultImport008 =  ImpExpData.import008Data();
                //Toast.makeText(this,"导入成功："+resultImport008.get("countSucc")+"条,失败："+resultImport008.get("countExist")+"条", Toast.LENGTH_SHORT).show();
                ImpExpData.getPhonePwd008snlp();//上传图给的008数据
                break;
            case R.id.btn_exp008Data:
                break;
            case R.id.btn_kill_app:
                AutoUtil.killApp();
                break;
            case R.id.btn_af_back:
                finish();
                break;
        }
    }

}
