package com.search.engine.action;

import com.search.engine.service.EngineService;
import com.search.engine.util.R;
import org.elasticsearch.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

}
