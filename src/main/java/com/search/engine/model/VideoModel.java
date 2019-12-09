package com.search.engine.model;

import lombok.Data;

import java.util.List;

/**
 * @author xuh
 * @date 2019/12/9
 */
@Data
public class VideoModel {
    private long channelid;

    private String title;

    private String date;

    private String url;

    private String photo;

    private String contenturl;

    private List<String> videoTag;
}
