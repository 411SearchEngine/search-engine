package com.search.engine.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by xuh
 * DATE 2019/11/29 1:14.
 * version 1.0
 */
@Data
public class TestDo {

    @Id
    private long id;

    private String test;

}
