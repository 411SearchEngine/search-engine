package com.search.engine.repository;

import com.search.engine.entity.WeatherDo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xuh
 * @date 2019/12/6
 */
public interface WeatherDoEsRepository extends ElasticsearchRepository<WeatherDo, Long> {
}
