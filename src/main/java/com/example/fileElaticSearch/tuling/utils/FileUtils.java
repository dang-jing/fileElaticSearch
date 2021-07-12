package com.example.fileElaticSearch.tuling.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author：小党
 * @Date:Created in 11:53  2021/6/16
 * @Explain：    对文件的操作类
 */
@Service
public class FileUtils {



    //创建临时文件夹以及文件
    public static String createDirectory(String dirName, String fileName, JSONObject jsonObject) throws Exception {
        //临时文件创建路径
        String folder = System.getProperty("java.io.tmpdir");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df.format(new Date());
        //dataset文件夹
        String dir = folder + File.separator + "dataset" + time;
        File file = null;
        File parentFile = null;
        try {
            if ("json".equals(dirName)) {
                file = new File(dir + File.separator + dirName + File.separator + fileName + ".json");
                exists(file);
                String jsonString = jsonObject.toString();
                // 将格式化后的字符串写入文件
                Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                write.write(jsonString);
                write.flush();
                write.close();
            }
            if ("img".equals(dirName)) {
                String imgFilePath = dir + File.separator + dirName + File.separator + fileName + ".jpg";
                file = new File(dir + File.separator + dirName + File.separator + fileName + ".jpg");
                exists(file);
                String data = jsonObject.get("imageData").toString();
                GenerateImage(data, imgFilePath);
            }
        } catch (IOException e) {
            System.out.println("io流错误");
        }
        /*System.out.println(file.getAbsolutePath());
        System.out.println(file.isDirectory());
        System.out.println(file.isFile());*/
        return dir;
    }

    //判断路径是否存在
    public static void exists(File file) {
        //拿到文件的文件夹路径
        File parentFile = file.getParentFile();
        //判断所有文件夹是否存在，不存在创建
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        //判断文件是否存在，不存在创建
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {

            }
            System.out.println(file);
        }
    }

    //删除文件夹
    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }

    // base64字符串转化成图片
    public static boolean GenerateImage(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            //String imgFilePath = "C:/Users/Star/Desktop/test22.png";// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void main(String[] args) {
        String folder = System.getProperty("java.io.tmpdir");
        System.out.println(folder);
    }
}
