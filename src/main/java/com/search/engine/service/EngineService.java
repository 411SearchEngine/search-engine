package com.search.engine.service;

import com.search.engine.model.KeywordModel;
import com.search.engine.model.SearchModel;

/**
 * @author xuh
 * @date 2019/12/10
 */
public interface EngineService {
    SearchModel searchKeyword(KeywordModel keyword);
}
