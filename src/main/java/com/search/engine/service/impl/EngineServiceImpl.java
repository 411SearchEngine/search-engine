package com.search.engine.service.impl;

import com.search.engine.entity.ProvinceCityDo;
import com.search.engine.repository.ProvinceCityDoRepository;
import com.search.engine.service.EngineService;
import com.search.engine.util.DataCrawlerUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuh
 * @date 2019/12/2
 */
@Slf4j
@Service
public class EngineServiceImpl implements EngineService {

    @Autowired
    private ProvinceCityDoRepository provinceCityDoRepository;

    private static String baseUrl = "http://www.tianqihoubao.com";

    /**
     * 查询历史 过去一年内的数据
     */
    @Override
    public void findWeather() {
        String url = "http://www.tianqihoubao.com/lishi/";

//        provinceCityDo.setZhProvince("浙江");
//        provinceCityDo.setEnProvince("zhejiang");
//        provinceCityDo.setZhCity("杭州");
//        provinceCityDo.setEnCity("hangzhou");
//        this.provinceCityDoRepository.save(provinceCityDo);
        List<ProvinceCityDo> cityData = this.getCityData(url);

        for (ProvinceCityDo cityDo : cityData) {
            Document document = DataCrawlerUtil.getDocument(baseUrl + "/lishi/" + cityDo.getEnProvince() + ".htm");

            Elements select = document.select(".citychk dl");

            String href = select.select("dl dt a").attr("href");
            Elements dl_dd_a = select.select("dl dd a");
            for (Element element : dl_dd_a) {
                String text = element.text();
            }

        }

    }

    private List<ProvinceCityDo> getCityData(String url) {

        List<ProvinceCityDo> urls = new ArrayList<>();
        Document document = DataCrawlerUtil.getDocument(url);

        Elements select = document.select(".citychk dl dt");
        for (Element element : select) {
            ProvinceCityDo provinceCityDo = new ProvinceCityDo();
            String href = element.select("a[href]").attr("href");
            String text = element.text();

            provinceCityDo.setZhProvince(text);
            provinceCityDo.setEnProvince(href.replace("/lishi/", "").replace(".htm", ""));
            urls.add(provinceCityDo);
        }
        return urls;
    }


}
