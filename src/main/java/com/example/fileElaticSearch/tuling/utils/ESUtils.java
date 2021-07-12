package com.example.fileElaticSearch.tuling.utils;

import com.alibaba.fastjson.JSON;
import com.example.fileElaticSearch.tuling.pojo.MoXing;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author：小党
 * @Date:Created in 10:492021/4/26
 * @Explain：
 */
@Component
public class ESUtils implements ESInterface{


    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            new HttpHost("192.168.3.10", 9200, "http")
        ));;

    /**
     * 添加数据
     *
     * @Author: dang on 2021/4/26 17:34
     * @Param moXing
     * @return:void
     * @description:
     */

    public void addDocument(String index, MoXing moXing) throws IOException {


        IndexRequest request = new IndexRequest(index);
        request.timeout();
        request.source(JSON.toJSONString(moXing), XContentType.JSON);

        //try {
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
        /*} catch (IOException e) {
            System.out.println("es写入错误");
        }*/

    }

    /**
     * 多条件查询es
     *
     * @Author: dang on 2021/4/26 20:25
     * @Param name
     * @Param model
     * @Param type
     * @return:java.lang.String返回地址
     * @description: 根据名字，模型，类型查找文件地址
     */

    public String search(String name, String model, String type) throws IOException {

        SearchRequest request = new SearchRequest(name);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchPhraseQueryBuilder q2 = QueryBuilders.matchPhraseQuery("model", model);
        MatchPhraseQueryBuilder q3 = QueryBuilders.matchPhraseQuery("type", type);
        BoolQueryBuilder must = QueryBuilders.boolQuery().must(q2).must(q3);
        sourceBuilder.query(must);

        request.source(sourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);

        String a = "";
        for (SearchHit documentFields : search.getHits().getHits()) {
            //拿到所有字段的内容，后再拿到指定字段内容
            a = documentFields.getSourceAsMap().get("address").toString();
        }
        client.close();
        return a;
    }

    /**
     * 根据id删除文档信息
     *
     * @Author: dang on 2021/4/27 14:51
     * @Param id
     * @return:org.elasticsearch.action.delete.DeleteResponse 返回请求结果
     * @description:
     */

    public DeleteResponse deleteRequest(String index, String id) throws IOException {

        DeleteRequest request = new DeleteRequest(index, id);
        request.timeout();
        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        return delete;
    }


}
