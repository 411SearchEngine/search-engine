package com.search.engine.repository;

import com.search.engine.entity.WeatherDo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author xuh
 * @date 2019/12/6
 */
public interface WeatherDoMoRepository extends MongoRepository<WeatherDo, Long> {
}
