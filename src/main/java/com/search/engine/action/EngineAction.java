package com.search.engine.action;

import com.search.engine.model.KeywordModel;
import com.search.engine.service.CrawlerService;
import com.search.engine.service.EngineService;
import com.search.engine.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuh
 * @date 2019/12/10
 */
@RestController
public class EngineAction {

    @Autowired
    private EngineService engineService;

    @PostMapping("/search/keyword")
    public R searchKeyword(@RequestBody KeywordModel keyword) {

        return R.ok(this.engineService.searchKeyword(keyword));
    }

}
