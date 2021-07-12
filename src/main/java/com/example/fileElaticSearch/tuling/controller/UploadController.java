package com.example.fileElaticSearch.tuling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author：小党
 * @Date:Created in 16:502021/4/25
 * @Explain：
 */


@Controller
public class UploadController {

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(HttpServletRequest request, HttpServletResponse response) {

        return "hello hanfei";
    }
}
