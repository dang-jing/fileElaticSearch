package com.example.fileElaticSearch.tuling.controller;

import com.example.fileElaticSearch.tuling.service.PictureService;
import com.example.fileElaticSearch.tuling.service.impl.PictureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：小党
 * @Date:Created in 16:08  2021/5/11
 * @Explain：        图片分类接口
 */
@RestController
@ResponseBody
public class PictureController {

    @Autowired
    private PictureService pictureService;
    private List<String> list;

/*
   public static void main(String[] args) throws Exception {
       */
/* String a="http://localhost:8080/tupian?imgPath=";
        String b="D:\\图片\\1-100\\000c9c3eef5443f59a6be1d7de211f9d.jp";
        String gbk = URLEncoder.encode(b, "gbk");
        System.out.println(a+gbk);*//*

       new PictureController().getPaths("D:\\图片\\分类\\单题");
    }
*/

    @CrossOrigin(origins = "*",maxAge = 3600)
    @RequestMapping(value = "/getPaths",produces = "application/json")
    public String getPaths(String imgPaths) throws Exception {
        /**
         *@Author: dang on 2021/5/12 14:16
          * @Param filepath     穿个文件夹路径
         *@return:java.util.List<java.lang.String>      返回文件夹内所有文件路径
         *@description:     页面加载时获取
         */
//D:\图片\1-100
        System.out.println(imgPaths);
        this.list = pictureService.getPaths(imgPaths);
        //System.out.println(list);
        return "{\"path\":\""+URLEncoder.encode(list.get(0),"UTF-8")+"\",\"length\":\""+list.size()+"\"}";
    }

    @CrossOrigin(origins = "*",maxAge = 3600)
    @RequestMapping("/getTest")
    public String getTest(){
        return "aaa";
    }

    /**
     *@Author: dang on 2021/5/12 14:18
     * @Param request
     * @Param response     响应一个图片
     * @Param imgPath      传图片路径
     *@return:void
     *@description:     根据图片路径返会图片给页面
     */
    @RequestMapping(value = "/tupian", method = RequestMethod.GET)
    public void IoReadImage( HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "imgPath") String imgPath) throws IOException {

        pictureService.IoReadImage(request, response, imgPath);
    }

    /**
     *@Author: dang on 2021/5/12 14:22
     * @Param imgPath   当前页面的图片路径
     * @Param type     图片的类型
     *@return:void
     *@description:
     */
    @CrossOrigin(origins = "*",maxAge = 3600)
    @RequestMapping(value = "/passImg",produces = "application/json")
    public String passImg(int path, String type)throws Exception {
        String imgPath = list.get(path);
        pictureService.passImg(imgPath, type);
        int size=list.size();
        int i = path + 1;
        int length=size-i;
        return "{\"path\":\""+URLEncoder.encode(list.get(i),"UTF-8")+"\",\"length\":\""+length+"\"}";
    }

  /*  @CrossOrigin(origins = "*",maxAge = 3600)
    @RequestMapping(value = "/passImg",produces = "application/json")
    public String passImg(int path, String type)throws Exception {
        System.out.println("path=="+path);
        System.out.println("type=="+type);
        return "{\"path\":\"你好呀\"}";
    }*/
}
