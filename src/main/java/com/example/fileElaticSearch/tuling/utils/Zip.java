package com.example.fileElaticSearch.tuling.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author：小党
 * @Date:Created in 14:222021/4/27
 * @Explain：
 */
@Service
public class Zip {


    /**
     *@Author: dang on 2021/4/27 14:29
     * @Param srcSource
     *@return:byte[]
     *@description:获取zip的文件流
     */
    public byte[] createZip(String srcSource) throws Exception{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        //将目标文件打包成zip导出
        File file = new File(srcSource);
        a(zip,file,"");
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     *@Author: dang on 2021/4/27 14:29
     * @Param zip 输出
     * @Param file 原文件
     * @Param dir
     *@return:void
     *@description:
     */
    public void a(ZipOutputStream zip, File file, String dir) throws Exception {

        //如果当前的是文件夹，则进行进一步处理
        if (file.isDirectory()) {
            System.out.println("文件夹");
            //得到文件列表信息
            File[] files = file.listFiles();
            //将文件夹添加到下一级打包目录
            zip.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                a(zip, files[i], dir + files[i].getName());         //递归处理
            }
        } else {   //当前的是文件，打包处理
            System.out.println("文件");
            //文件输入流
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(dir);
            zip.putNextEntry(entry);
            zip.write(FileUtils.readFileToByteArray(file));
            IOUtils.closeQuietly(bis);
            zip.flush();
            zip.closeEntry();
        }
    }
}
