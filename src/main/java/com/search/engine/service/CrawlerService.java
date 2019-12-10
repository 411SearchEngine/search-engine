package com.search.engine.service;

/**
 * @author xuh
 * @date 2019/12/2
 */
public interface CrawlerService {

    /**
     * 查询信息
     */
    void findWeather();

    /**
     * 查询历史天气
     * @param id
     * @param time
     */
    void findHistory(long id, String time);

    /**
     * 爬取视频网站
     */
    void findVideo();

}
