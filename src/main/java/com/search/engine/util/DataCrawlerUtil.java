package com.search.engine.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

/**
 * @author xuh
 * @date 2019/12/4
 */
@Slf4j
public class DataCrawlerUtil {

    /**
     * jsoup发送请求
     *
     * @param url 请求URL
     * @return Document网页数据
     */
    public static Document getDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(url).openStream(), "gb2312", url);
//            doc = Jsoup.connect(url)
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36")
//                    .timeout(15000)
//                    .get();
        } catch (Exception e) {
            log.debug("获取网页数据出错 {} , 错误 : {}", url, e.getMessage());
        }
        return doc;
    }


}
