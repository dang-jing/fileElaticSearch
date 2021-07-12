package com.example.fileElaticSearch.tuling.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author：小党
 * @Date:Created in 10:532021/4/26
 * @Explain：模型索引内容
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MoXing {
    //模型
    private String model;
    private String type;
    private String address;
}
