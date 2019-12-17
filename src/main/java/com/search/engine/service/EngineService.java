package com.search.engine.service;

import com.search.engine.model.KeywordModel;
import com.search.engine.model.KeywordSearchModel;

/**
 * @author xuh
 * @date 2019/12/10
 */
public interface EngineService {
    KeywordSearchModel searchKeyword(KeywordModel keyword);
}
