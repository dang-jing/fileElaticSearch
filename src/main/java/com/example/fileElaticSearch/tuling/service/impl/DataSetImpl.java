package com.example.fileElaticSearch.tuling.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.fileElaticSearch.tuling.service.DataSet;
import com.example.fileElaticSearch.tuling.utils.ElasticSearchUtils;
import com.example.fileElaticSearch.tuling.utils.FileUtils;
import com.example.fileElaticSearch.tuling.utils.Zip;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @Author：小党
 * @Date:Created in 9:17  2021/6/15
 * @Explain：
 */
@Service
public class DataSetImpl implements DataSet {

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Autowired
    private static Zip zip;

    @Override
    public void SLD(HttpServletResponse response) throws Exception {
        List<Map> list = elasticSearchUtils.findAssignData("dataset", "1400标准题", "", "", "", "", "", "", "");
        //System.out.println(list.get(0));
        String dir = "";
        String downloadName = "";
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = (JSONObject) list.get(i).get("label");
            String imagePath = jsonObject.get("imagePath").toString().split("\\.")[0];
            System.out.println(imagePath);

            if (imagePath.matches("\\\\")) {
                downloadName = URLEncoder.encode(imagePath.replaceAll(".*\\\\", ""), "utf-8");
            } else {
                downloadName = imagePath;
            }
            System.out.println(downloadName);
            dir = fileUtils.createDirectory("json", downloadName, jsonObject);
            fileUtils.createDirectory("img", downloadName, jsonObject);

        }
        //创建输出接口
        OutputStream out = response.getOutputStream();
        System.out.println(dir);
        //拿到压缩后将文件夹格式
        byte[] data = zip.createZip(dir);
        //System.out.println(data);
        response.reset();
        String dirName = URLEncoder.encode(dir.replaceAll(".*\\\\", ""), "utf-8") + ".zip";
        response.setHeader("Content-Disposition", "attachment;fileName=" + dirName);
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=utf-8");
        IOUtils.write(data, out);
        out.flush();
        out.close();
        File file = new File(dir);
        fileUtils.deleteFile(file);
        //System.out.println(jsonObject.get("imagePath"));
    }


    public static void main(String[] args) throws Exception {
        //new FileUtils().exists(new File("C:\\Users\\dangc\\AppData\\Local\\Temp\\dataset2021-06-16\\json\\508347ed-c9c7-11eb-b177-9c2976e90c22.json"));
        /*byte[] a = new Zip().createZip("D:\\图片\\分类\\11");
        System.out.println(a.toString());*/
        //new DataSetImpl().SLD();
    }


}
