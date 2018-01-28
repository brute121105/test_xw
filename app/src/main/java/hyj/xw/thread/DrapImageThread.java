package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DragImageUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.ParseRootUtil;


/**
 * Created by asus on 2017/8/20.
 */

public class DrapImageThread extends BaseThread {
    int countWaitDragResultNum=0;

    public DrapImageThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
    }
    @Override
    public Object call() {
        while (true){
            AutoUtil.sleep(500);
            LogUtil.d("DrapImageThread","【拖动方块DrapImageThread...】"+Thread.currentThread().getName()+" ");
            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            if(root==null){
                LogUtil.d("DrapImageThread","DrapImageThread root is null");
                AutoUtil.sleep(500);
                continue;
            }
            //判断是否在方块界面
            AccessibilityNodeInfo fkNode = ParseRootUtil.getNodePath(root,"0000000000");
            if(fkNode==null||(fkNode!=null&&!"拖动下方滑块完成拼图".equals(fkNode.getContentDescription().toString()))){
                AutoUtil.sleep(2000);
                continue;
            }

            if(AutoUtil.checkAction(record,"wx方块拖动成功")){
                continue;
            }


            //等待拖动方块结果，由于执行拖动了没响应
            if(AutoUtil.checkAction(record,"wx拖动方块")){
                AccessibilityNodeInfo errorNode = ParseRootUtil.getNodePath(root,"0000000003");
                if(errorNode==null||(errorNode!=null&&!"请控制拼图块对齐缺口".equals(errorNode.getContentDescription()+""))){
                    countWaitDragResultNum = countWaitDragResultNum+1;
                    if(countWaitDragResultNum<30){
                        LogUtil.d("DrapImageThread","wait dragImage num:"+countWaitDragResultNum);
                        continue;
                    }else {
                        countWaitDragResultNum=0;
                    }
                }
            }

            //等待加载
            AccessibilityNodeInfo loadNode = ParseRootUtil.getNodePath(root,"000001");
            if(loadNode!=null&&(loadNode.getContentDescription()+"").indexOf("加载中")>-1){
                LogUtil.d("DrapImageThread","DrapImageThread root is 加载中...");
                AutoUtil.sleep(500);
                continue;
            }

            //方块处理
      if(1==1||AutoUtil.checkAction(record,"wx开始安全验证")){
            AccessibilityNodeInfo errorNode = ParseRootUtil.getNodePath(root,"0000000003");

           //判断是否拖动成功
           if(AutoUtil.checkAction(record,"wx拖动方块")||AutoUtil.checkAction(record,"wx计算距离无效")){
               if(errorNode!=null){
                   if("请控制拼图块对齐缺口".equals(errorNode.getContentDescription()+"")){
                       AccessibilityNodeInfo refreshNode = ParseRootUtil.getNodePath(root,"0030");
                       //AutoUtil.performClick(refreshNode,record,"wx刷新方块");
                       AutoUtil.clickXY(987,1175);
                       AutoUtil.recordAndLog(record,"wx刷新方块");
                       LogUtil.d("DrapImageThread","wx刷新方块"+refreshNode);
                       continue;
                   }else if((errorNode.getContentDescription()+"").indexOf("只用了")>-1){
                       AutoUtil.recordAndLog(record,"wx方块拖动成功");
                       continue;
                   }else{
                       LogUtil.d("DrapImageThread","等待拖动结果");
                       continue;
                   }
               }
           }
            if(fkNode!=null&&"拖动下方滑块完成拼图".equals(fkNode.getContentDescription().toString())){
                AutoUtil.sleep(1000);
                String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
                LogUtil.d("DrapImageThread","path-->"+path);

                //截图
                AutoUtil.execShell("input keyevent 120");
                LogUtil.d("DrapImageThread","截图");
                AutoUtil.sleep(4000);

                //获取图片，计算拖动坐标
                File picFile = waitAndGetFile(path);
                if(picFile ==null){
                    LogUtil.d("DrapImageThread","等待picFile生成");
                    AutoUtil.sleep(3000);
                    continue;
                }

               // InputStream is = getFileInputStrem(picFile);
               /* Bitmap bi = BitmapFactory.decodeStream(is);*/
                String picPath = path+"/"+picFile.getName();
                LogUtil.d("DrapImageThread","picPath:"+picPath);
                Bitmap bi = BitmapFactory.decodeFile(picPath);
                if(bi ==null){
                    LogUtil.d("DrapImageThread","等待bitmap生成");
                    AutoUtil.sleep(3000);
                    continue;
                }
                String dragStr = DragImageUtil.dragPoint(bi);

                //判断计算的是否空白图片距离
                String x2 = dragStr.split(" ")[2];
                LogUtil.d("DrapImageThread","x2:"+x2);
                if(Integer.parseInt(x2)>950){
                    AutoUtil.recordAndLog(record,"wx计算距离无效");
                    //AutoUtil.recordAndLog(record,"wx拖动方块");
                    AutoUtil.clickXY(987,1175);
                    AutoUtil.sleep(1000);
                    continue;
                }else if(Integer.parseInt(x2)>100){
                    LogUtil.d("DrapImageThread","开始拖动");
                    AutoUtil.execShell("input swipe "+dragStr);
                    LogUtil.d("DrapImageThread","结束拖动");
                    AutoUtil.recordAndLog(record,"wx拖动方块");
                    AutoUtil.sleep(500);
                }

            }
        }

        }
    }

   /* //轮询获取截图图片
    private File waitAndGetFile(String path){
        File picFile = null;
        while (picFile==null){
            File[] files = new File(path).listFiles();
            if(files!=null&&files.length>0){
                picFile = files[files.length-1];
            }else{
                AutoUtil.sleep(1000);
                LogUtil.d("DrapImageThread","等待截图生成");
            }
        }
        return picFile;
    }*/
   //轮询获取截图图片
   private File waitAndGetFile(String path){
       File picFile = null;
       File[] files = new File(path).listFiles();
       if(files!=null&&files.length>0){
           picFile = files[files.length-1];
       }
       return picFile;
   }

    private InputStream getFileInputStrem(File f){
        InputStream in = null;
        try {
            in = new FileInputStream(f);
            byte b[]=new byte[(int)f.length()];     //创建合适文件大小的数组
            try {
                in.read(b);    //读取文件中的内容到b[]数组
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(new String(b));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;

    }


}
