package com.example.fileElaticSearch.tuling.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author：小党
 * @Date:Created in 10:57  2021/5/12
 * @Explain：
 */
public interface PictureService {
    List<String> getPaths(String filepath);
    void IoReadImage(HttpServletRequest request, HttpServletResponse response, String imgPath) throws IOException;
    void passImg(String imgPath,String imgName);
}
