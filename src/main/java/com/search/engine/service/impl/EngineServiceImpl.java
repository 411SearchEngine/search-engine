package com.search.engine.service.impl;

import com.search.engine.entity.WeatherDo;
import com.search.engine.entity.WeatherVideo;
import com.search.engine.model.KeywordModel;
import com.search.engine.model.KeywordSearchModel;
import com.search.engine.model.SearchModel;
import com.search.engine.repository.WeatherDoEsRepository;
import com.search.engine.repository.WeatherVideoRepository;
import com.search.engine.service.EngineService;
import com.search.engine.util.DateUtil;
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

import java.util.ArrayList;
import java.util.List;

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
    public KeywordSearchModel searchKeyword(KeywordModel keyword) {
        KeywordSearchModel searchModels = this.getWeatherHisData(keyword);
        KeywordSearchModel searchModels1 = this.getWeatherVideo(keyword);

        List<SearchModel> searchModels2 = searchModels1.getSearchModels();
        searchModels2.addAll(searchModels.getSearchModels());

        searchModels1.setCount(Math.max(searchModels.getCount(), searchModels1.getCount()));
        searchModels1.setCurrent(keyword.getPage());
        searchModels1.setSearchModels(searchModels2);
        return searchModels1;
    }

    /**
     * 查询视频信息
     *
     * @param keyword
     * @return
     */
    private KeywordSearchModel getWeatherVideo(KeywordModel keyword) {
        KeywordSearchModel keywordSearchModel = new KeywordSearchModel();

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        List<SearchModel> searchModels = new ArrayList<>();

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
        List<WeatherVideo> videosContent = videos.getContent();
        for (WeatherVideo weatherVideo : videosContent) {
            SearchModel searchModel = new SearchModel();

            searchModel.setId(String.valueOf(weatherVideo.getId()));
            searchModel.setTitle(weatherVideo.getTitle());
            searchModel.setContent(weatherVideo.getContent());
            searchModel.setPublic_date(weatherVideo.getPublicDate());
            searchModel.setSource_url(weatherVideo.getUrl());
            searchModel.setType("video");

            searchModels.add(searchModel);
        }
        keywordSearchModel.setSearchModels(searchModels);
        keywordSearchModel.setCount(videos.getTotalPages());

        return keywordSearchModel;
    }

    private KeywordSearchModel getWeatherHisData(KeywordModel keyword) {
        KeywordSearchModel keywordSearchModel = new KeywordSearchModel();
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
        List<WeatherDo> content = weatherDoAggregatedPage.getContent();

        List<SearchModel> searchModels = new ArrayList<>();
        for (WeatherDo weatherDo : content) {
            SearchModel searchModel = new SearchModel();

            searchModel.setId(String.valueOf(weatherDo.getId()));
            searchModel.setTitle(weatherDo.getTitle());
            searchModel.setContent(weatherDo.getTitle() + "" + weatherDo.getNightWeatherConditions());
            searchModel.setPublic_date(DateUtil.getStandardDate(weatherDo.getWeatherDate()));
            searchModel.setSource_url(weatherDo.getUrl());
            searchModel.setType("history");

            searchModels.add(searchModel);
        }
        keywordSearchModel.setSearchModels(searchModels);
        keywordSearchModel.setCount(weatherDoAggregatedPage.getTotalPages());

        return keywordSearchModel;
    }
}
