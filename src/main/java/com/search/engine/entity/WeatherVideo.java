package com.search.engine.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

/**
 * @author xuh
 * @date 2019/12/9
 */
@Data
@Document(indexName = "weather_video", type = "video")
public class WeatherVideo {

    @Id
    private long id;

    @Field(searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String content;

    @Field(searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String title;

    @Field(searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String tags;

    private String url;

    private String videoUrl;

    private String photo;

    private String source;

    private String author;

    private Date publicDate;
}
