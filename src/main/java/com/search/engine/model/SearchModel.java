package com.search.engine.model;

import lombok.Data;

import java.util.Date;

/**
 * @author xuh
 * @date 2019/12/10
 */
@Data
public class SearchModel {

    private String id;

    private String title;

    private String content;

    private Date public_date;

    private String source_url;
}
