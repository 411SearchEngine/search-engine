package com.search.engine.model;

import lombok.Data;

/**
 * @author xuh
 * @date 2019/12/10
 */
@Data
public class KeywordModel {
    private String keyword;

    private int page;

    private int size;
}
