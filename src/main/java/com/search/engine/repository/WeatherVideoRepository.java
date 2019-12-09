package com.search.engine.repository;

import com.search.engine.entity.WeatherVideo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xuh
 * @date 2019/12/9
 */
public interface WeatherVideoRepository extends ElasticsearchRepository<WeatherVideo, Long> {
}
