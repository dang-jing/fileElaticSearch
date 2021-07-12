package com.example.fileElaticSearch.tuling.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：小党
 * @Date:Created in 10:352021/4/26
 * @Explain：es数据库连接
 */
@Configuration
public class ElasticSearcherConfig {

    @Bean("restHighLevelClient")
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.3.10", 9200, "http")
        ));
        return client;
    }
}
