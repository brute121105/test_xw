package hyj.autooperation.util;

import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import hyj.autooperation.model.PixPoint;


/**
 * Created by Administrator on 2018/06/27.
 */

public class DragImageUtil2 {
    private static int availableWidth=130;//方块宽度
    private static int pic1X = 166;//方块一 x 白色定位
    private static int pic1X_1 = 154;//方块一 x 白色定位 第二种情况
    private static int minY=510,maxY=875;
    private static int pic1_rgb_r_min =230,pic1_rgb_r_max=250;//图片一 方块
    private static int pic2_minX = 730,pic2_maxX = 840;

    //获取方块二x坐标位置
    public static Integer[]  getPic2LocXAndDrapX(Bitmap bi) {
        Integer[] result = new Integer[2];
        result[0]= pic1X+availableWidth/2;//拖动地位置 方块起始位置+方块办宽
        //方块1坐落 像素点
        List<PixPoint> ps =  getScanXPoints(bi,pic1X);
        System.out.println("doAction-->getScanXPoints:"+JSON.toJSONString(ps));
        if(ps==null||ps.size()==0||ps.isEmpty()){
            result[0]= pic1X_1+availableWidth/2;
            ps =  getScanXPoints(bi,pic1X_1);
            System.out.println("doAction-->getScanXPoints 22:"+JSON.toJSONString(ps));
        }
        if(ps==null||ps.size()==0||ps.isEmpty()){
            result[1]= 0;
            return result;
        }
        //定位出方块1 Y方向位置，得出方块2 Y方向位置
        int scanY = ps.get(0).getY()+3;//加3，防止刚好边缘
        //获取Y方向相邻两点像素差较大的点,默认差值85
        List<PixPoint> targets = getSubtractPointBySubValue(bi,scanY,85);
        int targetX = 0;
        if(!targets.isEmpty()) {
            targetX = targets.get(0).getX();//默认取得第一个差距大的像素点为方块2起始点
        }
        result[1]= targetX;
        System.out.println("scanY:"+scanY+" target X:"+targetX);
        return result;
    }

    //获取方块二x坐标位置
    public static int  getPic2LocX(Bitmap bi) {
        //Bitmap bi = BitmapFactory.decodeFile(picName);;
        //方块1坐落 像素点
        List<PixPoint> ps =  getScanXPoints(bi,pic1X);
        System.out.println("doAction-->getScanXPoints:"+JSON.toJSONString(ps));
        if(ps==null||ps.size()==0||ps.isEmpty()){
            ps =  getScanXPoints(bi,pic1X_1);
            System.out.println("doAction-->getScanXPoints 22:"+JSON.toJSONString(ps));
        }
        if(ps==null||ps.size()==0||ps.isEmpty()) return 0;
        /*for(PixPoint p :ps) {
            System.out.println("ps_"+JSON.toJSONString(p));
        }*/

        //定位出方块1 Y方向位置，得出方块2 Y方向位置
        int scanY = ps.get(0).getY()+3;//加3，防止刚好边缘
        //获取Y方向相邻两点像素差较大的点,默认差值85
        List<PixPoint> targets = getSubtractPointBySubValue(bi,scanY,85);
        int targetX = 0;
        if(!targets.isEmpty()) {
            targetX = targets.get(0).getX();//默认取得第一个差距大的像素点为方块2起始点
        }
        System.out.println("scanY:"+scanY+" target X:"+targetX);
        return targetX;
    }

    //获取纵坐标位置scanY，横线上指定范围所有像素点
    public static List<PixPoint> getPointsByScanY(Bitmap bi,int scanY){
        List<PixPoint> pps = new ArrayList<PixPoint>();
        int i = pic2_minX;
        while(i>=pic2_minX&&i<=pic2_maxX) {
            int R = getImageRByXY(bi,i,scanY);
            //System.out.println("scanX-->"+i+" RGB:"+R);
            PixPoint pp = new PixPoint();
            pp.setX(i);
            pp.setR(R);
            pps.add(pp);
            i = i+1;
        }
        return pps;

    }
    //查找相邻两点像素差别最大的点
    public static List<PixPoint> getSubtractPointBySubValue(Bitmap bi,int scanY,int subValue) {
        List<PixPoint> pps = getPointsByScanY(bi,scanY);

        List<PixPoint> result = new ArrayList<PixPoint>();
        //刷选相邻像素点差别较大的点
        int i=0;
        while(i<pps.size()-1) {
            if(Math.abs(pps.get(i).getR()-pps.get(i+1).getR())>subValue) {
                result.add(pps.get(i));
            }
            i = i+1;
        }
        //System.out.println("result-->"+JSON.toJSONString(result)+" scanY:"+scanY);
        return result;

    }

    //获取方块一白色 像素点集合
    public static List<PixPoint> getScanXPoints(Bitmap bi,int LocPic1X){
        System.out.println("doAction---->LocPic1X:"+LocPic1X);
        List<PixPoint> pps = new ArrayList<PixPoint>();
        int i = minY;
        while(i>=minY&&i<=maxY) {
            int R = getImageRByXY(bi,LocPic1X,i);
            if(R>pic1_rgb_r_min&&R<pic1_rgb_r_max) {
                PixPoint pp = new PixPoint();
                pp.setR(R);
                pp.setY(i);
                pps.add(pp);
            }
            i = i+8;
        }

        //去除不连续点 ,正常情况，两点之间像素差别为8为连续点
        List<PixPoint> result = new ArrayList<PixPoint>();
        int index =0;
        while(index<=pps.size()-3) {
            if(pps.get(index).getY()+8==pps.get(index+1).getY()&&pps.get(index).getY()+16==pps.get(index+2).getY()) {
                result.add(pps.get(index));
                result.add(pps.get(index+1));
                result.add(pps.get(index+2));
                index = index+3;
            }else {
                index = index+1;
            }
        }
        return result;
    }

    //获取坐标颜色RGB
    public static int getImageRByXY(Bitmap bi,int x,int y) {
        //rgb[0] = (pix1 & 0xff0000) >> 16;
        //rgb[1] = (pix1 & 0xff00) >> 8;
        //rgb[2] = (pix1 & 0xff);
        int pix1 = bi.getPixel(x,y);
        int R= (pix1 & 0xff0000) >> 16;
        int G= (pix1 & 0xff00) >> 8;
        int B= (pix1 & 0xff);
        //System.out.println("doAction-->x:"+x+" y:"+y+" R:"+R+" G:"+G+" B:"+B);
        return R;
    }


}
