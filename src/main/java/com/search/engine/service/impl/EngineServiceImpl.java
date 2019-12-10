package com.search.engine.service.impl;

import com.search.engine.entity.WeatherDo;
import com.search.engine.entity.WeatherVideo;
import com.search.engine.model.KeywordModel;
import com.search.engine.model.SearchModel;
import com.search.engine.repository.WeatherDoEsRepository;
import com.search.engine.repository.WeatherVideoRepository;
import com.search.engine.service.EngineService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * @author xuh
 * @date 2019/12/10
 */
@Slf4j
@Service
public class EngineServiceImpl implements EngineService {

    @Autowired
    private WeatherVideoRepository videoRepository;

    @Autowired
    private WeatherDoEsRepository hisWeatherRepository;

//    @Autowired
//    private HighlightResultMapper highlightResultMapper;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * 关键词查询
     *
     * @param keyword
     * @return
     */
    @Override
    public SearchModel searchKeyword(KeywordModel keyword) {
        this.getWeatherHisData(keyword);

        this.getWeatherVideo(keyword);
        return null;
    }

    /**
     * 查询视频信息
     *
     * @param keyword
     */
    private void getWeatherVideo(KeywordModel keyword) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //  高亮
        HighlightBuilder.Field highLightField = new HighlightBuilder.Field("title")
                .preTags("<span style='color: red'>")
                .postTags("</span>");
        //高亮
        queryBuilder.withHighlightFields(highLightField);
        //  条件
        queryBuilder.withQuery(QueryBuilders.multiMatchQuery(keyword.getKeyword(),
                "title", "content", "tags", "source", "author"));

        //  分页
        PageRequest pageRequest = PageRequest.of(keyword.getPage(), keyword.getSize());

        //  评分降序
        queryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));

        queryBuilder.withPageable(pageRequest);
        //查询
        Page<WeatherVideo> videos = esTemplate.queryForPage(queryBuilder.build(), WeatherVideo.class);
    }

    private void getWeatherHisData(KeywordModel keyword) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //  高亮
        HighlightBuilder.Field highLightField = new HighlightBuilder.Field("title")
                .preTags("<span style='color: red'>")
                .postTags("</span>");
        //高亮
        queryBuilder.withHighlightFields(highLightField);
        //  条件
        queryBuilder.withQuery(QueryBuilders.multiMatchQuery(keyword.getKeyword(),
                "title", "province", "city", "county", "fitting", "avoid"));

        //  分页
        PageRequest pageRequest = PageRequest.of(keyword.getPage(), keyword.getSize());

        //  评分降序
        queryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));

        queryBuilder.withPageable(pageRequest);
        //查询
        Page<WeatherDo> weatherDoAggregatedPage = esTemplate.queryForPage(queryBuilder.build(), WeatherDo.class);
    }
}
