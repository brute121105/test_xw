package hyj.xw.util;

import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;

/**
 * Created by Administrator on 2017/11/9.
 */

public class DragImageUtil {

    private static final int[] pic2Y = {331,361,736};
    private static final int startX=154;//第一个方块 开始找色位置X 154
    private static final int startY=403;//第一个方块 开始找色位置Y
    private static final int endY=972;//方块查找结束位置
    private static final int endX=878;//方块查找结束位置
    private static final int secondStartX=320;//第二方块 开始找色位置X
    private static final int pointNum=5;//判断颜色点个数
    private static final int distanceX=-5;// 两点之间行坐标距离
    private static final int distanceXForward=2;// 两点之间行坐标距离
    private static final int distanceY=5;//两点之间纵坐标距离
    private static final int num2=7;// 颜色差值位数
    private static final int num1=7;// 颜色差值位数

    private static final int DragImageStartx= 232;
    private static final int DragImageStarty= 1042;

    public static String dragPoint(Bitmap bi){
        int distance = getDragDistance(bi);
        int dragtoX = DragImageStartx+(distance-154);//应拖动到的目标x位置
        String ponitStr = DragImageStartx+" "+DragImageStarty+" "+dragtoX+" "+DragImageStarty;
        LogUtil.d("DrapImageThread","("+DragImageStartx+","+DragImageStarty+")->("+dragtoX+","+DragImageStarty+")");
        return ponitStr;
    }

    public static int getDragDistance(Bitmap bi){
        int findIndexY = getLocYofFirstRect(bi);
        int distance = getLocOfSecondRect(bi,findIndexY);//右边方块起始距离
        LogUtil.d("DrapImageThread","distance:"+distance);
        return distance;
    }

    public static int getLocOfSecondRect(Bitmap bi,int startY) {
        int findIndexX = secondStartX;
        while(findIndexX<endX) {

            int p1 = bi.getPixel(findIndexX, startY);
            int p2 = bi.getPixel(findIndexX+distanceX, startY);

            findIndexX =findIndexX+distanceXForward;
            String[] str = getPointDiffs(bi,findIndexX,startY);
            System.out.print(findIndexX+","+startY+"==="+p1+"-"+p2+"--second--"+(p2-p1)+"  >>>>findIndexX:"+findIndexX);
            System.out.println("  str****-->"+JSON.toJSONString(str));
            if(checkIsBigDiff(str,num2)) {
                break;
            }

        }
        System.out.println("second image x-->"+findIndexX);
        return findIndexX;
    }
    /**
     * 获取第一幅图纵坐标位置
     * @param bi
     * @return
     */
    public static int getLocYofFirstRect(Bitmap bi) {
        int findIndexY = startY;
        while(findIndexY<endY) {
            int p1 = bi.getPixel(startX, findIndexY);
            int p2 = bi.getPixel(startX+distanceX, findIndexY);
            System.out.print((startX+","+findIndexY)+"==="+p1+"-"+p2+"--first--"+(p2-p1));
            findIndexY =findIndexY+distanceY;

            String[] str = getPointDiffs(bi,startX,findIndexY);
            System.out.println("  str****-->"+JSON.toJSONString(str));
            if(checkIsAllBigDiff(str,num1)) {
                break;
            }

        }
        System.out.println("first image y-->"+findIndexY);
        return findIndexY;
    }


    /**
     *Y轴方向 从上往下  获取连续几个点的差异值 存数组
     * @param bi
     * @param locX 开始找色位置X

     */
    public static String[] getPointDiffs(Bitmap bi,int locX,int locY) {
        String[] strs = new String[pointNum];
        for(int i=0;i<pointNum;i++) {
            int diff = bi.getPixel(locX+distanceX, locY)-bi.getPixel(locX, locY);
            locY = locY+distanceY;
            strs[i]=String.valueOf(diff).replace("-", "");
        }
        return strs;
    }
    /**
     * 判断数组里颜色是否全部大于num
     * @param strs
     * @param num 颜色差值位数
     * @return
     */
    public static boolean checkIsBigDiff(String[] strs,int num) {
        int count=0;//计算位数个数
        int count2=0;//计算第一位数大于1个数
        boolean flag = false;
        for(String str:strs) {
            if(str.length()>=num) {
                count=count+1;
                //7位数首位必须大于1,
                if(str.length()==num&&Integer.parseInt(str.substring(0,1))>1){
                    count2 = count2+1;
                }else if(str.length()>num) {
                    count2 = count2+1;
                }
            }
        }
        if(count>=pointNum-1&&count2>=pointNum-2) {//5个点有4个或以上符合，就成立
            flag = true;
        }
        return flag;
    }

    /**
     * 判断数组里颜色是否全部大于num
     * @param strs
     * @param num 颜色差值位数
     * @return
     */
    public static boolean checkIsAllBigDiff(String[] strs,int num) {
        int count=0;//计算个数
        boolean flag = false;
        for(String str:strs) {
            if(str.length()>=num) {
                count=count+1;
            }
        }
        if(count==pointNum) {//5个点有4个或以上符合，就成立
            flag = true;
        }
        return flag;
    }


}
