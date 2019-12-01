package com.search.engine.repository;

import com.search.engine.entity.TestDo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by xuh
 * DATE 2019/12/1 18:26.
 * version 1.0
 */
public interface TestEsRepository extends ElasticsearchRepository<TestDo, Long> {
    List<TestDo> findByTest(String test);
}
