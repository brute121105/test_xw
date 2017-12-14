package hyj.xw;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hyj.xw.conf.CreateNode2DB;
import hyj.xw.dao.WxNodeDao;
import hyj.xw.model.LitePalModel.WxNode;
import hyj.xw.util.DeviceParamUtil;
import hyj.xw.util.GetPermissionUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetPermissionUtil.getReadAndWriteContactPermision(this,MainActivity.this);

        Button openAssit = (Button)this.findViewById(R.id.open_assist);
        openAssit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                Toast.makeText(MainActivity.this, "打开启权限，才能运行", Toast.LENGTH_LONG).show();
                testMethod();
            }
        });



    }
    public void testMethod(){
        //CreateNode2DB.create();
        WxNodeDao.findAllNode();
    }
}
