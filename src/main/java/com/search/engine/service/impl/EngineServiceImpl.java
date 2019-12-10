package com.search.engine.service.impl;

import com.search.engine.entity.ProvinceCityDo;
import com.search.engine.entity.WeatherDo;
import com.search.engine.entity.WeatherVideo;
import com.search.engine.model.VideoModel;
import com.search.engine.repository.ProvinceCityDoRepository;
import com.search.engine.repository.WeatherDoEsRepository;
import com.search.engine.repository.WeatherVideoRepository;
import com.search.engine.service.EngineService;
import com.search.engine.util.DataCrawlerUtil;
import com.search.engine.util.DateUtil;
import com.search.engine.util.HttpRequest;
import com.search.engine.util.JsonUtil;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

//    @Autowired
//    private WeatherDoMoRepository weatherDoMoRepository;

    @Autowired
    private WeatherVideoRepository videoRepository;

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
            List<WeatherDo> weatherHistoryDos = new ArrayList<>();
            String enProvince = provinceCityDo.getEnProvince();

            String enCounty = provinceCityDo.getEnCounty();

            String start_time = "20101231";
            if (StringUtils.isNotEmpty(time)) {
                start_time = time;
            }
            ArrayList<String> urls = new ArrayList<>();
            for (; ; ) {
                //  设置日期
                start_time = DateUtil.getNextDate(start_time);

                if (StringUtils.equals(DateUtil.getyyyyMMdd(new Date()), start_time)) {

                    if (urls.size() != 0) {
                        List<WeatherDo> weatherDos = this.findWeatherHistoryThread(urls, provinceCityDo, start_time);
                        weatherHistoryDos.addAll(weatherDos);
                    }

//                    this.weatherDoMoRepository.saveAll(weatherHistoryDos);
                    this.weatherDoEsRepository.saveAll(weatherHistoryDos);
                    break;
                }

                String url = baseUrl + enCounty + "/" + start_time + ".html";
//                Document pageInfo = DataCrawlerUtil.getDocument(url);
//                String text = pageInfo.select("#bd #content h1").text();
                System.out.println(DateUtil.getyyyyMMdd(start_time) + provinceCityDo.getZhCounty() + "历史天气查询");
                urls.add(url);
                if (urls.size() == 1) {
                    List<WeatherDo> weatherDos = this.findWeatherHistoryThread(urls, provinceCityDo, start_time);

                    weatherHistoryDos.addAll(weatherDos);
                    urls = new ArrayList<>();
                }
            }
        }

    }

    /**
     * 爬取视频网站
     */
    @Override
    public void findVideo() {
        String baseUrl = "http://video.weather.com.cn";
        int page = 1;

        List<WeatherVideo> weatherVideos;
        for (; ; ) {
            weatherVideos = new ArrayList<>();
            String url = "http://video.weather.com.cn/weather/video/weather_video_retrieval?keyword=" +
                    "&page=" + page + "&per_num=12&hotSpot=0&forecast=0&solarTerm=0&life=0&popularScience=0";

            String httpData = HttpRequest.sendGet(url, null);
            String arrData = JsonUtil.getJsonNode(httpData, "arr").toString();
            List<VideoModel> videoModels = JsonUtil.jsonToGenericObj(arrData, List.class, VideoModel.class);
            page++;
            if (videoModels != null && videoModels.size() > 0) {

                for (VideoModel videoModel : videoModels) {

                    System.out.println(videoModel.getChannelid() + " " + videoModel.getTitle());
                    String contentUrl = baseUrl + videoModel.getContenturl();

                    Document document = DataCrawlerUtil.getDocument(contentUrl);
                    WeatherVideo weatherVideo = this.getVideoInfo(document, videoModel);
                    if (weatherVideo != null) {
                        weatherVideo.setId(System.nanoTime());
                        weatherVideo.setUrl(contentUrl);
                        weatherVideos.add(weatherVideo);
                    }
                }

                this.videoRepository.saveAll(weatherVideos);
            } else {
                break;
            }
        }
    }


    private static Pattern mp4Url = Pattern.compile("(http://vod.weather.com.cn/video/)(.*)((.mp4)|(.MP4))");
    private WeatherVideo getVideoInfo(Document document, VideoModel videoModel) {
        WeatherVideo weatherVideo = null;

        if (document != null) {
            Elements contentInfo = document.select(".detail-txt div");
            weatherVideo = new WeatherVideo();

            weatherVideo.setTitle(videoModel.getTitle());
            weatherVideo.setTags(JsonUtil.objToJson(videoModel.getVideoTag()));
            weatherVideo.setPhoto(videoModel.getPhoto());

            String src = document.select("._tdp_vbox video").attr("src");
            weatherVideo.setVideoUrl(src);

            for (Element item : contentInfo) {
                Elements spans = item.select("span");
                String key = spans.first().text();
                String value = spans.last().text();

                if (StringUtils.contains(key, "视频内容")) {
                    weatherVideo.setContent(value);
                }
                if (StringUtils.contains(key, "来源")) {
                    weatherVideo.setSource(value);
                }
                if (StringUtils.contains(key, "编辑")) {
                    weatherVideo.setAuthor(value);
                }
                if (StringUtils.contains(key, "发布时间")) {
                    weatherVideo.setPublicDate(DateUtil.getDate(value));
                }
            }

            //  查询视频连接
            Elements script = document.getElementsByTag("script");
            for (Element element : script) {
                String text = element.html();
                Matcher matcher;
                if ((matcher = mp4Url.matcher(text)).find()) {
                    String video = matcher.group(0);

                    weatherVideo.setVideoUrl(video);
                }
            }
        }


        return weatherVideo;
    }

    /**
     * 多线程查询信息
     *
     * @param urls           URL
     * @param provinceCityDo
     * @param start_time
     * @return 返回历史信息
     */
    private List<WeatherDo> findWeatherHistoryThread(List<String> urls, ProvinceCityDo provinceCityDo, String start_time) {
        final List<WeatherDo> weatherDos = Collections.synchronizedList(new ArrayList<>());

//        开启多线程 最多5个线程
        ExecutorService exs = Executors.newFixedThreadPool(5);
        List<Future<WeatherDo>> futureList = new ArrayList<>();

        //启动线程池，固定线程数为page 大小
        for (String url : urls) {
            futureList.add(exs.submit(() -> {
                //  发送HTTP请求. 返回组装数据
                Document pageInfo = DataCrawlerUtil.getDocument(url);
                WeatherDo weatherDoInfo = DataCrawlerUtil.getWeatherDoInfo(pageInfo);
                if (weatherDoInfo != null) {
                    weatherDoInfo.setProvince(provinceCityDo.getZhProvince());
                    weatherDoInfo.setCity(provinceCityDo.getZhCity());
                    weatherDoInfo.setCounty(provinceCityDo.getZhCounty());

                    weatherDoInfo.setId(System.nanoTime());
                    weatherDoInfo.setWeatherDate(start_time);
                    weatherDoInfo.setUrl(url);
                }

                return weatherDoInfo;
            }));
        }

        //  结果归集
        for (Future<WeatherDo> listFuture : futureList) {
            for (; ; ) {
                try {
                    if (listFuture.isDone() && !listFuture.isCancelled()) {
                        if (weatherDos != null)
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
