package com.search.engine.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by xuh
 * DATE 2019/11/29 1:14.
 * version 1.0
 */
@Data
@Document(indexName = "weather",type = "test", shards = 1,replicas = 0, refreshInterval = "-1")
public class TestDo {

    @Id
    private long id;

    private String test;

}
