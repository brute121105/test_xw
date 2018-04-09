package hyj.xw.aw.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import hyj.xw.hook.newHook.PathFileUtil;
import hyj.xw.util.FileUtil;


/**
 * Created by Administrator on 2018/4/9.
 */

public class Edbw69C30UgVp2ocKByJ
{
    public static String O000000o(Context paramContext)
    {
        StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
        return Formatter.formatFileSize(paramContext, localStatFs.getBlockSize() * localStatFs.getAvailableBlocks());
    }

    public static String O000000o(Context paramContext, String paramString)
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

    public static String O000000o(String paramString)
    {
        File localFile1 = new File(pU9KwKC2ppIRCaiFhaek.O00000Oo);
        if (!localFile1.exists())
            localFile1.mkdirs();
        File localFile2 = new File(localFile1, paramString);
        StringBuilder localStringBuilder = new StringBuilder();
        try
        {
            RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile2, "rw");
            localRandomAccessFile.seek(0L);
            while (true)
            {
                String str = localRandomAccessFile.readLine();
                if (str == null)
                {
                    localRandomAccessFile.close();
                    return localStringBuilder.toString();
                }
                localStringBuilder.append(new String(str.getBytes("ISO-8859-1"), "utf-8"));
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return localStringBuilder.toString();
    }

    public static List<String> O000000o(File paramFile)
    {
        ArrayList localArrayList = new ArrayList();
        try
        {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramFile));
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

    public static void O000000o()
    {
        File[] arrayOfFile = new File(pU9KwKC2ppIRCaiFhaek.O000000o).listFiles();
        if (arrayOfFile != null)
        {
            int i = arrayOfFile.length;
            for (int j = 0; j < i; j++)
            {
                File localFile = arrayOfFile[j];
                if (!localFile.getAbsolutePath().equalsIgnoreCase(pU9KwKC2ppIRCaiFhaek.O00000Oo))
                {
                    O00000Oo(localFile);
                    localFile.delete();
                }
            }
        }
    }

    public static void O000000o(File paramFile1, File paramFile2)
    {
        Log.d("FileUtils", paramFile1.getAbsolutePath() + " --> " + paramFile2.getAbsolutePath());
        FileInputStream localFileInputStream;
        try
        {
            localFileInputStream = new FileInputStream(paramFile1);
            FileOutputStream localFileOutputStream = new FileOutputStream(paramFile2);
            byte[] arrayOfByte = new byte[1444];
            while (true)
            {
                int i = localFileInputStream.read(arrayOfByte);
                if (i == -1)
                    break;
                localFileOutputStream.write(arrayOfByte, 0, i);
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
            return;
        }
        try {
            localFileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ERROR //
    public static void O000000o(String paramString1, String paramString2)
    {
        // Byte code:
        //   0: new 16	java/io/File
        //   3: dup
        //   4: aload_0
        //   5: invokespecial 90	java/io/File:<init>	(Ljava/lang/String;)V
        //   8: astore_2
        //   9: aload_2
        //   10: invokevirtual 94	java/io/File:exists	()Z
        //   13: ifne +8 -> 21
        //   16: aload_2
        //   17: invokevirtual 201	java/io/File:createNewFile	()Z
        //   20: pop
        //   21: new 185	java/io/FileOutputStream
        //   24: dup
        //   25: aload_2
        //   26: invokespecial 186	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
        //   29: astore_3
        //   30: aload_3
        //   31: aload_1
        //   32: ldc 203
        //   34: invokevirtual 125	java/lang/String:getBytes	(Ljava/lang/String;)[B
        //   37: invokevirtual 206	java/io/FileOutputStream:write	([B)V
        //   40: aload_3
        //   41: invokevirtual 209	java/io/FileOutputStream:flush	()V
        //   44: aload_3
        //   45: ifnull +7 -> 52
        //   48: aload_3
        //   49: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   52: return
        //   53: astore 9
        //   55: aload 9
        //   57: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   60: goto -39 -> 21
        //   63: astore 8
        //   65: aload 8
        //   67: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   70: return
        //   71: astore 4
        //   73: aconst_null
        //   74: astore_3
        //   75: aload 4
        //   77: invokevirtual 79	java/lang/Exception:printStackTrace	()V
        //   80: aload_3
        //   81: ifnull -29 -> 52
        //   84: aload_3
        //   85: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   88: return
        //   89: astore 7
        //   91: aload 7
        //   93: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   96: return
        //   97: astore 5
        //   99: aconst_null
        //   100: astore_3
        //   101: aload_3
        //   102: ifnull +7 -> 109
        //   105: aload_3
        //   106: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   109: aload 5
        //   111: athrow
        //   112: astore 6
        //   114: aload 6
        //   116: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   119: goto -10 -> 109
        //   122: astore 5
        //   124: goto -23 -> 101
        //   127: astore 4
        //   129: goto -54 -> 75
        //
        // Exception table:
        //   from	to	target	type
        //   16	21	53	java/io/IOException
        //   48	52	63	java/io/IOException
        //   21	30	71	java/lang/Exception
        //   84	88	89	java/io/IOException
        //   21	30	97	finally
        //   105	109	112	java/io/IOException
        //   30	44	122	finally
        //   75	80	122	finally
        //   30	44	127	java/lang/Exception
    }

    public static List<String> O00000Oo(Context paramContext, String paramString)
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

    public static void O00000Oo(File paramFile)
    {
        if (paramFile.isFile())
            paramFile.delete();
        while (!paramFile.isDirectory())
            return;
        File[] arrayOfFile = paramFile.listFiles();
        if ((arrayOfFile == null) || (arrayOfFile.length == 0))
        {
            paramFile.delete();
            return;
        }
        int i = arrayOfFile.length;
        for (int j = 0; j < i; j++)
            O00000Oo(arrayOfFile[j]);
        paramFile.delete();
    }

    // ERROR //
    public static void O00000Oo1(String paramString1, String paramString2)
    {
        // Byte code:
        //   0: new 16	java/io/File
        //   3: dup
        //   4: aload_0
        //   5: invokespecial 90	java/io/File:<init>	(Ljava/lang/String;)V
        //   8: astore_2
        //   9: aload_2
        //   10: invokevirtual 94	java/io/File:exists	()Z
        //   13: ifne +8 -> 21
        //   16: aload_2
        //   17: invokevirtual 201	java/io/File:createNewFile	()Z
        //   20: pop
        //   21: new 185	java/io/FileOutputStream
        //   24: dup
        //   25: aload_2
        //   26: invokespecial 186	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
        //   29: astore_3
        //   30: aload_3
        //   31: aload_1
        //   32: ldc 219
        //   34: invokevirtual 125	java/lang/String:getBytes	(Ljava/lang/String;)[B
        //   37: invokevirtual 206	java/io/FileOutputStream:write	([B)V
        //   40: aload_3
        //   41: invokevirtual 209	java/io/FileOutputStream:flush	()V
        //   44: aload_3
        //   45: ifnull +7 -> 52
        //   48: aload_3
        //   49: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   52: return
        //   53: astore 9
        //   55: aload 9
        //   57: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   60: goto -39 -> 21
        //   63: astore 8
        //   65: aload 8
        //   67: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   70: return
        //   71: astore 4
        //   73: aconst_null
        //   74: astore_3
        //   75: aload 4
        //   77: invokevirtual 79	java/lang/Exception:printStackTrace	()V
        //   80: aload_3
        //   81: ifnull -29 -> 52
        //   84: aload_3
        //   85: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   88: return
        //   89: astore 7
        //   91: aload 7
        //   93: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   96: return
        //   97: astore 5
        //   99: aconst_null
        //   100: astore_3
        //   101: aload_3
        //   102: ifnull +7 -> 109
        //   105: aload_3
        //   106: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   109: aload 5
        //   111: athrow
        //   112: astore 6
        //   114: aload 6
        //   116: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   119: goto -10 -> 109
        //   122: astore 5
        //   124: goto -23 -> 101
        //   127: astore 4
        //   129: goto -54 -> 75
        //
        // Exception table:
        //   from	to	target	type
        //   16	21	53	java/io/IOException
        //   48	52	63	java/io/IOException
        //   21	30	71	java/lang/Exception
        //   84	88	89	java/io/IOException
        //   21	30	97	finally
        //   105	109	112	java/io/IOException
        //   30	44	122	finally
        //   75	80	122	finally
        //   30	44	127	java/lang/Exception
    }


    public static boolean O00000Oo(final File file, final File file2) {
        int i = 0;
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!file.exists() || !file.isDirectory() || !file2.isDirectory()) {
            return false;
        }
        for (File[] listFiles = file.listFiles(); i < listFiles.length; ++i) {
            if (listFiles[i].isFile()) {
                O000000o(listFiles[i], new File(file2.getAbsolutePath(), listFiles[i].getName()));
            }
            else {
                O00000Oo(listFiles[i], new File(file2.getAbsolutePath(), listFiles[i].getName()));
            }
        }
        return true;
    }

    // ERROR //
    public static void O00000o0(String paramString1, String paramString2)
    {
        FileUtil.writeContent2FileForce(PathFileUtil.str10+ File.separator,paramString2,paramString1);
        // Byte code:
        //   0: new 16	java/io/File
        //   3: dup
        //   4: getstatic 89	com/money/utils/pU9KwKC2ppIRCaiFhaek:O00000Oo	Ljava/lang/String;
        //   7: invokespecial 90	java/io/File:<init>	(Ljava/lang/String;)V
        //   10: astore_2
        //   11: aload_2
        //   12: invokevirtual 94	java/io/File:exists	()Z
        //   15: ifne +8 -> 23
        //   18: aload_2
        //   19: invokevirtual 97	java/io/File:mkdirs	()Z
        //   22: pop
        //   23: new 16	java/io/File
        //   26: dup
        //   27: aload_2
        //   28: aload_1
        //   29: invokespecial 100	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
        //   32: astore_3
        //   33: aload_3
        //   34: invokevirtual 94	java/io/File:exists	()Z
        //   37: ifne +8 -> 45
        //   40: aload_3
        //   41: invokevirtual 201	java/io/File:createNewFile	()Z
        //   44: pop
        //   45: new 185	java/io/FileOutputStream
        //   48: dup
        //   49: aload_3
        //   50: invokespecial 186	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
        //   53: astore 4
        //   55: aload 4
        //   57: aload_0
        //   58: ldc 219
        //   60: invokevirtual 125	java/lang/String:getBytes	(Ljava/lang/String;)[B
        //   63: invokevirtual 206	java/io/FileOutputStream:write	([B)V
        //   66: aload 4
        //   68: invokevirtual 209	java/io/FileOutputStream:flush	()V
        //   71: aload 4
        //   73: ifnull +8 -> 81
        //   76: aload 4
        //   78: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   81: return
        //   82: astore 10
        //   84: aload 10
        //   86: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   89: goto -44 -> 45
        //   92: astore 9
        //   94: aload 9
        //   96: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   99: return
        //   100: astore 5
        //   102: aconst_null
        //   103: astore 4
        //   105: aload 5
        //   107: invokevirtual 79	java/lang/Exception:printStackTrace	()V
        //   110: aload 4
        //   112: ifnull -31 -> 81
        //   115: aload 4
        //   117: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   120: return
        //   121: astore 8
        //   123: aload 8
        //   125: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   128: return
        //   129: astore 6
        //   131: aconst_null
        //   132: astore 4
        //   134: aload 4
        //   136: ifnull +8 -> 144
        //   139: aload 4
        //   141: invokevirtual 210	java/io/FileOutputStream:close	()V
        //   144: aload 6
        //   146: athrow
        //   147: astore 7
        //   149: aload 7
        //   151: invokevirtual 151	java/io/IOException:printStackTrace	()V
        //   154: goto -10 -> 144
        //   157: astore 6
        //   159: goto -25 -> 134
        //   162: astore 5
        //   164: goto -59 -> 105
        //
        // Exception table:
        //   from	to	target	type
        //   40	45	82	java/io/IOException
        //   76	81	92	java/io/IOException
        //   45	55	100	java/lang/Exception
        //   115	120	121	java/io/IOException
        //   45	55	129	finally
        //   139	144	147	java/io/IOException
        //   55	71	157	finally
        //   105	110	157	finally
        //   55	71	162	java/lang/Exception
    }
}
