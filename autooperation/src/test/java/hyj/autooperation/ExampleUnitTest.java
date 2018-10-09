package hyj.autooperation;

import android.graphics.Point;

import com.alibaba.fastjson.JSON;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        /*int startX = 235;
        int endX = 830;

        List<Integer> xList = getAllInterVal(startX,endX,50,80,70,5,10);
        System.out.println(JSON.toJSONString(xList));
        Point[] points = getSwipePoints(startX,endX,50,80,70,5,10,1000,1050);
        System.out.println(JSON.toJSONString(points));*/
        String pwd = createPwdByPhone("13625485654");
        System.out.println("doAction--->"+pwd);

    }

    public  String createPwdByPhone(String phone){
        if(phone==null||phone.length()<7) return "null";
        String[] zm = {"c","v","b","k","p","y","h","f","g","m"};
        String pwd = zm[Integer.parseInt(phone.substring(4,5))]+zm[Integer.parseInt(phone.substring(5,6))];
        for(int i=phone.length()-1;i>=phone.length()-6;i--){
            pwd = pwd +phone.charAt(i);
        }
        return pwd;
    }

    /**
     * @param startX endX 滑动起始 结束 位置
     * @param min1，max1 滑动前段快速部分每段间隔为 min1和max1 之间随机数
     * @param slowDistance 后面减速距离
     * @param min2,max2 滑动 后面减速距离 每段间隔为min2和max2 之间随机数
     * @param yMin ,yMax 滑动y方向波动距离
     * @return
     */
    public Point[] getSwipePoints(int startX,int endY,int min1,int max1,int slowDistance,int min2,int max2,int yMin,int yMax){
        List<Integer> xList = getAllInterVal(startX,endY,min1,max1,slowDistance,min2,max2);
        Point[] points = new Point[xList.size()];
        for(int i=0;i<xList.size()-1;i++){
            Point point = new Point();
            point.set(xList.get(i),getRandomNum(yMin,yMax));
            points[i] = point;
        }
        return points;
    }

    /**
     * @param startX endX 滑动起始 结束 位置
     * @param min1，max1 滑动前段快速部分每段间隔为 min1和max1 之间随机数
     * @param slowDistance 后面减速距离
     * @param min2,max2 滑动 后面减速距离 每段间隔为min2和max2 之间随机数
     * @return 滑动点集合
     */
    public List<Integer> getAllInterVal(int startX,int endX,int min1,int max1,int slowDistance,int min2,int max2){
        List<Integer> result  = new ArrayList<Integer>();
        List<Integer> list1 = getInterVal(startX,endX-slowDistance,min1,max1);
        List<Integer> list2 = getInterVal(endX-slowDistance,endX,min2,max2);
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }
    //获取指定范围随机数
    public int getRandomNum(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
    //startX 和 tmpEndX 划分段，每段大小为为min 和max之间随机数
    public List<Integer> getInterVal(int startX,int tmpEndX,int min,int max){
        List<Integer> list = new ArrayList<Integer>();
        list.add(startX);
        while (true){
            startX = startX+ getRandomNum(min,max);
            if(startX<tmpEndX){
                list.add(startX);
            }else {
                list.add(tmpEndX);
                break;
            }
        }
        return list;
    }
}