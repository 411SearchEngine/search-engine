package com.search.engine.service.impl;

import com.search.engine.entity.ProvinceCityDo;
import com.search.engine.repository.ProvinceCityDoRepository;
import com.search.engine.service.EngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuh
 * @date 2019/12/2
 */
@Slf4j
@Service
public class EngineServiceImpl implements EngineService {

    @Autowired
    private ProvinceCityDoRepository provinceCityDoRepository;
    /**
     * 查询历史 过去一年内的数据
     */
    @Override
    public void findWeather() {
        ProvinceCityDo provinceCityDo = new ProvinceCityDo();
        provinceCityDo.setZhProvince("浙江");
        provinceCityDo.setEnProvince("zhejiang");
        provinceCityDo.setZhCity("杭州");
        provinceCityDo.setEnCity("hangzhou");
        this.provinceCityDoRepository.save(provinceCityDo);

        System.out.println(provinceCityDo.getId());
    }

}
