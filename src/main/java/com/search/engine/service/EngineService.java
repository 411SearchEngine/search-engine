package com.search.engine.service;

import com.search.engine.model.KeywordModel;
import com.search.engine.model.SearchModel;

import java.util.List;

/**
 * @author xuh
 * @date 2019/12/10
 */
public interface EngineService {
    List<SearchModel> searchKeyword(KeywordModel keyword);
}
