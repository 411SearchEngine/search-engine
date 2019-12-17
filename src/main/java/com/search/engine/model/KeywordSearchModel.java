package com.search.engine.model;

import lombok.Data;

import java.util.List;

/**
 * Created by xuh
 * DATE 2019/12/17 21:18.
 * version 1.0
 */
@Data
public class KeywordSearchModel {

    private List<SearchModel> searchModels;

    private int count;

    private int current;

}
