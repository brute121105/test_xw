package hyj.xw.util;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import hyj.xw.GlobalApplication;

/**
 * Created by Administrator on 2017/5/15.
 */

public class FileUtil {
    boolean successFlag = false;

    //创建文件路径
    public static void createFilePath(String filePathName){
        /**
         * 创建路径
         */
        File filePath = null;
        try {
            filePath = new File(filePathName);
            if (!filePath.exists()) {
                filePath.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
    }

    //创建路径和文件
    public static void createFile2Path(String filePathName,String fileName){
        /**
         * 创建路径
         */
        File filePath = null;
        try {
            filePath = new File(filePathName);
            if (!filePath.exists()) {
                filePath.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
        /**
         * 创建文件
         */
        File file = null;
        try {
            file = new File(filePathName + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
    }

    //内容写入文件,覆盖
    public static void writeContent2FileForce(String filePathName,String fileName,String strcontent){
        createFile2Path(filePathName,fileName);
        String strFilePath = filePathName+fileName;
        try {
            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream (strFilePath,false),"gbk"));//设置成true就是追加 false 覆盖
            fw.write(strcontent);
            fw.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    //内容写入文件,覆盖
    public static void writeContent2FileForceUtf8(String filePathName,String fileName,String strcontent){
        createFile2Path(filePathName,fileName);
        String strFilePath = filePathName+fileName;
        try {
            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream (strFilePath,false),"utf-8"));//设置成true就是追加 false 覆盖
            fw.write(strcontent);
            fw.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    public static void writeContent2FileForceUtf8(String filePathAndFileName,String strcontent){
        String path = filePathAndFileName.substring(0,filePathAndFileName.lastIndexOf("/")+1);
        String fileName = filePathAndFileName.substring(filePathAndFileName.lastIndexOf("/")+1);
        writeContent2FileForceUtf8(path,fileName,strcontent);
    }


    public static void writeContentToJsonTxt(String fileName,String text){
        FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/",fileName,text);
    }
    public static String readContentToJsonTxt(String fileName){
        String con = FileUtil.readAll("/sdcard/A_hyj_json/"+fileName);
        System.out.println("readContentToJsonTxt--> fileName"+fileName+"content:"+con);
        return con;
    }

    //内容写入文件
    public static void writeContent2File(String filePathName,String fileName,String strcontent){
        createFile2Path(filePathName,fileName);
        String strFilePath = filePathName+fileName;
        // 每次写入时，换行
        String strContent = strcontent + "\r\n";

        try {
            File file = new File(strFilePath);
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes("gbk"));
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    //内容写入文件
    public static void writeContent2FileUtf8(String filePathName,String fileName,String strcontent){
        createFile2Path(filePathName,fileName);
        String strFilePath = filePathName+fileName;
        // 每次写入时，换行
        String strContent = strcontent + "\r\n";

        try {
            File file = new File(strFilePath);
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes("utf-8"));
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    public static List<String[]> readConfFile(String path) {
        List<String[]> list = new ArrayList<String[]>();
        try {
            String encoding="gbk";
            File file=new File(path);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    if(lineTxt.contains("----")){
                        String[] str = lineTxt.split("----");
                        if(str.length==2){
                            list.add(str);
                        }
                    }
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> read008Data(String path) {
        List<String> list = new ArrayList<String>();
        try {
            String encoding="gbk";
            File file=new File(path);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    list.add(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return list;
    }

    public static String readAll(String path) {
        String str = "";
        try {
            String encoding="gbk";
            File file=new File(path);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    str = str + lineTxt;
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return str;
    }

    public static String readAllUtf8(String path) {
        String str = "";
        try {
            String encoding="utf-8";
            File file=new File(path);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    str = str + lineTxt;
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return str;
    }

    public static String readAllUtf8ByFile(File file) {
        String str = "";
        try {
            String encoding="utf-8";
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    str = str + lineTxt;
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return str;
    }

    public static String readAll1(String path) {
        String str = "";
        try {
            String encoding="gbk";
            File file=new File(path);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    str = str + lineTxt+"\n";
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return str;
    }
  /*  //文件上传到服务器
    public static void uploadMultiFile(String url, String data, File file, final String fileName) {
        //开启子线程执行上传，避免主线程堵塞
      *//*  new Thread(new Runnable() {
            @Override
            public void run() {*//*
                //File file = new File(filePath, fileName);
                //if(file==null) file = new File("");
          if(file==null) file = new File("");
          RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                       *//* .addFormDataPart("file",fileName, fileBody)
                        .addFormDataPart("name",fileName)
                        .addFormDataPart("data",data);*//*//name是对方接收的另一个参数，文件名
              if(fileName!=null){
                  System.out.println("fileName--->"+fileName);
                  builder. addFormDataPart("file",fileName, fileBody)
                          .addFormDataPart("name",fileName);
              }

              if(data!=null){
                  builder. addFormDataPart("data",data);
              }

                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
                OkHttpClient okHttpClient  = httpBuilder
                        //设置超时
                        .readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "uploadMultiFile() e=" + e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.body().string();
                        Log.i(TAG, "uploadMultiFile() response=" +responseStr );
                    }
                });
            //}
        //}).start();
    }*/



    public static void copyFolder(File srcFile, File destFile) {
        if(srcFile.isDirectory()){
            File newFolder=new File(destFile,srcFile.getName());
            newFolder.mkdirs();
            File[] fileArray=srcFile.listFiles();

            for(File file:fileArray){
                copyFolder(file, newFolder);
            }
        }else{
            File newFile=new File(destFile,srcFile.getName());
            copyFile(srcFile,newFile);
        }

    }

    public static void copyFile(File srcFile, File newFile) {
        // TODO Auto-generated method stub
        BufferedInputStream bis= null;
        try {
            bis = new BufferedInputStream(new FileInputStream(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos= null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(newFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] bys=new byte[1024];
        int len=0;
        try {
            bos.write(bys,0,len);
            bos.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void copyFileByStream(InputStream is, File newFile) {
        // TODO Auto-generated method stub
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos= null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(newFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] bys=new byte[1024];
        int len=0;
        try {
            bos.write(bys,0,len);
            bos.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void copyAssetFile(String filename,String destinationPath) {
        AssetManager assetManager = GlobalApplication.getContext().getAssets();
        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        try {
            Log.i("tag", "copyFile() "+filename);
            in = assetManager.open(filename);
            newFileName = destinationPath + filename;
            out = new FileOutputStream(newFileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", "Exception in copyFile() of "+newFileName);
            Log.e("tag", "Exception in copyFile() "+e.toString());
        }
    }
    //读取文件夹
    public static File readFolderByPath(String path){
        File file = new File(path);
        if(file.exists()){
            return file;
        }
        return null;
    }
    //读取文件夹下的所有文件
    public static File[] readFileInFolderByPath(String path){
        File file = readFolderByPath(path);
        if(file!=null){
            File[] files = file.listFiles();
            return files;
        }
        return null;
    }
}
