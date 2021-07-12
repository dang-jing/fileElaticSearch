package com.example.fileElaticSearch.tuling.utils;

import com.example.fileElaticSearch.tuling.pojo.MoXing;
import org.elasticsearch.action.delete.DeleteResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author：小党
 * @Date:Created in 9:212021/4/27
 * @Explain：
 */

public interface ESInterface {
    public void addDocument(String index, MoXing moXing)throws IOException;
    public String search(String name,String model,String type) throws IOException;
    public DeleteResponse deleteRequest(String index,String id) throws IOException;
}
