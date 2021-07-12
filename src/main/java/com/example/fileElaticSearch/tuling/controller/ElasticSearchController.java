package com.example.fileElaticSearch.tuling.controller;

import com.example.fileElaticSearch.tuling.service.DataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @Author：小党
 * @Date:Created in 16:11  2021/6/16
 * @Explain：        目前实现下载接口
 */
@RestController
@ResponseBody
public class ElasticSearchController {

    @Autowired
    private DataSet dataSet;

    @RequestMapping(value = "/dataset", method = {RequestMethod.GET})
    public void getDataSet(HttpServletResponse response) throws Exception {
        dataSet.SLD(response);
    }
}
