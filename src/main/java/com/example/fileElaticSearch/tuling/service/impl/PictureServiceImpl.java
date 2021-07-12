package com.example.fileElaticSearch.tuling.service.impl;

import com.example.fileElaticSearch.tuling.service.PictureService;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Author：小党
 * @Date:Created in 10:58  2021/5/12
 * @Explain：
 */
@Service
public class PictureServiceImpl implements PictureService {


    /**
     * @Author: dang on 2021/5/12 11:10
     * @Param filepath  文件夹路径
     * @return:java.util.List<java.lang.String> 返回该文件夹下所有文件路径
     * @description:
     */
    @Override
    public List<String> getPaths(String filepath) {
        //System.out.println(filepath);
        File file = new File(filepath);
        File[] tempList;
        List<String> list = new ArrayList<String>();
        //判断地址是否为文件夹
        if (file.isDirectory()) {
            //拿到该地址下所有文件路径
            tempList = file.listFiles();
            //输出数组
            //System.out.println(Arrays.toString(tempList));
            list = new ArrayList<String>();

            for (int i = 0; i < tempList.length; i++) {
                //判断是否为文件
                if (tempList[i].isFile()) {
                    //System.out.println(tempList[i].getAbsolutePath());
                    //拿到指定数组中的路径，并加入到集合中
                    list.add(tempList[i].getAbsolutePath());
                }
                if (tempList[i].isDirectory()) {
                    //如果是文件夹调用本方法，并将list内容加入到list集合中
                    //System.out.println(getPaths(tempList[i].getAbsolutePath()));
                    list.addAll(getPaths(tempList[i].getAbsolutePath()));
                }
            }

        } else if (file.isFile()) {
            String s = file.getAbsolutePath();
            list.add(s);
        }
        return list;
    }

    /**
     * @Author: dang on 2021/5/12 11:11
     * @Param request
     * @Param response     响应给页面指定路径的图片
     * @Param imgPath  文件路径
     * @return:void
     * @description:
     */
    @Override
    public void IoReadImage(HttpServletRequest request, HttpServletResponse response, String imgPath) throws IOException {

        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            //获取图片存放路径
            //String imgPath = "D:\\学习\\linux文件\\shiJuan-1-500\\0ac201615a63409db498477004f34d9f.jpg";
            ips = new FileInputStream(new File(imgPath));
            response.setContentType("image/png");
            out = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = ips.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            ips.close();
        }
    }

    /**
     * @Author: dang on 2021/5/12 11:39
     * @Param imgPath   要复制的图片路径
     * @Param type     文件夹名
     * @return:void
     * @description:
     */
    @Override
    public void passImg(String imgPath, String type) {

        try {
            File oldFile = new File(imgPath);
            String newPath = "D:\\图片\\分类\\" + type;
            File newFile = new File(newPath);
            //判断文件夹是否存在，不存在则新建
            if (!newFile.exists()) {
                (new File(newPath)).mkdir();
            }
            String fileName = newPath + "\\" + oldFile.getName();
            File testFile = new File(fileName);
            if (!testFile.exists()) {
                FileInputStream input = new FileInputStream(oldFile);
                FileOutputStream output = new FileOutputStream(testFile);
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = input.read(b)) != -1) {
                    output.write(b, 0, len);
                }
                output.flush();
                output.close();
                input.close();
                oldFile.delete();
            }
        } catch (Exception e) {
            System.out.println("传输图片错误");
            e.printStackTrace();
        }
    }


    /**
     * @Author: dang on 2021/5/19 9:46
     * @Param path  路径
     * @return:void
     * @description: 拿到指定路径下的所有的文件夹及其下的文件数
     */
    public static Map<String, Integer> getPathsNumber(String path) {

        Map<String, Integer> map = new HashMap<>();
        File files = new File(path);
        //拿到指定路径下所有的文件对象
        File[] tempList = files.listFiles();
        //记录文件数量
        int filerCount = 0;
        //String filerName="";
        //拿到文件名
        String filerName = files.getName();
        //System.out.println(filerName);
        //System.out.println(Arrays.toString(tempList));
        for (File file : tempList) {
            //判断是否为文件夹
            if (file.isDirectory()) {
                //文件夹再次调用本方法
                map.putAll(getPathsNumber(file.getAbsolutePath()));
            } else {
                filerCount++;
            }
        }

        map.put(filerName, filerCount);
        return map;
    }

    public static void main(String[] args) {
        Map<String, Integer> pathsNumber = getPathsNumber("D:\\测试\\中考\\imgs");
        int values=0;
        /*//遍历所有values
        for (int value:pathsNumber.values()){
            values+=value;
        }*/
        for (String key:pathsNumber.keySet()){
            Integer value = pathsNumber.get(key);
            System.out.println(key+"="+value);
            values+=value;
        }
        System.out.println("总和为："+values);

        //System.out.println(new PictureServiceImpl().getPaths("D:\\图片\\分类\\图形"));
    }
}
