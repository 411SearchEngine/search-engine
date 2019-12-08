package com.search.engine.entity;

import com.search.engine.config.AutoIncKey;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xuh
 * @date 2019/12/6
 */
@Data
@Document
public class WeatherDo {
    @Id
    @AutoIncKey
    private long id;

    private String title;

    private String province;

    private String city;

    private String county;

    private String lightTemperature;

    private String nightTemperature;

    private String lightWeatherConditions;

    private String nightWeatherConditions;

    private String lightWind;

    private String nightWind;

    private String weatherDate;

    private String solarCalendar;

    private String lunarCalendar;

    private String constellation;

    private String fitting;

    private String avoid;

}
