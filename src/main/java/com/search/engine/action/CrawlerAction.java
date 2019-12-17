package com.search.engine.action;

import com.search.engine.service.CrawlerService;
import com.search.engine.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuh
 * @date 2019/12/2
 */
@RestController
public class CrawlerAction {

    @Autowired
    private CrawlerService crawlerService;

    @GetMapping("/find/weather")
    public R findWeather() {
        this.crawlerService.findWeather();
        return R.ok();
    }


    @GetMapping("/find/lishi")
    public R findHistory(@Param("id") long id, @Param("time") String time) {
        this.crawlerService.findHistory(id, time);
        return R.ok();
    }

    @GetMapping("/find/video")
    public R findVideo() {
        this.crawlerService.findVideo();
        return R.ok();
    }

}
