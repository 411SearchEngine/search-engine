//package com.search.engine.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.ResultsMapper;
//
///**
// * Created by xuh
// * DATE 2019/12/10 23:42.
// * version 1.0
// */
//@Configuration
//public class ElasticSearchConfig {
//    @Autowired
//    private ElasticsearchTemplate esTemplate;
//
//    @Bean
//    @Primary
//    public ResultsMapper highlightResultMapper() {
//        return new HighlightResultMapper(esTemplate.getElasticsearchConverter().getMappingContext());
//    }
//}
