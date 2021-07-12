package com.example.fileElaticSearch.tuling.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @Author：小党
 * @Date:Created in 14:29  2021/6/15
 * @Explain：
 */
@Service
public class ElasticSearchUtils {
    // 初始化api客户端
    public static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("192.168.3.10", 9200, "http")
            ));

    //设置页码和长度
    public static List<Map> findAssignData(String index, String topic_type, String subject_quality, String topic_hierarchy, String txt_graph, String is_answer, String picture_type, String picture_quality, String lable) {
        RequestOptions.Builder options = RequestOptions.DEFAULT.toBuilder();
        //设置连接的索引
        SearchRequest request = new SearchRequest(index);
        //搜索源构造对象
        SearchSourceBuilder search = new SearchSourceBuilder();
        //多条件查询
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(topic_type)) {
            boolBuilder.must(QueryBuilders.matchPhraseQuery("topic _type", topic_type));
        }
        if (!StringUtils.isEmpty(subject_quality)) {
            boolBuilder.must(QueryBuilders.matchPhraseQuery("subject_quality", subject_quality));
        }
        if (!StringUtils.isEmpty(topic_hierarchy)) {
            boolBuilder.must(QueryBuilders.matchPhraseQuery("topic_hierarchy", topic_hierarchy));
        }
        if (!StringUtils.isEmpty(is_answer)) {
            boolBuilder.must(QueryBuilders.matchPhraseQuery("is_answer", is_answer));
        }
        if (!StringUtils.isEmpty(picture_type)) {
            boolBuilder.must(QueryBuilders.matchPhraseQuery("picture_type", picture_type));
        }
        if (!StringUtils.isEmpty(picture_quality)) {
            boolBuilder.must(QueryBuilders.matchPhraseQuery("picture_quality", picture_quality));
        }
        //根据字段排序
        //search.sort("createDate.keyword", SortOrder.DESC);
        search.query(boolBuilder);
        //页码
        //search.from();
        //一次拿的长度
        search.size(10);

        request.source(search);
        SearchResponse response = null;
        try {
            response = client.search(request,RequestOptions.DEFAULT);
            //client.close();
        } catch (IOException e) {

        }
        Integer a = 0;
        List<Map> list = new ArrayList();
        for (SearchHit hit : response.getHits().getHits()) {
            JSONObject sourceMap =  JSON.parseObject(hit.getSourceAsString());
            //System.out.println(((JSONObject)sourceMap.get("label")).get("imagePath"));
            list.add(sourceMap);
        }
        System.out.println(list.size());
        return list;
    }

    public static void main(String[] args) {
        findAssignData("dataset", "1400标准题", "", "", "", "", "", "", "");
       // String str = "{a=1,b=2,c=3}";
       // JSONObject jsonObject = JSONArray.parseObject(str);
        //System.out.println(jsonObject);
    }
}
