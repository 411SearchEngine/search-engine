package com.search.engine.action;

import com.search.engine.service.EngineService;
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
public class SearchAction {

    @Autowired
    private EngineService engineService;

    @GetMapping("/find/weather/")
    public R findWeather() {
        this.engineService.findWeather();
        return R.ok();
    }


    @GetMapping("/find/lishi")
    public R findHistory(@Param("id") long id, @Param("time") String time) {
        this.engineService.findHistory(id, time);
        return R.ok();
    }

    @GetMapping("/find/video")
    public R findVideo() {
        this.engineService.findVideo();
        return R.ok();
    }

}
