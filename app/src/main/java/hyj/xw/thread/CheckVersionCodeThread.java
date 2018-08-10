package hyj.xw.thread;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import hyj.xw.service.HttpRequestService;
import hyj.xw.util.OkHttpUtil;

/**
 * Created by asus on 2018/2/3.
 */

public class CheckVersionCodeThread implements Callable<String> {
    private String res = "";
    private String apkType;
    private int currentVersionCode;


    public CheckVersionCodeThread(String apkType,int currentVersionCode){
        this.apkType = apkType;
        this.currentVersionCode = currentVersionCode;
    }
    @Override
    public String  call() {
        HttpRequestService service = new HttpRequestService();
        res = service.checkUpdate(apkType,currentVersionCode);
        return res;
    }

    public FutureTask<String> startThreadByFuture(){
        FutureTask<String> futureTask = new FutureTask<>(this);
        new Thread(futureTask).start();
        return futureTask;
    }
}
