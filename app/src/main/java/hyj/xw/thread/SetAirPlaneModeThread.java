package hyj.xw.thread;

import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;

/**
 * Created by Administrator on 2018/1/19.
 */

public class SetAirPlaneModeThread extends Thread {
    private long ms;
    public SetAirPlaneModeThread(long ms){
        this.ms = ms;
    }
    @Override
    public void run() {
        super.run();
        LogUtil.d("SetAirPlaneModeThread","开启飞行模式");
        AutoUtil.setAriplaneMode(ms);
        LogUtil.d("SetAirPlaneModeThread","关闭飞行模式");
    }
}
