package com.search.engine.repository;

import com.search.engine.entity.ProvinceCityDo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author xuh
 * @date 2019/12/3
 */
public interface ProvinceCityDoRepository extends MongoRepository<ProvinceCityDo, Long> {
}
