package hyj.xw.aw.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import hyj.xw.hook.newHook.PathFileUtil;
import hyj.xw.util.FileUtil;

/**
 * Created by asus on 2018/4/8.
 */

public class ReadAssetsFileUtil {
    public static String readAssetFileStr(Context paramContext, String paramString)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        try
        {
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramContext.getAssets().open(paramString)));
            while (true)
            {
                String str = localBufferedReader.readLine();
                if (str == null)
                    break;
                localStringBuffer.append(str).append("\n");
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return localStringBuffer.toString();
    }

    public static List<String> readAssetFileList(Context paramContext, String paramString)
    {
        ArrayList localArrayList = new ArrayList();
        try
        {
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramContext.getAssets().open(paramString)));
            while (true)
            {
                String str = localBufferedReader.readLine();
                if (str == null)
                    break;
                localArrayList.add(str);
            }
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
        return localArrayList;
    }


}

