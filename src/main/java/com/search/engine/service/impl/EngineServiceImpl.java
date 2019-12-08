package com.search.engine.service.impl;

import com.search.engine.entity.ProvinceCityDo;
import com.search.engine.entity.WeatherDo;
import com.search.engine.repository.ProvinceCityDoRepository;
import com.search.engine.repository.WeatherDoEsRepository;
import com.search.engine.repository.WeatherDoMoRepository;
import com.search.engine.service.EngineService;
import com.search.engine.util.DataCrawlerUtil;
import com.search.engine.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author xuh
 * @date 2019/12/2
 */
@Slf4j
@Service
public class EngineServiceImpl implements EngineService {

    @Autowired
    private ProvinceCityDoRepository provinceCityDoRepository;

    @Autowired
    private WeatherDoEsRepository weatherDoEsRepository;

    @Autowired
    private WeatherDoMoRepository weatherDoMoRepository;

    private static String baseUrl = "http://www.tianqihoubao.com";

    /**
     * 查询历史 过去一年内的数据
     */
    @Override
    public void findWeather() {
        String url = "http://www.tianqihoubao.com/lishi/";
        List<ProvinceCityDo> cityData = this.getCityData(url);

        List<ProvinceCityDo> provinceCityDos = new ArrayList<>();
        for (ProvinceCityDo cityDo : cityData) {
            //  获取市县字段信息
            Document document = DataCrawlerUtil.getDocument(baseUrl + "/lishi/" + cityDo.getEnProvince() + ".htm");

            String enProvince = cityDo.getEnProvince();
            String zhProvince = cityDo.getZhProvince();

            Elements select = document.select(".citychk dl");
            for (Element element : select) {

                //  获取县的信息
                String en_city = element.select("a").first().attr("href").replace("/lishi/", "").replace(".html", "");
                String zh_city = element.select("a b").first().text();

//                System.out.println("            县  : " + text + "   href : " + itemHref);
                Elements dd_a = element.select("dd a");

                for (Element countys : dd_a) {
                    //  获取县的信息
                    String zh_county = countys.text();
                    String en_county = countys.attr("href").replace("/lishi/", "").replace(".html", "");

                    ProvinceCityDo provinceCityDo = new ProvinceCityDo();
                    provinceCityDo.setEnProvince(enProvince);
                    provinceCityDo.setZhProvince(zhProvince);

                    provinceCityDo.setEnCity(en_city);
                    provinceCityDo.setZhCity(zh_city);

                    provinceCityDo.setEnCounty(en_county);
                    provinceCityDo.setZhCounty(zh_county);
                    provinceCityDos.add(provinceCityDo);
                }

            }

        }

        this.provinceCityDoRepository.saveAll(provinceCityDos);
    }

    /**
     * 查询历史天气
     */
    @Override
    public void findHistory(long id, String time) {
        List<ProvinceCityDo> provinceCityDos = this.provinceCityDoRepository.findByIdGreaterThanEqual(id);

        String baseUrl = "http://www.tianqihoubao.com/lishi/";
//        taiyuan/month/201101.html
//        http://www.tianqihoubao.com/lishi/taiyuan/month/201101.html

        for (ProvinceCityDo provinceCityDo : provinceCityDos) {

            String enProvince = provinceCityDo.getEnProvince();

            String enCounty = provinceCityDo.getEnCounty();

            String start_time = "20101231";
            if (StringUtils.isNotEmpty(time)) {
                start_time = time;
            }
            for (; ; ) {
                //  设置日期
                start_time = DateUtil.getNextDate(start_time);

                if (StringUtils.equals(DateUtil.getyyyyMMdd(new Date()), start_time)) {
                    break;
                }

                String url = baseUrl + enCounty + "/" + start_time + ".html";
                Document pageInfo = DataCrawlerUtil.getDocument(url);
                String text = pageInfo.select("#bd #content h1").text();
                System.out.println(text);

                this.findWeatherHistoryThread(new ArrayList<>());
            }


        }

    }

    /**
     * 多线程查询信息
     *
     * @param urls URL
     * @return 返回历史信息
     */
    private List<WeatherDo> findWeatherHistoryThread(List<String> urls) {
        final List<WeatherDo> weatherDos = Collections.synchronizedList(new ArrayList<>());

//        开启多线程 最多5个线程
        ExecutorService exs = Executors.newFixedThreadPool(5);
        List<Future<WeatherDo>> futureList = new ArrayList<>();

        //启动线程池，固定线程数为page 大小
        for (String url : urls) {
            futureList.add(exs.submit(new Callable<WeatherDo>() {
                @Override
                public WeatherDo call() throws Exception {
                    //  发送HTTP请求. 返回组装数据
                    Document pageInfo = DataCrawlerUtil.getDocument(url);
                    WeatherDo weatherDoInfo = DataCrawlerUtil.getWeatherDoInfo(pageInfo);
                    return weatherDoInfo;
                }
            }));
        }

        //  结果归集
        for (Future<WeatherDo> listFuture : futureList) {
            for (; ; ) {
                try {
                    if (listFuture.isDone() && !listFuture.isCancelled()) {
                        weatherDos.add(listFuture.get());
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return weatherDos;
    }


    /**
     * 获取省份的信息
     *
     * @param url
     * @return
     */
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
