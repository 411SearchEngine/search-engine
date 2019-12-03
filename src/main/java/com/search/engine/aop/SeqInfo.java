package com.search.engine.aop;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xuh
 * @date 2019/12/3
 */
@Data
@Document(collection = "sequence")
public class SeqInfo {
    @Id
    private String id;//主键

    private Long seqId;//序列值

    private String collName;//集合名称
}
