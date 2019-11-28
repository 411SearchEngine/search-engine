package com.search.engine.repository;

import com.search.engine.entity.TestDo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by xuh
 * DATE 2019/11/29 1:15.
 * version 1.0
 */
public interface TestRepository extends MongoRepository<TestDo, Long> {
    TestDo findByTest(String test);
}
