package com.search.engine.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 历史天气预报数据
 *
 * Created by xuh
 * DATE 2019/12/3 23:43.
 * version 1.0
 */
@Data
@Document(collection = "weather")
public class WeatherHisDo {

    @Id
    private Long id;

    private String province;

    private String city;

    private String area;

    /**
     * 天气状况 白天
     */
    private String lightState;

    /**
     * 天气状况 夜晚
     */
    private String nightState;

//    private String
}
