//package com.search.engine.config;
//
//import com.fasterxml.jackson.core.JsonEncoding;
//import com.fasterxml.jackson.core.JsonFactory;
//import com.fasterxml.jackson.core.JsonGenerator;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.get.MultiGetItemResponse;
//import org.elasticsearch.action.get.MultiGetResponse;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHitField;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.ElasticsearchException;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.ScriptedField;
//import org.springframework.data.elasticsearch.core.AbstractResultMapper;
//import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
//import org.springframework.data.elasticsearch.core.EntityMapper;
//import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
//import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
//import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
//import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
//import org.springframework.data.mapping.context.MappingContext;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ReflectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.nio.charset.Charset;
//import java.util.*;
//
///**
// * Created by xuh
// * DATE 2019/12/10 23:40.
// * version 1.0
// */
//@Component
//public class HighlightResultMapper extends AbstractResultMapper {
//
//    private MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;
//
//    public HighlightResultMapper() {
//        super(new DefaultEntityMapper());
//    }
//
//    public HighlightResultMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
//        super(new DefaultEntityMapper());
//        this.mappingContext = mappingContext;
//    }
//
//    public HighlightResultMapper(EntityMapper entityMapper) {
//        super(entityMapper);
//    }
//
//    public HighlightResultMapper(
//            MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext,
//            EntityMapper entityMapper) {
//        super(entityMapper);
//        this.mappingContext = mappingContext;
//    }
//
//    @Override
//    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//        long totalHits = response.getHits().totalHits();
//        List<T> results = new ArrayList<>();
//        for (SearchHit hit : response.getHits()) {
//            if (hit != null) {
//                T result = null;
//                if (StringUtils.hasText(hit.sourceAsString())) {
//                    result = mapEntity(hit.sourceAsString(), clazz);
//                } else {
//                    result = mapEntity(hit.getFields().values(), clazz);
//                }
//                setPersistentEntityId(result, hit.getId(), clazz);
//                setPersistentEntityVersion(result, hit.getVersion(), clazz);
//                populateScriptFields(result, hit);
//                buildHighlightField(hit, result);
//                results.add(result);
//            }
//        }
//
//        return new AggregatedPageImpl<T>(results, pageable, totalHits, response.getAggregations(), response.getScrollId());
//    }
//
//    private <T> void populateScriptFields(T result, SearchHit hit) {
//        if (hit.getFields() != null && !hit.getFields().isEmpty() && result != null) {
//            for (java.lang.reflect.Field field : result.getClass().getDeclaredFields()) {
//                ScriptedField scriptedField = field.getAnnotation(ScriptedField.class);
//                if (scriptedField != null) {
//                    String name = scriptedField.name().isEmpty() ? field.getName() : scriptedField.name();
//                    SearchHitField searchHitField = hit.getFields().get(name);
//                    if (searchHitField != null) {
//                        field.setAccessible(true);
//                        try {
//                            field.set(result, searchHitField.getValue());
//                        } catch (IllegalArgumentException e) {
//                            throw new ElasticsearchException("failed to set scripted field: " + name + " with value: "
//                                    + searchHitField.getValue(), e);
//                        } catch (IllegalAccessException e) {
//                            throw new ElasticsearchException("failed to access scripted field: " + name, e);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private <T> T mapEntity(Collection<SearchHitField> values, Class<T> clazz) {
//        return mapEntity(buildJSONFromFields(values), clazz);
//    }
//
//    private String buildJSONFromFields(Collection<SearchHitField> values) {
//        JsonFactory nodeFactory = new JsonFactory();
//        try {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            JsonGenerator generator = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
//            generator.writeStartObject();
//            for (SearchHitField value : values) {
//                if (value.getValues().size() > 1) {
//                    generator.writeArrayFieldStart(value.getName());
//                    for (Object val : value.getValues()) {
//                        generator.writeObject(val);
//                    }
//                    generator.writeEndArray();
//                } else {
//                    generator.writeObjectField(value.getName(), value.getValue());
//                }
//            }
//            generator.writeEndObject();
//            generator.flush();
//            return new String(stream.toByteArray(), Charset.forName("UTF-8"));
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    @Override
//    public <T> T mapResult(GetResponse response, Class<T> clazz) {
//        T result = mapEntity(response.getSourceAsString(), clazz);
//        if (result != null) {
//            setPersistentEntityId(result, response.getId(), clazz);
//            setPersistentEntityVersion(result, response.getVersion(), clazz);
//        }
//        return result;
//    }
//
//    @Override
//    public <T> LinkedList<T> mapResults(MultiGetResponse responses, Class<T> clazz) {
//        LinkedList<T> list = new LinkedList<>();
//        for (MultiGetItemResponse response : responses.getResponses()) {
//            if (!response.isFailed() && response.getResponse().isExists()) {
//                T result = mapEntity(response.getResponse().getSourceAsString(), clazz);
//                setPersistentEntityId(result, response.getResponse().getId(), clazz);
//                setPersistentEntityVersion(result, response.getResponse().getVersion(), clazz);
//                list.add(result);
//            }
//        }
//        return list;
//    }
//
//    private <T> void setPersistentEntityId(T result, String id, Class<T> clazz) {
//
//        if (mappingContext != null && clazz.isAnnotationPresent(Document.class)) {
//
//            ElasticsearchPersistentEntity<?> persistentEntity = mappingContext.getRequiredPersistentEntity(clazz);
//            ElasticsearchPersistentProperty idProperty = persistentEntity.getIdProperty();
//
//            // Only deal with String because ES generated Ids are strings !
//            if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
//                persistentEntity.getPropertyAccessor(result).setProperty(idProperty, id);
//            }
//
//        }
//    }
//
//    private <T> void setPersistentEntityVersion(T result, long version, Class<T> clazz) {
//        if (mappingContext != null && clazz.isAnnotationPresent(Document.class)) {
//
//            ElasticsearchPersistentEntity<?> persistentEntity = mappingContext.getPersistentEntity(clazz);
//            ElasticsearchPersistentProperty versionProperty = persistentEntity.getVersionProperty();
//
//            // Only deal with Long because ES versions are longs !
//            if (versionProperty != null && versionProperty.getType().isAssignableFrom(Long.class)) {
//                // check that a version was actually returned in the response, -1 would indicate that
//                // a search didn't request the version ids in the response, which would be an issue
//                Assert.isTrue(version != -1, "Version in response is -1");
//                persistentEntity.getPropertyAccessor(result).setProperty(versionProperty, version);
//            }
//        }
//    }
//
//    /**
//     * 给目标字段设置高亮
//     **/
//    private <T> T buildHighlightField(SearchHit searchHit, T source){
//        Assert.notNull(source, "buildHighlightField source is null!");
//        Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
//        if(CollectionUtils.isEmpty(highlightFields)){
//            return source;
//        }
//        //遍历设置的高亮字段
//        for (Map.Entry<String, HighlightField> highlightFieldEntry : highlightFields.entrySet()) {
//            try {
//                //获取高亮字段名称和高亮后的值
//                String fieldName = highlightFieldEntry.getKey();
//                HighlightField highlightField = highlightFieldEntry.getValue();
//                String fieldValue = highlightField.getFragments()[0].toString();
//                //获取目标字段方法反射调用
//                Field field = ReflectionUtils.findField(source.getClass(), fieldName);
//                String setMethodName = "set" + StringUtils.capitalize(fieldName);
//                Method setMethod = ReflectionUtils.findMethod(source.getClass(), setMethodName, field.getType());
//                ReflectionUtils.invokeMethod(setMethod, source, fieldValue);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return source;
//    }
//
//}