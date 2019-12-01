package com.search.engine.service.impl;

import com.search.engine.entity.TestDo;
import com.search.engine.repository.TestEsRepository;
import com.search.engine.repository.TestRepository;
import com.search.engine.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuh
 * DATE 2019/11/29 1:13.
 * version 1.0
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestEsRepository testEsRepository;

    @Autowired
    private TestRepository testRepository;

    @Override
    public List<TestDo> findTest(String test) {
//        return this.testRepository.findByTest(test);
        return this.testEsRepository.findByTest(test);
    }

    @Override
    public void addTest(String test, long id) {
        TestDo testDo = new TestDo();
        testDo.setId(id);
        testDo.setTest(test);

        this.testEsRepository.save(testDo);

        this.testRepository.save(testDo);
    }
}
