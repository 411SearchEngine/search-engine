package com.search.engine.service;

import com.search.engine.entity.TestDo;

/**
 * Created by xuh
 * DATE 2019/11/29 1:13.
 * version 1.0
 */
public interface TestService {
    TestDo findTest(String test);

    void addTest(String test, long id);
}
