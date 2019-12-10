package com.search.engine.service.impl;

import com.search.engine.entity.ProvinceCityDo;
import com.search.engine.util.DataCrawlerUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuh
 * @date 2019/12/4
 */
public class CrawlerServiceImplTest {

    @Test
    public void findWeather() {
        String baseUrl = "http://www.tianqihoubao.com";
        String url = "http://www.tianqihoubao.com/lishi/";

        List<ProvinceCityDo> cityData = new ArrayList<>();
        Document document = DataCrawlerUtil.getDocument(url);

        Elements select = document.select(".citychk dl dt");
        for (Element element : select) {
            ProvinceCityDo provinceCityDo = new ProvinceCityDo();
            String href = element.select("a[href]").attr("href");
            String text = element.text();

            provinceCityDo.setZhProvince(text);
            provinceCityDo.setEnProvince(href.replace("/lishi/", "").replace(".htm", ""));
            cityData.add(provinceCityDo);
        }





        List<ProvinceCityDo> provinceCityDos = new ArrayList<>();
        for (ProvinceCityDo cityDo : cityData) {
            Document doc = DataCrawlerUtil.getDocument(baseUrl + "/lishi/" + cityDo.getEnProvince() + ".htm");

            System.out.println(cityDo.getZhProvince() + "    :   " + cityDo.getEnProvince());

            Elements cities = doc.select(".citychk dl");

            //  获取市
            for (Element city : cities) {
                String enCity = city.select("dt a").attr("href");
                String zhCity = city.select("dt b").text();

//                doc.select(".citychk dl dd")

            }







//            String href = select1.select("dl dt a").attr("href");
//            String text1 = select1.select("dl dt a b").text();
//
//            Elements dl_dd_a = select1.select("dl dd a");
//            for (Element element : dl_dd_a) {
//                String href1 = element.attr("href");
//                String text = element.text();
//                System.out.println("   === city : " + text1  + "  href : " + href + "    :   市xian : " + text + "  -- " + href1);
//            }

        }

    }
}