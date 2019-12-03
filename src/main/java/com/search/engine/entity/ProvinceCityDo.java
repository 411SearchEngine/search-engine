package com.search.engine.entity;

import com.search.engine.config.AutoIncKey;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xuh
 * @date 2019/12/3
 */
@Data
@Document
public class ProvinceCityDo {

    @Id
    @AutoIncKey
    private long id;

    private String zhProvince;

    private String enProvince;

    private String zhCity;

    private String enCity;

}
