package com.search.engine.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @author xuh
 * @date 2019/12/6
 */
@Data
@Document(indexName = "weather",type = "historyWeather")
public class WeatherDo {
    @Id
    private long id;

    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String title;

    private String province;

    private String city;

    private String county;

    private String lightTemperature;

    private String nightTemperature;

    private String lightWeatherConditions;

    private String nightWeatherConditions;

    @Field(searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String lightWind;

    @Field(searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String nightWind;

    private String weatherDate;

    @Field(searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String fitting;

    @Field(searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String avoid;

    private String url;

}
