package com.search.engine.util;

import com.search.engine.entity.WeatherDo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
//            doc = Jsoup.parse(new URL(url).openStream(), "gb2312", url);
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36")
                    .timeout(10000)
                    .get();
        } catch (Exception e) {
            log.debug("获取网页数据出错 {} , 错误 : {}", url, e.getMessage());
        }
        return doc;
    }

    public static WeatherDo getWeatherDoInfo(Document document) {
        WeatherDo weatherDo = null;
        if (document == null) {
            return null;
        }
        String title = document.select("#bd #content h1").text();

        if (StringUtils.isNotEmpty(title)) {
            weatherDo = new WeatherDo();
            weatherDo.setTitle(title);
            //  获取table信息
            Elements tables = document.select("#bd table tbody tr");
            for (Element table : tables) {
                Elements td = table.select("td");
                for (Element element : td) {
                    if (StringUtils.isNotEmpty(element.text())) {
//                    if (element.i) {
//
//                    }
                        String ele = table.child(0).text();
                        String light = table.child(1).text();
                        String night = table.child(2).text();
                        if (StringUtils.equals(ele, "天气状况")) {
                            weatherDo.setLightWeatherConditions(light);
                            weatherDo.setNightWeatherConditions(night);
                        }
                        if (StringUtils.equals(ele, "温度/气温")) {
                            weatherDo.setLightTemperature(light);
                            weatherDo.setNightTemperature(night);
                        }
                        if (StringUtils.equals(ele, "风力风向")) {
                            weatherDo.setTitle(title);
                            weatherDo.setLightWind(light);
                            weatherDo.setNightWind(night);
                        }
                    }
                }

            }

            String fitting = document.select("#content font").first().text();
            String avoid = document.select("#content font").last().text();
            weatherDo.setFitting(fitting);
            weatherDo.setAvoid(avoid);
        }
        return weatherDo;
    }

}
